package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DealExceptionTest {

    @Test
    void shouldExposeProvidedMessage() {
        DealException exception = new DealException("deal issue");

        assertEquals("deal issue", exception.getMessage());
        assertThrows(DealException.class, () -> {
            throw new DealException("deal issue");
        });
    }
}
