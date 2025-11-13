// package com.edugate.edugateapi.dto;

// import com.fasterxml.jackson.annotation.JsonInclude;
// import lombok.Builder;
// import lombok.Data;
// import org.springframework.http.HttpStatus;

// import java.time.Instant;

// import io.swagger.v3.oas.annotations.media.Schema;
// import com.edugate.edugateapi.dto.UserDto;
// import com.edugate.edugateapi.dto.PendingCourseDto;
// import com.edugate.edugateapi.dto.AdminLogDto;
// import com.edugate.edugateapi.dto.UserProfileDto;
// import com.edugate.edugateapi.dto.auth.AuthenticationResponse;
// import com.edugate.edugateapi.dto.course.CourseResponse;

// @Data
// @Builder
// @JsonInclude(JsonInclude.Include.NON_NULL) // This hides 'null' fields in the JSON
// @Schema(description = "Standard API response envelope")
// public class ApiResponse<T> {


//     @Schema(description = "True if request succeeded, false if error", example = "true")
//     @Builder.Default
//     private boolean success = true;

//     @Schema(description = "HTTP status code", example = "200")
//     private int status;

//     @Schema(description = "Timestamp of response", example = "2025-11-12T14:55:27.830Z")
//     @Builder.Default
//     private Instant timestamp = Instant.now();

//     @Schema(description = "Request path", example = "/api/user/profile/me")
//     private String path;

//     @Schema(description = "Error or success message", example = "Successfully retrieved user profile")
//     private String message;

//     @Schema(description = "Response data (null for errors)",
//         oneOf = {UserDto.class, UserProfileDto.class, CourseResponse.class, PendingCourseDto.class, AdminLogDto.class, AuthenticationResponse.class},
//         example = "{\"id\":1,\"fullName\":\"John Doe\",\"email\":\"john@example.com\",\"phoneNumber\":\"+1-234-567-8900\",\"role\":\"ROLE_USER\"}")
//     private T data;

//     // --- Helper Methods for Convenience ---

//     /**
//      * Creates a standardized SUCCESS response.
//      */
//     public static <T> ApiResponse<T> success(T data, String message, String path, HttpStatus status) {
//         return ApiResponse.<T>builder()
//                 .success(true)
//                 .status(status.value())
//                 .message(message)
//                 .path(path)
//                 .data(data)
//                 .build();
//     }

//     /**
//      * Creates a standardized ERROR response.
//      */
//     public static <T> ApiResponse<T> error(String message, HttpStatus status, String path) {
//         return ApiResponse.<T>builder()
//                 .success(false)
//                 .status(status.value())
//                 .message(message)
//                 .path(path)
//                 .data(null) // No data on error
//                 .build();
//     }

//      /**
//      * Creates a standardized ERROR response with validation data.
//      */
//     public static <T> ApiResponse<T> error(T data, String message, HttpStatus status, String path) {
//         return ApiResponse.<T>builder()
//                 .success(false)
//                 .status(status.value())
//                 .message(message)
//                 .path(path)
//                 .data(data) // e.g., for validation fields
//                 .build();
//     }
// }
// In a new file, e.g., src/main/java/com/edugate/edugateapi/dto/ApiResponse.java
package com.edugate.edugateapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import com.edugate.edugateapi.dto.UserDto;
import com.edugate.edugateapi.dto.PendingCourseDto;
import com.edugate.edugateapi.dto.AdminLogDto;
import com.edugate.edugateapi.dto.UserProfileDto;
import com.edugate.edugateapi.dto.auth.AuthenticationResponse;
import com.edugate.edugateapi.dto.course.CourseResponse;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // This hides 'null' fields in the JSON
@Schema(description = "Standard API response envelope")
public class ApiResponse<T> {


    @Schema(description = "True if request succeeded, false if error", example = "true")
    @Builder.Default
    private boolean success = true;

    @Schema(description = "HTTP status code", example = "200")
    private int status;

    @Schema(description = "Timestamp of response", example = "2025-11-12T14:55:27.830Z")
    @Builder.Default
    private Instant timestamp = Instant.now();

    @Schema(description = "Request path", example = "/api/user/profile/me")
    private String path;

    @Schema(description = "Error or success message", example = "Successfully retrieved user profile")
    private String message;

    @Schema(description = "Response data (null for errors)",
        oneOf = {UserDto.class, UserProfileDto.class, CourseResponse.class, PendingCourseDto.class, AdminLogDto.class, AuthenticationResponse.class},
        example = "{\"id\":1,\"fullName\":\"John Doe\",\"email\":\"john@example.com\",\"phoneNumber\":\"+1-234-567-8900\",\"role\":\"ROLE_USER\"}")
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