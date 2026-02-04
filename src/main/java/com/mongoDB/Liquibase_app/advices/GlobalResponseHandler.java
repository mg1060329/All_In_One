package com.mongoDB.Liquibase_app.advices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Global response wrapper: sab responses ko ApiResponse mein wrap karta hai
 * String return type ke special handling ke saath (common cast issue fix)
 */
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Sab responses pe apply karo (except jab pehle se ApiResponse hai)
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        // 1. Agar body pehle se ApiResponse hai → direct return (double wrap avoid)
        if (body instanceof ApiResponse) {
            return body;
        }

        // 2. Agar body null hai → empty ApiResponse
        if (body == null) {
            return new ApiResponse<>(null);
        }

        // 3. Special handling for String return types (sabse common problem yahin hota hai)
        if (body instanceof String) {
            // String ko ApiResponse mein wrap karo
            ApiResponse<String> wrapped = new ApiResponse<>((String) body);

            // Force content type JSON (Postman/Swagger ke liye safe)
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            try {
                // ObjectMapper se JSON string banao → String converter sahi se handle karega
                return objectMapper.writeValueAsString(wrapped);
            } catch (JsonProcessingException e) {
                // Rare case: fallback to original string (production mein log kar sakte ho)
                return body;
            }
        }

        // 4. Normal case: objects, lists, DTOs etc. → ApiResponse mein wrap
        return new ApiResponse<>(body);
    }
}