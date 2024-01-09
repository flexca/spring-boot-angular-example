package com.github.flexca.sbae.backend.common.utils;

import org.apache.commons.codec.binary.Hex;
import com.github.flexca.sbae.backend.errors.ErrorCode;
import com.github.flexca.sbae.backend.errors.ErrorType;
import com.github.flexca.sbae.backend.errors.SbaeException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashUtils {

    public static String sha256(String input) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            return Hex.encodeHexString(digest, true);
        } catch (Exception e) {
            throw new SbaeException("Failure during sha256 calculation. Reason: " + e.getMessage(), ErrorType.INTERNAL_SERVER_ERROR,
                    ErrorCode.SHA256_CALCULATION_ERROR, e);
        }
    }
}
