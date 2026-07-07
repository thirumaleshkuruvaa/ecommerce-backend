package com.ecommerce.backend.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class SaveCardRequestTest {

    @Test
    void shouldSetAndGetFields() {
        SaveCardRequest request = new SaveCardRequest();
        request.setCardHolderName("Jane Doe");
        request.setCardNumber("4111111111111111");
        request.setExpiryMonth("12");
        request.setExpiryYear("2028");
        request.setCardType("VISA");

        assertEquals("Jane Doe", request.getCardHolderName());
        assertEquals("4111111111111111", request.getCardNumber());
        assertEquals("12", request.getExpiryMonth());
        assertEquals("2028", request.getExpiryYear());
        assertEquals("VISA", request.getCardType());
    }

    @Test
    void shouldDefaultToNullValues() {
        SaveCardRequest request = new SaveCardRequest();
        assertNull(request.getCardHolderName());
        assertNull(request.getCardNumber());
        assertNull(request.getExpiryMonth());
        assertNull(request.getExpiryYear());
        assertNull(request.getCardType());
    }
}
