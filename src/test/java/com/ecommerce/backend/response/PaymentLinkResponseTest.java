package com.ecommerce.backend.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PaymentLinkResponseTest {

    @Test
    void shouldSetAndGetPaymentLinkFields() {
        PaymentLinkResponse response = new PaymentLinkResponse();
        response.setPaymentLinkId("pay_123");
        response.setPaymentLinkUrl("https://example.com/pay");

        assertEquals("pay_123", response.getPaymentLinkId());
        assertEquals("https://example.com/pay", response.getPaymentLinkUrl());
    }
}
