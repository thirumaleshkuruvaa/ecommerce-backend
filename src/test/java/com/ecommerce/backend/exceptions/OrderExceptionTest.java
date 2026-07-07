package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class OrderExceptionTest {

    @Test
    void shouldExposeProvidedMessage() {
        OrderException exception = new OrderException("order issue");

        assertEquals("order issue", exception.getMessage());
        assertThrows(OrderException.class, () -> {
            throw new OrderException("order issue");
        });
    }
}
