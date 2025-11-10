package com.edugate.edugateapi.service;

import com.edugate.edugateapi.dto.auth.AuthenticationRequest;
import com.edugate.edugateapi.dto.auth.AuthenticationResponse;
import com.edugate.edugateapi.dto.auth.RegisterRequest;
import com.edugate.edugateapi.model.Role;
import com.edugate.edugateapi.model.User;
import com.edugate.edugateapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user.
     * By default, all new users are given the 'ROLE_USER'.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        
        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("User with this email already exists.");
        }

        // Create the new user object
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.ROLE_USER) // *** This is where you set the default role ***
                .build();

        // Save the user to the database
        var savedUser = userRepository.save(user);

        // Generate a JWT for the new user
        var jwtToken = jwtService.generateToken(savedUser);

        // Return the token and user info
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .role(savedUser.getRole())
                .build();
    }

    /**
     * Authenticates an existing user.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // This will try to authenticate the user. If credentials are bad,
        // it throws an AuthenticationException.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // If we get here, the user is authenticated
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found after authentication"));

        // Generate a JWT
        var jwtToken = jwtService.generateToken(user);

        // Return the token and user info
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }
}