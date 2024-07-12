package com.locationguru.authentication.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.locationguru.authentication.model.AuthKey;
import com.locationguru.authentication.request.KeyRequest;
import com.locationguru.authentication.request.OrganisationIdRequest;
import com.locationguru.authentication.service.AuthService;

public class AuthControllerTest {
	
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
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
    public void testValidateKey_InvalidKey() throws Exception {
        // Mocking the service behavior
        KeyRequest request = new KeyRequest("invalid_key", null);
        when(authService.validateKey(anyString())).thenReturn(false);

        // Performing the request
        mockMvc.perform(post("/api/auth/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("401"))
                .andExpect(jsonPath("$.message").value("Invalid key"));
    }

  

    @Test
    public void testCreateKey_Success() throws Exception {
        // Mocking the service behavior
        KeyRequest request = new KeyRequest(null, "TestOrg");
        String generatedKey = "generated_key";
        when(authService.createKey(anyString())).thenReturn(generatedKey);

        // Performing the request
        mockMvc.perform(post("/api/auth/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value(generatedKey));
    }


    @Test
    public void testCreateKey_Failure() throws Exception {
        // Mocking the service behavior
        KeyRequest request = new KeyRequest(null, "TestOrg");
        String errorMessage = "Key creation failed";
        when(authService.createKey(anyString())).thenThrow(new IllegalStateException(errorMessage));

        // Performing the request
        mockMvc.perform(post("/api/auth/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message").value(errorMessage));
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
    public void testDeleteKeyByOrganisationId_KeyNotFound() throws Exception {
        // Mocking the service behavior
        String organisationId = "123";
        when(authService.deleteKeyByOrganisationId(eq(organisationId))).thenReturn(false);

        // Performing the request
        mockMvc.perform(delete("/api/auth/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new OrganisationIdRequest(organisationId))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.message").value("Id does not exist"));
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
    public void testGetAllKeys_Failure() throws Exception {
        // Mocking the service behavior
        String errorMessage = "Failed to retrieve keys";
        when(authService.getAllKeys()).thenThrow(new RuntimeException(errorMessage));

        // Performing the request
        mockMvc.perform(get("/api/auth/keys")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("500"))
                .andExpect(jsonPath("$.message").value(containsString(errorMessage))); // Adjusted assertion
    }
    
    
    
    // Utility method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}