package com.ecommerce.backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PaymentStatusTest {

    @Test
    void shouldExposeExpectedStatuses() {
        assertEquals("PENDING", PaymentStatus.PENDING.name());
        assertEquals("SUCCESS", PaymentStatus.SUCCESS.name());
        assertEquals("FAILED", PaymentStatus.FAILED.name());
    }
}
