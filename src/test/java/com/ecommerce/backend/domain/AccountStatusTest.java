package com.ecommerce.backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AccountStatusTest {

    @Test
    void shouldExposeExpectedAccountStatuses() {
        assertEquals("ACTIVE", AccountStatus.ACTIVE.name());
        assertEquals("PENDING_VERIFICATION", AccountStatus.PENDING_VERIFICATION.name());
    }
}
