package com.github.flexca.sbae.backend.authentication.service;

import com.github.flexca.sbae.backend.authentication.model.LoginRequest;
import com.github.flexca.sbae.backend.authentication.model.LoginResponse;
import com.github.flexca.sbae.backend.authentication.model.RefreshRequest;
import com.github.flexca.sbae.backend.authentication.model.RefreshResponse;
import com.github.flexca.sbae.backend.common.utils.HashUtils;
import com.github.flexca.sbae.backend.errors.ErrorCode;
import com.github.flexca.sbae.backend.errors.ErrorType;
import com.github.flexca.sbae.backend.errors.SbaeException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import com.github.flexca.sbae.backend.users.mapper.UserMapper;
import com.github.flexca.sbae.backend.users.model.UserEntity;
import com.github.flexca.sbae.backend.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new SbaeException("Invalid email or password", ErrorType.NOT_AUTHORIZED, ErrorCode.INVALID_CREDENTIALS));

        if(!user.getPassword().equals(HashUtils.sha256(request.getPassword()))) {
            throw new SbaeException("Invalid email or password", ErrorType.NOT_AUTHORIZED, ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtService.createToken(UserMapper.INSTANCE.toDto(user));
        String refreshToken = jwtService.createRefreshToken(user.getId().toHexString());

        userRepository.setUserTokens(user.getId(), token, refreshToken);

        return new LoginResponse(token, refreshToken);
    }

    public RefreshResponse refreshToken(RefreshRequest request) {

        Claims claims = jwtService.extractAllClaims(request.getRefreshToken());
        Optional<UserEntity> user = userRepository.getById((String) claims.get("userId"));
        if(user.isEmpty()) {
            throw new SbaeException("Invalid refresh token", ErrorType.NOT_AUTHORIZED, ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String authenticationToken = jwtService.createToken(UserMapper.INSTANCE.toDto(user.get()));
        return new RefreshResponse(authenticationToken);
    }
}
