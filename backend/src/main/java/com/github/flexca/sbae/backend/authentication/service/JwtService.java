package com.github.flexca.sbae.backend.authentication.service;

import com.github.flexca.sbae.backend.authentication.mapper.AuthenticationMapper;
import com.github.flexca.sbae.backend.common.config.IgniteConfig;
import com.github.flexca.sbae.backend.common.mapper.DateTimeMapper;
import com.github.flexca.sbae.backend.common.model.generic.KeyParam;
import com.github.flexca.sbae.backend.common.service.DateProvider;
import com.github.flexca.sbae.backend.common.service.KeyGenerator;
import com.github.flexca.sbae.backend.common.utils.PemUtils;
import com.github.flexca.sbae.backend.users.model.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String SIGN_KEY_LOCK = "sign-key-lock";
    private static final String SIGN_KEY_CACHE_KEY_PRIVATE = "sign-key-private";
    private static final String SIGN_KEY_CACHE_KEY_PUBLIC = "sign-key-public";

    private final DateProvider dateProvider;
    private final KeyGenerator keyGenerator;

    private final Ignite ignite;

    @Autowired
    @Qualifier(IgniteConfig.GENERIC_CACHE_NAME)
    private IgniteCache<String, Object> genericCache;

    @PostConstruct
    public void init() {

        IgniteLock signLock = ignite.reentrantLock(SIGN_KEY_LOCK, true, true, true);
        try {
            signLock.lock();
            KeyPair keyPair = keyGenerator.createKeyPair(KeyParam.P_256);
            genericCache.put(SIGN_KEY_CACHE_KEY_PRIVATE, PemUtils.privateKeyToPem(keyPair.getPrivate()));
            genericCache.put(SIGN_KEY_CACHE_KEY_PUBLIC, PemUtils.publicKeyToPem(keyPair.getPublic()));
        } finally {
            signLock.unlock();
        }
    }

    public String createToken(UserDto user) {

        ZonedDateTime expirationDate = dateProvider.currentZonedDateTime().plus(15, ChronoUnit.MINUTES);
        String privateKey = (String) genericCache.get(SIGN_KEY_CACHE_KEY_PRIVATE);

        return Jwts.builder()
                .claims()
                    .subject(user.getId())
                    .expiration(DateTimeMapper.INSTANCE.toDate(expirationDate))
                    .add("user", AuthenticationMapper.INSTANCE.toClaim(user)).and()
                .signWith(PemUtils.pemToPrivateKey(privateKey))
                .compact();
    }

    public String createRefreshToken(String userId) {

        ZonedDateTime expirationDate = dateProvider.currentZonedDateTime().plus(8, ChronoUnit.HOURS);
        String privateKey = (String) genericCache.get(SIGN_KEY_CACHE_KEY_PRIVATE);

        return Jwts.builder()
                .claims()
                .expiration(DateTimeMapper.INSTANCE.toDate(expirationDate))
                .add("userId", userId).and()
                .signWith(PemUtils.pemToPrivateKey(privateKey))
                .compact();
    }

    public Claims extractAllClaims(String token) {

        String publicKey = (String) genericCache.get(SIGN_KEY_CACHE_KEY_PUBLIC);

        return Jwts.parser()
                .verifyWith(PemUtils.pemToPublicKey(publicKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
