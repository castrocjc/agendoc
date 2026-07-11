package com.agendoc.common.exception;

/**
 * Indicates that an operation conflicts with the current
 * state or uniqueness rules of a business resource.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}