package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ErrorDetailsTest {

    @Test
    void shouldSetAndGetErrorDetails() {
        ErrorDetails details = new ErrorDetails();
        details.setError("error");
        details.setDetails("details");
        details.setTimestamp(LocalDateTime.of(2024, 1, 1, 12, 0));

        assertEquals("error", details.getError());
        assertEquals("details", details.getDetails());
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 0), details.getTimestamp());
    }

    @Test
    void shouldDefaultToNullValues() {
        ErrorDetails details = new ErrorDetails();
        assertNull(details.getError());
        assertNull(details.getDetails());
        assertNull(details.getTimestamp());
    }
}
