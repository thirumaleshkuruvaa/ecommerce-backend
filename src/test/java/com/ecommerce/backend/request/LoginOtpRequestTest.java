package com.ecommerce.backend.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.ecommerce.backend.domain.USER_ROLE;

class LoginOtpRequestTest {

    @Test
    void shouldSetAndGetFields() {
        LoginOtpRequest request = new LoginOtpRequest();
        request.setEmail("user@example.com");
        request.setOtp("123456");
        request.setRole(USER_ROLE.ROLE_CUSTOMER);

        assertEquals("user@example.com", request.getEmail());
        assertEquals("123456", request.getOtp());
        assertEquals(USER_ROLE.ROLE_CUSTOMER, request.getRole());
    }

    @Test
    void shouldDefaultToNullValues() {
        LoginOtpRequest request = new LoginOtpRequest();
        assertNull(request.getEmail());
        assertNull(request.getOtp());
        assertNull(request.getRole());
    }
}
