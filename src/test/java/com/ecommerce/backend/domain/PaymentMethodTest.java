package com.ecommerce.backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PaymentMethodTest {

    @Test
    void shouldExposeExpectedPaymentMethods() {
        assertEquals("RAZORPAY", PaymentMethod.RAZORPAY.name());
    }
}
