package com.didier.springSecurity.controllers;

import java.util.List;

public class RefreshTokenResponse {
    private String token;
    private long expiresIn;
    private List<String> role;

    public RefreshTokenResponse(String token, long expiresIn, List<String> role) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.role = role;
    }

    // Getters and setters...
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }
}
