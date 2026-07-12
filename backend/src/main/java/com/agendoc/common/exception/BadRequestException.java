package com.agendoc.common.exception;

/**
 * Indicates that a request contains invalid data
 * for the requested business operation.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}