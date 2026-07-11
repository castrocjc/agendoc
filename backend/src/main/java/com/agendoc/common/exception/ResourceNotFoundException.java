package com.agendoc.common.exception;

/**
 * Indicates that a requested business resource does not exist
 * or is not available for the current operation.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}