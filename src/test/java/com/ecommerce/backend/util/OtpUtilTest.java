package com.ecommerce.backend.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OtpUtilTest {

    @Test
    void shouldGenerateSixDigitOtp() {
        String otp = OtpUtil.generateOtp();
        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.chars().allMatch(Character::isDigit));
    }
}
