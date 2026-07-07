package com.ecommerce.backend.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RepositoryTest {

    @Test
    void shouldBeAnAnnotationType() {
        assertTrue(Repository.class.isAnnotation());
    }
}
