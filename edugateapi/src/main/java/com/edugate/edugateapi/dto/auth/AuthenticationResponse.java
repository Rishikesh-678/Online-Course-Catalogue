package com.edugate.edugateapi.dto.auth;

import com.edugate.edugateapi.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    
    private String token; // The JWT
    private String email;
    private String fullName;
    private Role role;
    private Long id;
}