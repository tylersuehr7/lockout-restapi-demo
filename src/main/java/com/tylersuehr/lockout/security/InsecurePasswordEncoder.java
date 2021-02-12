package com.tylersuehr.lockout.security;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Tyler Suehr
 */
@Component
class InsecurePasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(String value) {
        try {
            final MessageDigest sha = MessageDigest.getInstance("SHA-256");
            final byte[] out = sha.digest(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(out);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace(System.err);
            throw new Error("SHA-256 doesn't exist!");
        }
    }

    @Override
    public boolean matches(String rawPassword, String encoded) {
        final String passwordEncoded = encode(rawPassword);
        return passwordEncoded.equals(encoded);
    }
}
