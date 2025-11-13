package com.edugate.edugateapi.controller;

import com.edugate.edugateapi.dto.auth.AuthenticationRequest;
import com.edugate.edugateapi.dto.auth.AuthenticationResponse;
import com.edugate.edugateapi.dto.auth.RegisterRequest;
import com.edugate.edugateapi.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and login endpoints")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns authentication token and user details")
    @ApiResponse(
        responseCode = "200",
        description = "User registered and authenticated",
        content = @Content(
            schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class),
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/auth/register\",\"message\":\"Registered and authenticated\",\"data\":{\"token\":\"<jwt_token>\",\"email\":\"user@example.com\",\"fullName\":\"John Doe\",\"role\":\"ROLE_USER\",\"id\":1}}")
        )
    )
    @ApiResponse(responseCode = "400", description = "Bad request - validation failed", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":400,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/auth/register\",\"message\":\"Bad request - validation failed\",\"data\":null}")))
    @ApiResponse(responseCode = "409", description = "Conflict - Email already exists", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":409,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/auth/register\",\"message\":\"Conflict - Email already exists\",\"data\":null}")))
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        // The service handles all the logic
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates a user and returns a JWT and user details")
    @ApiResponse(
        responseCode = "200",
        description = "Authenticated successfully",
        content = @Content(
            schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class),
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":true,\"status\":200,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/auth/login\",\"message\":\"Authenticated successfully\",\"data\":{\"token\":\"<jwt_token>\",\"email\":\"user@example.com\",\"fullName\":\"John Doe\",\"role\":\"ROLE_USER\",\"id\":1}}")
        )
    )
    @ApiResponse(responseCode = "400", description = "Bad request - validation failed", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":400,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/auth/login\",\"message\":\"Bad request - validation failed\",\"data\":null}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials", content = @Content(schema = @Schema(implementation = com.edugate.edugateapi.dto.ApiResponse.class), examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\"success\":false,\"status\":401,\"timestamp\":\"2025-11-12T16:00:00Z\",\"path\":\"/api/auth/login\",\"message\":\"Unauthorized - Invalid credentials\",\"data\":null}")))
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        // The service handles all the logic
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}