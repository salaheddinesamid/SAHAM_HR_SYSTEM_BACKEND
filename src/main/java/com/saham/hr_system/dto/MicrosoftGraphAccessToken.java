package com.saham.hr_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MicrosoftGraphAccessToken {

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("ext_expires_in")
    private int extExpiresIn;

    @JsonProperty("access_token")
    private String accessToken;
}
