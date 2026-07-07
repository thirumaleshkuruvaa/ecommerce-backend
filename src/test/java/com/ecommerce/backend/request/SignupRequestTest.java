package com.ecommerce.backend.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SignupRequestTest {

    @Test
    void shouldSetAndGetSignupFields() {
        SignupRequest request = new SignupRequest();
        request.setEmail("user@example.com");
        request.setPassword("secret");
        request.setFullName("Jane Doe");
        request.setOtp("654321");
        request.setPhoneNumber("9876543210");

        assertEquals("user@example.com", request.getEmail());
        assertEquals("secret", request.getPassword());
        assertEquals("Jane Doe", request.getFullName());
        assertEquals("654321", request.getOtp());
        assertEquals("9876543210", request.getPhoneNumber());
    }
}
