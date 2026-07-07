package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CartItemExceptionTest {

    @Test
    void shouldExposeProvidedMessage() {
        CartItemException exception = new CartItemException("cart item issue");

        assertEquals("cart item issue", exception.getMessage());
        assertThrows(CartItemException.class, () -> {
            throw new CartItemException("cart item issue");
        });
    }
}
