package com.mongoDB.Liquibase_app.advices;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;


@Data
@Builder
public class ApiError {

    private HttpStatus status;
    private String message;
    private List<ErrorDetail> subErrors;  // Changed to custom ErrorDetail for field info

    // Nested class for field-level details
    @Data
    @Builder
    public static class ErrorDetail {
        private String field;
        private String message;
    }

}
