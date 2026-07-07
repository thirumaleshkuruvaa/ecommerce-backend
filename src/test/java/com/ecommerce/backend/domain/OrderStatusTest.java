package com.ecommerce.backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    void shouldExposeExpectedStatuses() {
        assertEquals("PENDING", OrderStatus.PENDING.name());
        assertEquals("PLACED", OrderStatus.PLACED.name());
        assertEquals("DELIVERED", OrderStatus.DELIVERED.name());
    }
}
