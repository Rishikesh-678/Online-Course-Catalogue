package com.edugate.edugateapi.dto;

import com.edugate.edugateapi.model.Role;
import com.edugate.edugateapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User data transfer object exposed by the API")
public class UserDto {
    @Schema(description = "Unique user identifier", example = "1")
    private Long id;

    @Schema(description = "User's full name", example = "John Doe")
    private String fullName;

    @Schema(description = "User's email address", example = "user@example.com")
    private String email;

    @Schema(description = "User's phone number", example = "+1-234-567-8900")
    private String phoneNumber;

    @Schema(description = "User role", example = "ROLE_USER")
    private Role role;

    // Helper method to convert from our @Entity
    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }
}