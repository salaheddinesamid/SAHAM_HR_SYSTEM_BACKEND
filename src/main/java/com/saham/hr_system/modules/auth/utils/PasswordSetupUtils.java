package com.saham.hr_system.modules.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordSetupUtils {
    @Value("${frontend.url}")
    private String URL_PREFIX;

    public String generatePasswordSetupLink(String token) {
        return URL_PREFIX + "/password-setup?token=" + token;
    }
}
