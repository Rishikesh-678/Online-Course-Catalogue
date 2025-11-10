package com.edugate.edugateapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity // Allows us to use @PreAuthorize for role-based access
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (Cross-Site Request Forgery) since we are using stateless JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Define endpoint permissions
                .authorizeHttpRequests(auth -> auth
                        // Whitelist our public authentication endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        
                        // Whitelist file serving endpoints
                        .requestMatchers("/api/images/**").permitAll()
                        
                        // Swagger UI endpoints
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/v3/api-docs.yaml").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        
                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )

                // Configure session management to be stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Set our custom authentication provider
                .authenticationProvider(authenticationProvider)

                // Add our JWT filter *before* the standard username/password filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}