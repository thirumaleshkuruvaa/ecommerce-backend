package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SellerExceptionTest {

    @Test
    void shouldExposeProvidedMessage() {
        SellerException exception = new SellerException("seller issue");

        assertEquals("seller issue", exception.getMessage());
        assertThrows(SellerException.class, () -> {
            throw new SellerException("seller issue");
        });
    }
}
