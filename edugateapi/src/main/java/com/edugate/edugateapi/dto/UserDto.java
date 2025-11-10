package com.edugate.edugateapi.dto;

import com.edugate.edugateapi.model.Role;
import com.edugate.edugateapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
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