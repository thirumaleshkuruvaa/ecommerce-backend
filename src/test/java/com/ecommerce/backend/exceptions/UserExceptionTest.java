package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class UserExceptionTest {

    @Test
    void shouldWrapAndExposeMessage() {
        UserException exception = new UserException("user issue");
        assertNotNull(exception);
        assertEquals("user issue", exception.getMessage());
    }
}
