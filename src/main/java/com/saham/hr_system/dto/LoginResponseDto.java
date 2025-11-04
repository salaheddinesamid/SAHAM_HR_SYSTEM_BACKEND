package com.saham.hr_system.dto;

import lombok.Data;

@Data
public class LoginResponseDto {

    private BearerToken bearerToken;
    private EmployeeDetailsDto userDetails;
    private String microsoftGraphAccessToken;

    public LoginResponseDto(
            BearerToken bearerToken,
            EmployeeDetailsDto userDetails,
            String microsoftGraphAccessToken
    ) {
        this.bearerToken = bearerToken;
        this.userDetails = userDetails;
        this.microsoftGraphAccessToken = "";
    }
}