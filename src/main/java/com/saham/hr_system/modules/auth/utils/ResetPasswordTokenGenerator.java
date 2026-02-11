package com.saham.hr_system.modules.auth.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ResetPasswordTokenGenerator {

    public String generateToken(){
        return
                UUID.randomUUID().toString();
    }
}
