package com.locationguru.authentication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import com.locationguru.authentication.model.AuthKey;
import com.locationguru.authentication.repository.AuthKeyRepository;

public class AuthServiceTest {

    @Mock
    private AuthKeyRepository authKeyRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateKey_ValidKey() {
        String key = "validKey";
        AuthKey authKey = new AuthKey();
        authKey.setKey(key);
        authKey.setOrganisationName("Organization1");
        authKey.setActive(true);

        when(authKeyRepository.findByKey(key)).thenReturn(authKey);

        boolean isValid = authService.validateKey(key);

        assertTrue(isValid);
        verify(authKeyRepository, times(1)).findByKey(key);
    }

    @Test
    void testValidateKey_InvalidKey() {
        String key = "invalidKey";
        when(authKeyRepository.findByKey(key)).thenReturn(null);

        boolean isValid = authService.validateKey(key);

        assertFalse(isValid);
        verify(authKeyRepository, times(1)).findByKey(key);
    }
    
    @Test
    void testCreateKey() {
        String organisationName = "MyOrganisation";
        when(authKeyRepository.save(any())).thenAnswer((Answer<AuthKey>) invocation -> {
            AuthKey authKey = invocation.getArgument(0);
            authKey.setId("generatedId");
            return authKey;
        });
        String generatedKey = authService.createKey(organisationName);

        assertNotNull(generatedKey);
        assertFalse(generatedKey.isEmpty());

        verify(authKeyRepository, times(1)).save(any());
    }

    @Test
    void testDeleteKeyByOrganisationId_KeyExists() {
        String organisationId = "1"; 
        AuthKey authKey = new AuthKey();
        authKey.setId(organisationId);

        when(authKeyRepository.existsById(organisationId)).thenReturn(true);

        boolean deleted = authService.deleteKeyByOrganisationId(organisationId);

        assertTrue(deleted);
        verify(authKeyRepository, times(1)).deleteById(organisationId);
    }

    @Test
    void testDeleteKeyByOrganisationId_KeyDoesNotExist() {
        String organisationId = "1"; 

        when(authKeyRepository.existsById(organisationId)).thenReturn(false);

        boolean deleted = authService.deleteKeyByOrganisationId(organisationId);

        assertFalse(deleted);

        verify(authKeyRepository, never()).deleteById(any());
    }


    @Test
    void testGetAllKeys() {
        List<AuthKey> keys = Arrays.asList(
            new AuthKey("key1", "Organisation1", true),
            new AuthKey("key2", "Organisation2", true)
        );

        when(authKeyRepository.findAll()).thenReturn(keys);

        List<AuthKey> result = authService.getAllKeys();

        assertEquals(keys, result);
        verify(authKeyRepository, times(1)).findAll();
    }
}
