package com.ecommerce.backend.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DevProfileTest {

    @Test
    void shouldReturnDevMessage() {
        DevProfile profile = new DevProfile();
        assertEquals("Running in DEV environment 🚀", profile.message());
    }
}
