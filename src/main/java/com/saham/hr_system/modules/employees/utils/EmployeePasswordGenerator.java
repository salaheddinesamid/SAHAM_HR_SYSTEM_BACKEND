package com.saham.hr_system.modules.employees.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
/** * Utility class for generating employee passwords based on their full name.
 * The generated password consists of the first three letters of the first name,
 * the first three letters of the last name, and a random 4-digit number.
 * The class also encodes the generated password using the provided PasswordEncoder.
 */
@Component
@RequiredArgsConstructor
public class EmployeePasswordGenerator {
    private final PasswordEncoder passwordEncoder;

    /**
     * Generates a password for an employee based on their full name.
     *
     * @param fullName The full name of the employee (e.g., "John Doe").
     * @return A map containing the raw generated password and its encoded version.
     */
    public Map<String, String> generatePassword(String fullName){
        String[] nameParts = fullName.split(" ");
        String firstNamePart = nameParts[0].length() >= 3 ? nameParts[0].substring(0, 3) : nameParts[0];
        String lastNamePart = nameParts.length > 1 && nameParts[1].length() >= 3 ? nameParts[1].substring(0, 3) : (nameParts.length > 1 ? nameParts[1] : "Emp");
        String randomDigits = String.valueOf((int)(Math.random() * 9000) + 1000); // Generate a random 4-digit number

        String rawPassword = firstNamePart + lastNamePart + randomDigits;
        return Map.of(
                "rawPassword", rawPassword,
                "encodedPassword", passwordEncoder.encode(rawPassword)
        );
    }
}
