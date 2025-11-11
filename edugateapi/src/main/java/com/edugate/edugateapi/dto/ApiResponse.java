// In a new file, e.g., src/main/java/com/edugate/edugateapi/dto/ApiResponse.java
package com.edugate.edugateapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // This hides 'null' fields in the JSON
public class ApiResponse<T> {

    @Builder.Default
    private boolean success = true;
    
    private int status;
    
    @Builder.Default
    private Instant timestamp = Instant.now();
    
    private String path;
    
    // This will hold error messages or a success message
    private String message;
    
    // This is the generic part for your data
    private T data;

    // --- Helper Methods for Convenience ---

    /**
     * Creates a standardized SUCCESS response.
     */
    public static <T> ApiResponse<T> success(T data, String message, String path, HttpStatus status) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(status.value())
                .message(message)
                .path(path)
                .data(data)
                .build();
    }

    /**
     * Creates a standardized ERROR response.
     */
    public static <T> ApiResponse<T> error(String message, HttpStatus status, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status.value())
                .message(message)
                .path(path)
                .data(null) // No data on error
                .build();
    }

     /**
     * Creates a standardized ERROR response with validation data.
     */
    public static <T> ApiResponse<T> error(T data, String message, HttpStatus status, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status.value())
                .message(message)
                .path(path)
                .data(data) // e.g., for validation fields
                .build();
    }
}