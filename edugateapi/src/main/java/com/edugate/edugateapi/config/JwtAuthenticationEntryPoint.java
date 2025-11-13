package com.edugate.edugateapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.edugate.edugateapi.dto.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

/**
 * Custom authentication entry point that returns 401 with ApiResponse format
 * when a user tries to access a protected endpoint without a valid JWT token.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        try {
            // Prevent any other filter from writing to the response
            response.reset();
            
            // Return 401 Unauthorized with ApiResponse format
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ApiResponse<Object> apiResponse = ApiResponse.builder()
                    .success(false)
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .timestamp(Instant.now())
                    .path(request.getRequestURI())
                    .message("Unauthorized - Valid JWT required")
                    .data(null)
                    .build();

            String jsonResponse = objectMapper.writeValueAsString(apiResponse);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
        } catch (IOException e) {
            // Silently fail if response is already committed
            throw new ServletException("Unable to send JSON message", e);
        }
    }
}
