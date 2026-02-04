package com.mongoDB.Liquibase_app.controllers;

import com.mongoDB.Liquibase_app.dto.LoginRequestDto;
import com.mongoDB.Liquibase_app.dto.SignupRequestDto;
import com.mongoDB.Liquibase_app.dto.TokenResponseDto;
import com.mongoDB.Liquibase_app.service.AuthService;
import com.mongoDB.Liquibase_app.service.KeycloakAdminService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final KeycloakAdminService keycloakAdminService;
    private final AuthService authService;

    /**
     * New endpoint: Invite user by email only
     * - Creates user if not exists
     * - Sends invitation email
     * - User sets password via link
     */
    @PostMapping("/invite")
    public ResponseEntity<String> inviteByEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return ResponseEntity.badRequest().body("Valid email is required");
        }

        try {
            keycloakAdminService.inviteUserByEmail(email.trim());
            return ResponseEntity.ok("Invitation sent successfully to " + email + ". Check your inbox to set password.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send invitation: " + e.getMessage());
        }
    }

    /**
     * Old /signup - can keep for backward compatibility or remove
     * Here we only use email (password & username ignored)
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto dto) {
        try {
            // Only email is used now
            keycloakAdminService.inviteUserByEmail(dto.getEmail());
            return ResponseEntity.ok("Invitation sent to " + dto.getEmail() + ". User can set their password via email link.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending invitation: " + e.getMessage());
        }
    }

    /**
     * Login remains the same (user logs in after setting password)
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        try {
            TokenResponseDto token = authService.login(dto);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}