package com.agendoc.security.handler;

import com.agendoc.common.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Writes a consistent API error when authentication is required
 * or the provided authentication is invalid.
 */
@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private static final String UNAUTHORIZED_MESSAGE =
            "Debes iniciar sesión para acceder a este recurso.";

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authenticationException
    ) throws IOException, ServletException {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ApiError error = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                UNAUTHORIZED_MESSAGE,
                request.getRequestURI()
        );

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(
                response.getOutputStream(),
                error
        );
    }
}