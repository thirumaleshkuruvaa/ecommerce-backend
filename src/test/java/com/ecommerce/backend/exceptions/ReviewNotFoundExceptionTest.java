package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ReviewNotFoundExceptionTest {

    @Test
    void shouldExposeProvidedMessage() {
        ReviewNotFoundException exception = new ReviewNotFoundException("review not found");

        assertEquals("review not found", exception.getMessage());
        assertThrows(ReviewNotFoundException.class, () -> {
            throw new ReviewNotFoundException("review not found");
        });
    }
}
