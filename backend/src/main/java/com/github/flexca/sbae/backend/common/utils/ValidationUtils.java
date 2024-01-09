package com.github.flexca.sbae.backend.common.utils;

import jakarta.mail.internet.InternetAddress;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$");
    public static boolean isValidEmail(String input) {
        try {
            InternetAddress emailAddr = new InternetAddress(input);
            emailAddr.validate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
