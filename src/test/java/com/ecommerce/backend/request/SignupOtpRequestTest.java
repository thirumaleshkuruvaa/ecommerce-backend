package com.ecommerce.backend.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class SignupOtpRequestTest {

    @Test
    void shouldSetAndGetEmail() {
        SignupOtpRequest request = new SignupOtpRequest();
        request.setEmail("signup@example.com");

        assertEquals("signup@example.com", request.getEmail());
    }

    @Test
    void shouldDefaultToNullEmail() {
        SignupOtpRequest request = new SignupOtpRequest();
        assertNull(request.getEmail());
    }
}
