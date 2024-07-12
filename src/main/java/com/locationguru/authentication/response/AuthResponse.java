package com.locationguru.authentication.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.locationguru.authentication.model.AuthKey;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthResponse {
    private int status;
    private String message;
    private List<AuthKey> keys;

    // Existing constructors
    public AuthResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // New constructor to handle keys list
    public AuthResponse(int status, String message, List<AuthKey> keys) {
        this.status = status;
        this.message = message;
        this.keys = keys;
    }

    // Getters and setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AuthKey> getKeys() {
        return keys;
    }

    public void setKeys(List<AuthKey> keys) {
        this.keys = keys;
    }
}
