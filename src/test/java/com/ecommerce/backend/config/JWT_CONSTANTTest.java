package com.ecommerce.backend.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class JWT_CONSTANTTest {

    @Test
    void shouldExposeExpectedSecurityConstants() {
        assertEquals("Authorization", JWT_CONSTANT.JWT_HEADER);
        assertFalse(JWT_CONSTANT.SECRET_KEY.isBlank());
    }
}
