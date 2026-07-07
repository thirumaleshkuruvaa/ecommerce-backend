package com.ecommerce.backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class USER_ROLETest {

    @Test
    void shouldExposeExpectedRoles() {
        assertEquals("ROLE_ADMIN", USER_ROLE.ROLE_ADMIN.name());
        assertEquals("ROLE_CUSTOMER", USER_ROLE.ROLE_CUSTOMER.name());
        assertEquals("ROLE_SELLER", USER_ROLE.ROLE_SELLER.name());
    }
}
