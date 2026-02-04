package com.mongoDB.Liquibase_app.dto;

import lombok.Data;

@Data
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private String idToken;
    private long expiresIn;
}