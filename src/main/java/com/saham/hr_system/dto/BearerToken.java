package com.saham.hr_system.dto;

import lombok.Data;

@Data
public class BearerToken {

    private String accessToken;
    private String refreshToken;

    public BearerToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
