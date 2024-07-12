package com.locationguru.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.locationguru.authentication.request.KeyRequest;
import com.locationguru.authentication.request.OrganisationIdRequest;
import com.locationguru.authentication.service.AuthService;
import com.locationguru.authentication.response.AuthResponse;
import com.locationguru.authentication.model.AuthKey;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/validate")
    public ResponseEntity<AuthResponse> validateKey(@RequestBody KeyRequest keyRequest) {
        if (keyRequest == null || keyRequest.getKey() == null || keyRequest.getKey().isEmpty()) {
            return ResponseEntity.ok().body(new AuthResponse(400, "Key cannot be empty"));
        }

        if (authService.validateKey(keyRequest.getKey())) {
            return ResponseEntity.ok().body(new AuthResponse(200, ""));
        } else {
            return ResponseEntity.ok().body(new AuthResponse(401, "Invalid key"));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<AuthResponse> createKey(@RequestBody KeyRequest keyRequest) {
        if (keyRequest == null || keyRequest.getOrganisationName() == null || keyRequest.getOrganisationName().isEmpty()) {
            return ResponseEntity.ok().body(new AuthResponse(400, "Organisation name cannot be empty"));
        }

        try {
            String generatedKey = authService.createKey(keyRequest.getOrganisationName());
            String message = "Key created successfully: " + generatedKey;
            return ResponseEntity.ok().body(new AuthResponse(200, message));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new AuthResponse(500, "Failed to create key: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<AuthResponse> deleteKeyByOrganisationId(@RequestBody OrganisationIdRequest organisationIdRequest) {
        if (organisationIdRequest == null || organisationIdRequest.getOrganisationId() == null || organisationIdRequest.getOrganisationId().isEmpty()) {
            return ResponseEntity.ok().body(new AuthResponse(400, "Organisation ID cannot be empty"));
        }

        boolean deleted = authService.deleteKeyByOrganisationId(organisationIdRequest.getOrganisationId());
        if (deleted) {
            return ResponseEntity.ok().body(new AuthResponse(200, ""));
        } else {
            return ResponseEntity.ok().body(new AuthResponse(404, "Key not found"));
        }
    }

    @GetMapping("/keys")
    public ResponseEntity<AuthResponse> getAllKeys() {
        try {
            List<AuthKey> keys = authService.getAllKeys();
            return ResponseEntity.ok().body(new AuthResponse(200, "", keys));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new AuthResponse(500, "Failed to retrieve keys: " + e.getMessage()));
        }
    }
}
