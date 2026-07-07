package com.ecommerce.backend.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class SellerLoginOtpRequestTest {

    @Test
    void shouldSetAndGetEmail() {
        SellerLoginOtpRequest request = new SellerLoginOtpRequest();
        request.setEmail("seller@example.com");

        assertEquals("seller@example.com", request.getEmail());
    }

    @Test
    void shouldDefaultToNullEmail() {
        SellerLoginOtpRequest request = new SellerLoginOtpRequest();
        assertNull(request.getEmail());
    }
}
