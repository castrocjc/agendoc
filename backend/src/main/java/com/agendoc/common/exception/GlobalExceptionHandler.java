package com.agendoc.common.exception;

import com.agendoc.modules.auth.exception.InvalidCredentialsException;
import com.agendoc.security.authorization.AuthorizationDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import lombok.extern.slf4j.Slf4j;

/**
 * Centralized REST exception handler.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<ApiError> handleInvalidCredentials(
                        InvalidCredentialsException exception,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                Instant.now(),
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidationException(
                        MethodArgumentNotValidException exception,
                        HttpServletRequest request) {

                String message = exception.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(fieldError -> fieldError.getDefaultMessage())
                                .distinct()
                                .collect(Collectors.joining(" "));

                ApiError error = new ApiError(
                                Instant.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                message,
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(error);
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ApiError> handleBadRequest(
                        BadRequestException exception,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                Instant.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(error);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiError> handleResourceNotFound(
                        ResourceNotFoundException exception,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                Instant.now(),
                                HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(error);
        }

        @ExceptionHandler(ConflictException.class)
        public ResponseEntity<ApiError> handleConflict(
                        ConflictException exception,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                Instant.now(),
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(error);
        }

        @ExceptionHandler(MissingServletRequestParameterException.class)
        public ResponseEntity<ApiError> handleMissingRequestParameter(
                        MissingServletRequestParameterException exception,
                        HttpServletRequest request) {
                String message = String.format(
                                "El parámetro '%s' es obligatorio.",
                                exception.getParameterName());

                ApiError error = new ApiError(
                                Instant.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                message,
                                request.getRequestURI());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(error);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiError> handleHttpMessageNotReadable(
                HttpMessageNotReadableException exception,
                HttpServletRequest request) {

        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "El cuerpo de la solicitud es obligatorio y debe contener un JSON válido.",
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
        }

        @ExceptionHandler(AuthorizationDeniedException.class)
        public ResponseEntity<ApiError> handleAuthorizationDenied(
                AuthorizationDeniedException exception,
                HttpServletRequest request) {

                ApiError error = new ApiError(
                        Instant.now(),
                        HttpStatus.FORBIDDEN.value(),
                        HttpStatus.FORBIDDEN.getReasonPhrase(),
                        exception.getMessage(),
                        request.getRequestURI());

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(error);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleUnexpectedException(
                        Exception exception,
                        HttpServletRequest request) {

                log.error(
                                "Unexpected error while processing {} {}",
                                request.getMethod(),
                                request.getRequestURI(),
                                exception);

                ApiError error = new ApiError(
                                Instant.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                "An unexpected error occurred.",
                                request.getRequestURI());

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(error);
        }
}