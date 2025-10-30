package com.saham.hr_system.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PasswordGenerator {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordGenerator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String generatePassword(String firstName, String lastName){

        // Get the current year:
        int year = LocalDate.now().getYear();
        // Generate Raw password
        String rawPassword = String.format("%s@%s%d", firstName.toLowerCase(), lastName.toLowerCase(), year);
        return passwordEncoder.encode(rawPassword);
    }
}
