package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class RazorePayExceptionTest {

    @Test
    void shouldExposeProvidedMessage() {
        RazorePayException exception = new RazorePayException("razorpay issue");

        assertEquals("razorpay issue", exception.getMessage());
        assertThrows(RazorePayException.class, () -> {
            throw new RazorePayException("razorpay issue");
        });
    }
}
