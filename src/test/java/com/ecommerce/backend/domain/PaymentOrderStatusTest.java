package com.ecommerce.backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PaymentOrderStatusTest {

    @Test
    void shouldExposeExpectedStatuses() {
        assertEquals("PENDING", PaymentOrderStatus.PENDING.name());
        assertEquals("SUCCESS", PaymentOrderStatus.SUCCESS.name());
        assertEquals("FAILED", PaymentOrderStatus.FAILED.name());
    }
}
