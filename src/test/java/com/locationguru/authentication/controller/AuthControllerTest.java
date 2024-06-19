package com.locationguru.authentication.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.locationguru.authentication.model.AuthKey;
import com.locationguru.authentication.request.KeyRequest;
import com.locationguru.authentication.request.OrganisationIdRequest;
import com.locationguru.authentication.service.AuthService;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateKey_ValidKey() {
        KeyRequest keyRequest = new KeyRequest();
        keyRequest.setKey("validKey");

        when(authService.validateKey("validKey")).thenReturn(true);

        ResponseEntity<?> response = authController.validateKey(keyRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testValidateKey_InvalidKey() {
        KeyRequest keyRequest = new KeyRequest();
        keyRequest.setKey("invalidKey");

        when(authService.validateKey("invalidKey")).thenReturn(false);

        ResponseEntity<?> response = authController.validateKey(keyRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid key", response.getBody());
    }

    @Test
    void testCreateKey_Success() {
        KeyRequest keyRequest = new KeyRequest();
        keyRequest.setOrganisationName("MyOrganisation");

        when(authService.createKey("MyOrganisation")).thenReturn("generatedKey");

        ResponseEntity<?> response = authController.createKey(keyRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("generatedKey", response.getBody());
    }

    @Test
    void testCreateKey_Failure() {
        KeyRequest keyRequest = new KeyRequest();
        keyRequest.setOrganisationName("MyOrganisation");

        when(authService.createKey("MyOrganisation")).thenThrow(new RuntimeException("Database connection error"));

        ResponseEntity<?> response = authController.createKey(keyRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to create key: Database connection error", response.getBody());
    }

    @Test
    void testDeleteKeyByOrganisationId_KeyDeletedSuccessfully() {
        OrganisationIdRequest organisationIdRequest = new OrganisationIdRequest();
        organisationIdRequest.setOrganisationId("orgId1");

        when(authService.deleteKeyByOrganisationId("orgId1")).thenReturn(true);

        ResponseEntity<?> response = authController.deleteKeyByOrganisationId(organisationIdRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteKeyByOrganisationId_KeyNotFound() {
        OrganisationIdRequest organisationIdRequest = new OrganisationIdRequest();
        organisationIdRequest.setOrganisationId("orgId1");

        when(authService.deleteKeyByOrganisationId("orgId1")).thenReturn(false);

        ResponseEntity<?> response = authController.deleteKeyByOrganisationId(organisationIdRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Key not found", response.getBody());
    }

    @Test
    void testGetAllKeys_Success() {
        List<AuthKey> keys = Arrays.asList(
            new AuthKey("key1", "Organisation1", true),
            new AuthKey("key2", "Organisation2", true)
        );

        when(authService.getAllKeys()).thenReturn(keys);

        ResponseEntity<?> response = authController.getAllKeys();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(keys, response.getBody());
    }

    @Test
    void testGetAllKeys_Failure() {
        when(authService.getAllKeys()).thenThrow(new RuntimeException("Database connection error"));

        ResponseEntity<?> response = authController.getAllKeys();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to retrieve keys: Database connection error", response.getBody());
    }
}
