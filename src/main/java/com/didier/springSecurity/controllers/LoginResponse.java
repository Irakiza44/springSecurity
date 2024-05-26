package com.didier.springSecurity.controllers;

import java.util.List;

public class LoginResponse {
    private String token;
    private long expiresIn;
    private List<String> role;

    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public List<String> getRole() {
        return role;
    }

    public LoginResponse setRole(List<String> role) {
        this.role = role;
        return this;
    }

}
