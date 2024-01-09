package com.github.flexca.sbae.backend.common.service;

import com.github.flexca.sbae.backend.common.model.generic.KeyType;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import com.github.flexca.sbae.backend.common.model.generic.KeyParam;
import com.github.flexca.sbae.backend.errors.ErrorCode;
import com.github.flexca.sbae.backend.errors.ErrorType;
import com.github.flexca.sbae.backend.errors.SbaeException;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

@Component
public class KeyGenerator {

    private SecureRandom secureRandom = new SecureRandom();

    public KeyPair createKeyPair(KeyParam keyParam) {

        if(KeyType.RSA.equals(keyParam.getKeyType())) {
            return createRsaKeyPair(keyParam);
        } else if(KeyType.EC.equals(keyParam.getKeyType())) {
            return createEcKeyPair(keyParam);
        } else {
            throw new SbaeException("Unsupported key type", ErrorType.INVALID_INPUT, ErrorCode.UNSUPPORTED_KEY_TYPE);
        }
    }

    private KeyPair createRsaKeyPair(KeyParam keyParam) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(keyParam.getSize());
            return keyPairGen.generateKeyPair();
        } catch(Exception e) {
            throw new SbaeException("Failed to generate RSA keypair. Reason: " + e.getMessage(), ErrorType.INTERNAL_SERVER_ERROR,
                    ErrorCode.KEYPAIR_GENERATION_FAILURE, e);
        }
    }

    private KeyPair createEcKeyPair(KeyParam keyParam) {
        try {
            ECGenParameterSpec ecSpec = new ECGenParameterSpec(keyParam.getCurve());
            KeyPairGenerator generator = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
            generator.initialize(ecSpec, secureRandom);
            return generator.generateKeyPair();
        } catch(Exception e) {
            throw new SbaeException("Failed to generate EC keypair. Reason: " + e.getMessage(), ErrorType.INTERNAL_SERVER_ERROR,
                    ErrorCode.KEYPAIR_GENERATION_FAILURE, e);
        }
    }
}
