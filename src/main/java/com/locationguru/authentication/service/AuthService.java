package com.locationguru.authentication.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.locationguru.authentication.model.AuthKey;
import com.locationguru.authentication.repository.AuthKeyRepository;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthKeyRepository authKeyRepository;

    public boolean validateKey(String key) {
        logger.debug("Validating key: {}", key);
        AuthKey authKey = authKeyRepository.findByKey(key);
        boolean isValid = authKey != null;
        logger.info("Key validation result: {}", isValid);
        return isValid;
    }

    public String createKey(String organisationName) {
        logger.debug("Creating key for organisation: {}", organisationName);
        AuthKey existingKey = authKeyRepository.findByOrganisationName(organisationName);
        if (existingKey != null) {
            String errorMessage = "Key for this organisation already exists, please delete it to create a new key.";
            logger.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        String generatedKey = generateRandomKey();
        AuthKey authKey = new AuthKey();
        authKey.setId(generateId());
        authKey.setKey(generatedKey);
        authKey.setOrganisationName(organisationName);
        authKey.setActive(true);

        authKeyRepository.save(authKey);

        logger.info("Key created successfully: {}", generatedKey);
        return generatedKey;
    }

    protected String generateId() {
        String id = UUID.randomUUID().toString();
        logger.debug("Generated ID: {}", id);
        return id;
    }

    protected String generateRandomKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[10];
        secureRandom.nextBytes(randomBytes);
        String key = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        logger.debug("Generated random key: {}", key);
        return key;
    }

    public boolean deleteKeyByOrganisationId(String organisationId) {
        logger.debug("Deleting key for organisation ID: {}", organisationId);
        if (authKeyRepository.existsById(organisationId)) {
            authKeyRepository.deleteById(organisationId);
            logger.info("Key deleted successfully");
            return true;
        } else {
            logger.warn("Key not found for organisation ID: {}", organisationId);
            return false;
        }
    }

    public List<AuthKey> getAllKeys() {
        logger.debug("Retrieving all keys");
        List<AuthKey> keys = authKeyRepository.findAll();
        logger.info("Keys retrieved successfully");
        return keys;
    }
}
