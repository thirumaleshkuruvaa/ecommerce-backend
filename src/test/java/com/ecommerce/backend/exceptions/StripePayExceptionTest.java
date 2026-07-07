package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class StripePayExceptionTest {

    @Test
    void shouldExposeProvidedMessage() {
        StripePayException exception = new StripePayException("stripe issue");

        assertEquals("stripe issue", exception.getMessage());
        assertThrows(StripePayException.class, () -> {
            throw new StripePayException("stripe issue");
        });
    }
}
