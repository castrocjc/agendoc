package com.agendoc.security.authorization;

/**
 * Indicates that an authenticated user is not allowed to perform
 * a requested domain operation.
 */
public class AuthorizationDeniedException extends RuntimeException {

    public AuthorizationDeniedException(String message) {
        super(message);
    }
}