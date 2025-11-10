package com.edugate.edugateapi.config;

import com.edugate.edugateapi.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor // Automatically injects final fields
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Check if the Authorization header is present and valid
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract the token
        jwt = authHeader.substring(7); // "Bearer " is 7 chars

        // 3. Extract the user email from the token
        userEmail = jwtService.extractUsername(jwt);

        // 4. If email exists and user is not already authenticated
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Get user details from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Validate the token against the user details
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Create an authentication token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // We don't have credentials
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                
                // 6. Set the user in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 7. Pass the request to the next filter
        filterChain.doFilter(request, response);
    }
}