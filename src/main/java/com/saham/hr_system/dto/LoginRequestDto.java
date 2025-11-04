package com.saham.hr_system.dto;

import lombok.Data;

@Data
public class LoginRequestDto {

    private String email;
    private String password;
}
