package com.ecommerce.backend.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LoginRequestTest {

    @Test
    void shouldSetAndGetEmailAndOtp() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setOtp("123456");

        assertEquals("user@example.com", request.getEmail());
        assertEquals("123456", request.getOtp());
    }
}
