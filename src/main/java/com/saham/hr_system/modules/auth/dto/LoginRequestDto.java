package com.saham.hr_system.modules.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotNull
    private String email;

    @NotNull
    private String password;
}
