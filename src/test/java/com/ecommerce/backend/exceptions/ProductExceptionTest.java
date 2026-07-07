package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ProductExceptionTest {

    @Test
    void shouldExposeProvidedMessage() {
        ProductException exception = new ProductException("product issue");

        assertEquals("product issue", exception.getMessage());
        assertThrows(ProductException.class, () -> {
            throw new ProductException("product issue");
        });
    }
}
