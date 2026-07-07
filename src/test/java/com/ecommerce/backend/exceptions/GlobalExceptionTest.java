package com.ecommerce.backend.exceptions;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class GlobalExceptionTest {

    @Test
    void shouldInstantiateAdviceClass() {
        GlobalException exception = new GlobalException();
        assertNotNull(exception);
    }
}
