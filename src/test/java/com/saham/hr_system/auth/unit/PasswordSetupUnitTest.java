package com.saham.hr_system.modules.auth.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordSetupUtilsTest {

    private PasswordSetupUtils utils;

    @BeforeEach
    void setUp() {
        utils = new PasswordSetupUtils();

        // Inject value for @Value field
        ReflectionTestUtils.setField(
                utils,
                "URL_PREFIX",
                "http://localhost:3000"
        );
    }

    @Test
    void shouldGeneratePasswordSetupLink() {
        String token = UUID.randomUUID().toString();

        String link = utils.generatePasswordSetupLink(token);

        assertThat(link)
                .isEqualTo("http://localhost:3000/password-setup?token="+token);
    }
}