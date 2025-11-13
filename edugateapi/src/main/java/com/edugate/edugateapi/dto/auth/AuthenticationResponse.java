package com.edugate.edugateapi.dto.auth;

import com.edugate.edugateapi.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response containing JWT and user information")
public class AuthenticationResponse {
    
    @Schema(description = "JWT authentication token for subsequent requests", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token; // The JWT
    
    @Schema(description = "User email address", example = "user@example.com")
    private String email;
    
    @Schema(description = "User's full name", example = "John Doe")
    private String fullName;
    
    @Schema(description = "User role", example = "ROLE_USER")
    private Role role;
    
    @Schema(description = "Unique user identifier", example = "1")
    private Long id;
}