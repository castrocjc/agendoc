package com.agendoc.common.exception;

import java.time.Instant;

/**
 * Standard API error response.
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
}