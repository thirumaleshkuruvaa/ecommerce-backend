package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class HomeCategoryExceptionTest {

    @Test
    void shouldExposeProvidedMessage() {
        HomeCategoryException exception = new HomeCategoryException("home category issue");

        assertEquals("home category issue", exception.getMessage());
        assertThrows(HomeCategoryException.class, () -> {
            throw new HomeCategoryException("home category issue");
        });
    }
}
