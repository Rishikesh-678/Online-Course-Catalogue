// package com.edugate.edugateapi.controller;

// import org.springframework.dao.DataIntegrityViolationException;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;

// import java.util.Map;
// import java.util.stream.Collectors;

// @ControllerAdvice
// public class GlobalExceptionHandler {

//     // Handles bad login (wrong email/password)
//     @ExceptionHandler(AuthenticationException.class)
//     public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                 .body(Map.of("error", "Invalid email or password"));
//     }

//     // Handles duplicate email on registration
//     @ExceptionHandler(DataIntegrityViolationException.class)
//     public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
//         if (ex.getMessage().contains("users_email_key")) {
//              return ResponseEntity.status(HttpStatus.CONFLICT)
//                 .body(Map.of("error", "An account with this email already exists."));
//         }
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                 .body(Map.of("error", "Database error occurred."));
//     }

//     // Handles validation errors (e.g., @NotBlank, @Email)
//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//         Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
//                 .collect(Collectors.toMap(
//                         fieldError -> fieldError.getField(),
//                         fieldError -> fieldError.getDefaultMessage()
//                 ));
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                 .body(Map.of("error", "Validation failed", "fields", errors));
//     }
// }
package com.edugate.edugateapi.controller;

import com.edugate.edugateapi.dto.ApiResponse; // <-- Import new class
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.access.AccessDeniedException;// <-- Import WebRequest

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

        // Helper to get the path
        private String getPath(WebRequest request) {
                return request.getDescription(false).replace("uri=", "");
        }

        // Handles bad login (wrong email/password)
        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex,
                        WebRequest request) {
                ApiResponse<Object> response = ApiResponse.error(
                                "Invalid email or password",
                                HttpStatus.UNAUTHORIZED,
                                getPath(request));
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // Handles duplicate email on registration
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                        WebRequest request) {
                String message = "Database error occurred.";
                HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

                if (ex.getMessage().contains("users_email_key")) {
                        message = "An account with this email already exists.";
                        status = HttpStatus.CONFLICT;
                }

                ApiResponse<Object> response = ApiResponse.error(message, status, getPath(request));
                return new ResponseEntity<>(response, status);
        }

        // Handles validation errors (e.g., @NotBlank, @Email)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
                        MethodArgumentNotValidException ex, WebRequest request) {
                // Collect field errors
                Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                                .collect(Collectors.toMap(
                                                fieldError -> fieldError.getField(),
                                                fieldError -> fieldError.getDefaultMessage()));

                // Use the error() overload that accepts data
                ApiResponse<Map<String, String>> response = ApiResponse.error(
                                errors,
                                "Validation failed",
                                HttpStatus.BAD_REQUEST,
                                getPath(request));

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex,
                        WebRequest request) {
                ApiResponse<Object> response = ApiResponse.error(
                                "Access Denied: You do not have permission to access this resource.",
                                HttpStatus.FORBIDDEN,
                                getPath(request));
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex, WebRequest request) {
                // You should log the 'ex' variable to your server logs for debugging
                // e.g., log.error("Unhandled exception occurred", ex);

                ApiResponse<Object> response = ApiResponse.error(
                                "An unexpected internal server error occurred.",
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                getPath(request));
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}