package com.ecommerce.backend.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class JwtProviderTest {

    @Test
    void shouldGenerateAndReadJwtToken() {
        JwtProvider provider = new JwtProvider();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "user@example.com",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));

        String token = provider.generateToken(auth);

        assertNotNull(token);
        assertEquals("user@example.com", provider.getEmailFromJwtToken(token));
    }
}
