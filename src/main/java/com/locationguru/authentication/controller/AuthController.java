package com.locationguru.authentication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.locationguru.authentication.model.AuthKey;
import com.locationguru.authentication.request.KeyRequest;
import com.locationguru.authentication.request.OrganisationIdRequest;
import com.locationguru.authentication.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/validate")
    public ResponseEntity<?> validateKey(@RequestBody KeyRequest keyRequest) {
        if (authService.validateKey(keyRequest.getKey())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid key");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createKey(@RequestBody KeyRequest keyRequest) {
        try {
            String generatedKey = authService.createKey(keyRequest.getOrganisationName());
            return ResponseEntity.ok().body(generatedKey);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create key: " + e.getMessage());
        }
    }
    

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteKeyByOrganisationId(@RequestBody OrganisationIdRequest organisationIdRequest) {
        boolean deleted = authService.deleteKeyByOrganisationId(organisationIdRequest.getOrganisationId());
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Key not found");
        }
    }
  
    @GetMapping("/keys")
    public ResponseEntity<?> getAllKeys() {
        try {
            List<AuthKey> keys = authService.getAllKeys();
            return ResponseEntity.ok().body(keys);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve keys: " + e.getMessage());
        }
    }
}