package com.tylersuehr.lockout.security;

/**
 * @author Tyler Suehr
 */
public interface PasswordEncoder {
    String encode(String value);
    boolean matches(String rawPassword, String encoded);
}
