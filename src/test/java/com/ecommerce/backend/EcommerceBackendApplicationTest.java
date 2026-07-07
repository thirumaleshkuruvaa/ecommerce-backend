package com.ecommerce.backend;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class EcommerceBackendApplicationTest {

    @Test
    void shouldExposeMainMethod() throws Exception {
        Method mainMethod = EcommerceBackendApplication.class.getMethod("main", String[].class);
        assertNotNull(mainMethod);
    }
}
