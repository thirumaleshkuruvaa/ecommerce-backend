package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class OtpExceptionTest {

    @Test
    void shouldExposeProvidedMessage() {
        OtpException exception = new OtpException("otp issue");

        assertEquals("otp issue", exception.getMessage());
        assertThrows(OtpException.class, () -> {
            throw new OtpException("otp issue");
        });
    }
}
