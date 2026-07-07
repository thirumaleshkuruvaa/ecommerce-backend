package com.ecommerce.backend.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

class AppConfigTest {

    @Test
    void shouldCreatePasswordEncoderAndCorsConfiguration() {
        AppConfig config = new AppConfig();
        PasswordEncoder encoder = config.passwordEncoder();
        CorsConfigurationSource source = config.corsConfigurationSource();

        assertNotNull(encoder);
        assertNotNull(source);
        assertTrue(encoder.matches("secret", encoder.encode("secret")));
    }
}
