package com.locationguru.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.locationguru.authentication.request.KeyRequest;
import com.locationguru.authentication.request.OrganisationIdRequest;
import com.locationguru.authentication.service.AuthService;
import com.locationguru.authentication.response.AuthResponse;
import com.locationguru.authentication.model.AuthKey;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/validate")
    public ResponseEntity<AuthResponse> validateKey(@RequestBody KeyRequest keyRequest) {
        logger.debug("Received validate key request: {}", keyRequest);

        if (keyRequest == null || StringUtils.isBlank(keyRequest.getKey())) {
            logger.warn("Key cannot be empty");
            return ResponseEntity.ok().body(new AuthResponse(400, "Key cannot be empty"));
        }

        if (authService.validateKey(keyRequest.getKey())) {
            logger.info("Key validated successfully");
            return ResponseEntity.ok().body(new AuthResponse(200, ""));
        } else {
            logger.warn("Invalid key");
            return ResponseEntity.ok().body(new AuthResponse(401, "Invalid key"));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<AuthResponse> createKey(@RequestBody KeyRequest keyRequest) {
        logger.debug("Received create key request: {}", keyRequest);

        if (keyRequest == null || StringUtils.isBlank(keyRequest.getOrganisationName())) {
            logger.warn("Organisation name cannot be empty");
            return ResponseEntity.ok().body(new AuthResponse(400, "Organisation name cannot be empty"));
        }

        try {
            String generatedKey = authService.createKey(keyRequest.getOrganisationName());
            String message = "Key created successfully: " + generatedKey;
            logger.info(message);
            return ResponseEntity.ok().body(new AuthResponse(200, message));
        } catch (Exception e) {
            logger.error("Failed to create key", e);
            return ResponseEntity.ok().body(new AuthResponse(500, "Failed to create key: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<AuthResponse> deleteKeyByOrganisationId(@RequestBody OrganisationIdRequest organisationIdRequest) {
        logger.debug("Received delete key request: {}", organisationIdRequest);

        if (organisationIdRequest == null || StringUtils.isBlank(organisationIdRequest.getOrganisationId())) {
            logger.warn("Organisation ID cannot be empty");
            return ResponseEntity.ok().body(new AuthResponse(400, "Organisation ID cannot be empty"));
        }

        boolean deleted = authService.deleteKeyByOrganisationId(organisationIdRequest.getOrganisationId());
        if (deleted) {
            logger.info("Key deleted successfully");
            return ResponseEntity.ok().body(new AuthResponse(200, ""));
        } else {
            logger.warn("Key not found");
            return ResponseEntity.ok().body(new AuthResponse(404, "Key not found"));
        }
    }

    @GetMapping("/keys")
    public ResponseEntity<AuthResponse> getAllKeys() {
        logger.debug("Received request to get all keys");

        try {
            List<AuthKey> keys = authService.getAllKeys();
            logger.info("Keys retrieved successfully");
            return ResponseEntity.ok().body(new AuthResponse(200, "", keys));
        } catch (Exception e) {
            logger.error("Failed to retrieve keys", e);
            return ResponseEntity.ok().body(new AuthResponse(500, "Failed to retrieve keys: " + e.getMessage()));
        }
    }
}
