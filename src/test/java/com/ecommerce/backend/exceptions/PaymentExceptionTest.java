package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PaymentExceptionTest {

    @Test
    void shouldExposeProvidedMessage() {
        PaymentException exception = new PaymentException("payment issue");

        assertEquals("payment issue", exception.getMessage());
        assertThrows(PaymentException.class, () -> {
            throw new PaymentException("payment issue");
        });
    }
}
