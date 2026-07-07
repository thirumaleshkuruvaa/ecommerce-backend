package com.ecommerce.backend.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TestProfileTest {

    @Test
    void shouldReturnTestMessage() {
        TestProfile profile = new TestProfile();
        assertEquals("Running in TEST environment 🚀", profile.message());
    }
}
