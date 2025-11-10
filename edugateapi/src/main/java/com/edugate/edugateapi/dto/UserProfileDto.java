package com.edugate.edugateapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// This DTO is for updating the user's profile.
// We only allow changing these two fields.
@Data
public class UserProfileDto {
    
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    private String phoneNumber;
}