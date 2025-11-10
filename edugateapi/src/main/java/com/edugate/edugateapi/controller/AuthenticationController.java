package com.edugate.edugateapi.controller;

import com.edugate.edugateapi.dto.auth.AuthenticationRequest;
import com.edugate.edugateapi.dto.auth.AuthenticationResponse;
import com.edugate.edugateapi.dto.auth.RegisterRequest;
import com.edugate.edugateapi.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        // The service handles all the logic
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        // The service handles all the logic
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}