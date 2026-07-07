package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CouponExceptionTest {

    @Test
    void shouldExposeProvidedMessage() {
        CouponException exception = new CouponException("coupon issue");

        assertEquals("coupon issue", exception.getMessage());
        assertThrows(CouponException.class, () -> {
            throw new CouponException("coupon issue");
        });
    }
}
