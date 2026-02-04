package com.mongoDB.Liquibase_app.service;

import com.mongoDB.Liquibase_app.dto.LoginRequestDto;
import com.mongoDB.Liquibase_app.dto.TokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Value("${keycloak.admin.server-url}")
    private String serverUrl;

    @Value("${keycloak.admin.app-realm}")
    private String appRealm;

    @Value("${keycloak.client.id}")
    private String clientId;

    @Value("${keycloak.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public TokenResponseDto login(LoginRequestDto dto) {
        String tokenUrl = serverUrl + "/realms/" + appRealm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", dto.getUsername());
        body.add("password", dto.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // Call token endpoint
        TokenResponseDto response = restTemplate.postForObject(tokenUrl, request, TokenResponseDto.class);
        return response;
    }
}