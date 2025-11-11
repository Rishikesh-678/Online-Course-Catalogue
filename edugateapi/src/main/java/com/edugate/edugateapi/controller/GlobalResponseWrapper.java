// In: src/main/java/com/edugate/edugateapi/controller/GlobalResponseWrapper.java
package com.edugate.edugateapi.controller;

import com.edugate.edugateapi.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@ControllerAdvice(basePackages = "com.edugate.edugateapi.controller")
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // We want to wrap all responses, so return true
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // Get HTTP status
        ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) response;
        int status = servletResponse.getServletResponse().getStatus();
        HttpStatus httpStatus = HttpStatus.valueOf(status);

        // Get request path
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
        String path = httpServletRequest.getRequestURI();

        // --- IMPORTANT ---
        // If the body is already an ApiResponse (from our GlobalExceptionHandler), don't wrap it again
        if (body instanceof ApiResponse) {
            return body;
        }

        // Don't wrap file/image responses
        if (body instanceof org.springframework.core.io.Resource) {
            return body;
        }
        
        // Don't wrap void (empty) responses from ResponseEntity<Void>
        if (body == null && returnType.getParameterType().equals(ResponseEntity.class)) {
            // But we still want to return our standard envelope
            return ApiResponse.success(null, "Operation successful", path, httpStatus);
        }

        // Wrap the successful response
        return ApiResponse.success(body, "Request successful", path, httpStatus);
    }
}