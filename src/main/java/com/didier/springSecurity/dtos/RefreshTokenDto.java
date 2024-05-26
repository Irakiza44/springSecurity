package com.didier.springSecurity.dtos;

public class RefreshTokenDto {
    private String expiredToken;

    // Getter and setter
    public String getExpiredToken() {
        return expiredToken;
    }

    public void setExpiredToken(String expiredToken) {
        this.expiredToken = expiredToken;
    }
}
