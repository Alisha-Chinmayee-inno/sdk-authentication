package com.locationguru.authentication.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.locationguru.authentication.model.AuthKey;
import com.locationguru.authentication.repository.AuthKeyRepository;

@Service
public class AuthService {

    @Autowired
    private AuthKeyRepository authKeyRepository;

    public boolean validateKey(String key) {
        AuthKey authKey = authKeyRepository.findByKey(key);
        if (authKey != null) {
            return true;
        } else {
            return false;
        }
    }


    public String createKey(String organisationName) {
        String generatedKey = generateRandomKey();
        
        // Create AuthKey instance
        AuthKey authKey = new AuthKey();
        authKey.setId(generateId()); 
        authKey.setKey(generatedKey);
        authKey.setOrganisationName(organisationName);
        authKey.setActive(true);  
        
        authKeyRepository.save(authKey);
        
        return generatedKey; 
    }
    
    protected String generateId() {
        return UUID.randomUUID().toString();
    }


    protected String generateRandomKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[10];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public boolean deleteKeyByOrganisationId(String organisationId) {
        if (authKeyRepository.existsById(organisationId)) {
            authKeyRepository.deleteById(organisationId);
            return true;
        } else {
            return false;
        }
    }


    public List<AuthKey> getAllKeys() {
        return authKeyRepository.findAll();
    }
}
