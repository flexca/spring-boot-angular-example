package com.github.flexca.sbae.backend.common.service;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomGenerator {

    private SecureRandom secureRandom = new SecureRandom();

    public String nextRandomBytesHex(int length) {
        byte[] data = new byte[length];
        secureRandom.nextBytes(data);
        return Hex.encodeHexString(data, true);
    }

}
