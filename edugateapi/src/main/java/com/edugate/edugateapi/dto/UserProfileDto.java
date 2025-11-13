package com.edugate.edugateapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

// This DTO is for updating the user's profile.
// We only allow changing these two fields.
@Data
@Schema(description = "Profile information for the authenticated user")
public class UserProfileDto {
    
    @NotBlank(message = "Full name is required")
    @Schema(description = "Full name of the user", example = "John Doe")
    private String fullName;
    
    @Schema(description = "Contact phone number", example = "+1-234-567-8900")
    private String phoneNumber;
}