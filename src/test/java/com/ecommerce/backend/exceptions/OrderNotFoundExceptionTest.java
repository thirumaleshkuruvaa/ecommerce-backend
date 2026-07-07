package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class OrderNotFoundExceptionTest {

    @Test
    void shouldExposeProvidedMessage() {
        OrderNotFoundException exception = new OrderNotFoundException("order not found");

        assertEquals("order not found", exception.getMessage());
        assertThrows(OrderNotFoundException.class, () -> {
            throw new OrderNotFoundException("order not found");
        });
    }
}
