package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class AddressNotFoundExceptionTest {

    @Test
    void shouldWrapAndExposeMessage() {
        AddressNotFoundException exception = new AddressNotFoundException("address not found");
        assertNotNull(exception);
        assertEquals("address not found", exception.getMessage());
    }
}
