package com.mongoDB.Liquibase_app.service;

import jakarta.annotation.PostConstruct;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakAdminService {

    private Keycloak keycloak;

    @Value("${keycloak.admin.server-url}")
    private String serverUrl;

    @Value("${keycloak.admin.realm}")
    private String adminRealm;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.admin.client-id}")
    private String adminClientId;

    @Value("${keycloak.admin.app-realm}")
    private String appRealm;  // "SpringBoot"

    @PostConstruct
    public void init() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(adminRealm)
                .username(adminUsername)
                .password(adminPassword)
                .clientId(adminClientId)
                .build();
    }

    /**
     * Creates a user with ONLY email (username = email) if not exists,
     * adds UPDATE_PASSWORD required action,
     * and sends invitation email so user can set their own password.
     */
    public void inviteUserByEmail(String email) {
        // 1. Check if user already exists by email
        List<UserRepresentation> existing = keycloak.realm(appRealm).users().searchByEmail(email, true);
        String userId;

        if (!existing.isEmpty()) {
            // User exists → just send/set reset action again
            userId = existing.get(0).getId();
        } else {
            // Create new user with email only
            UserRepresentation user = new UserRepresentation();
            user.setEmail(email);
            user.setUsername(email);           // username = email (common pattern)
            user.setEnabled(true);
            user.setEmailVerified(false);      // can send VERIFY_EMAIL if needed later

            var response = keycloak.realm(appRealm).users().create(user);
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create user: " + response.getStatusInfo().getReasonPhrase());
            }

            userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        }

        // 2. Force user to update password on next login (sends set-password link)
        UserRepresentation userToUpdate = keycloak.realm(appRealm).users().get(userId).toRepresentation();
        userToUpdate.setRequiredActions(Collections.singletonList("UPDATE_PASSWORD"));
        keycloak.realm(appRealm).users().get(userId).update(userToUpdate);

        // 3. Send the actual email with the set-password link
        List<String> actions = Collections.singletonList("UPDATE_PASSWORD");
        keycloak.realm(appRealm).users().get(userId).executeActionsEmail(actions);

        // Note: You can customize lifespan, redirectUri, clientId if needed:
        // keycloak.realm(appRealm).users().get(userId).executeActionsEmail(86400, null, "your-client-id", actions);
    }

    // You can keep the old createUser method if needed for other flows,
    // but it's no longer used in the invite flow
}