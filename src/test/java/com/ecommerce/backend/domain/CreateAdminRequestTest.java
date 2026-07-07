package com.ecommerce.backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CreateAdminRequestTest {

    @Test
    void shouldSetAndGetAdminCredentials() {
        CreateAdminRequest request = new CreateAdminRequest();
        request.setEmail("admin@example.com");
        request.setPassword("secret");

        assertEquals("admin@example.com", request.getEmail());
        assertEquals("secret", request.getPassword());
    }
}
