package com.edugate.edugateapi.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handles bad login (wrong email/password)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid email or password"));
    }
    
    // Handles duplicate email on registration
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        if (ex.getMessage().contains("users_email_key")) {
             return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "An account with this email already exists."));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Database error occurred."));
    }

    // Handles validation errors (e.g., @NotBlank, @Email)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()
                ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Validation failed", "fields", errors));
    }
}