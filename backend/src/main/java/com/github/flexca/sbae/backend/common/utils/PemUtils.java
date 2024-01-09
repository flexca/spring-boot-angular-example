package com.github.flexca.sbae.backend.common.utils;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import com.github.flexca.sbae.backend.errors.ErrorCode;
import com.github.flexca.sbae.backend.errors.ErrorType;
import com.github.flexca.sbae.backend.errors.SbaeException;

import java.io.StringReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class PemUtils {

    public static final String PRIVATE_KEY_HEADER = "-----BEGIN PRIVATE KEY-----";
    public static final String PRIVATE_KEY_FOOTER = "-----END PRIVATE KEY-----";

    public static final String PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----";
    public static final String PUBLIC_KEY_FOOTER = "-----END PUBLIC KEY-----";

    public static String privateKeyToPem(PrivateKey privateKey) {
        String base64Encoded = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        return toPem(base64Encoded, PRIVATE_KEY_HEADER, PRIVATE_KEY_FOOTER);
    }

    public static String publicKeyToPem(PublicKey publicKey) {
        String base64Encoded = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        return toPem(base64Encoded, PUBLIC_KEY_HEADER, PUBLIC_KEY_FOOTER);
    }

    public static PublicKey pemToPublicKey(String pem) {
        try {
            PEMParser pemParser = new PEMParser(new StringReader(pem));
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(pemParser.readObject());
            return converter.getPublicKey(publicKeyInfo);
        } catch(Exception e) {
            throw new SbaeException("Failed to parse public key from pem. Reason: {}" + e.getMessage(), ErrorType.INTERNAL_SERVER_ERROR,
                    ErrorCode.PEM_PARSE_FAILURE, e);
        }
    }

    public static PrivateKey pemToPrivateKey(String pem) {
        try {
            PEMParser pemParser = new PEMParser(new StringReader(pem));
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());
            return converter.getPrivateKey(privateKeyInfo);
        } catch(Exception e) {
            throw new SbaeException("Failed to parse private key from pem. Reason: {}" + e.getMessage(), ErrorType.INTERNAL_SERVER_ERROR,
                    ErrorCode.PEM_PARSE_FAILURE, e);
        }
    }

    private static String toPem(String input, String header, String footer) {
        String pem = new StringBuilder(header)
                .append("\n")
                .append(input.replaceAll("(.{64})", "$1\n"))
                .append("\n")
                .append(footer)
                .toString();
        return pem.replace("\n\n", "\n");
    }
}
