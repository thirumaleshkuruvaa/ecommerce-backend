package com.ecommerce.backend.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

class ProductRepositoryTest {

    @Test
    void shouldBeARepositoryInterfaceAndSupportSpecifications() {
        assertTrue(ProductRepository.class.isInterface());
        assertTrue(JpaRepository.class.isAssignableFrom(ProductRepository.class));
        assertTrue(JpaSpecificationExecutor.class.isAssignableFrom(ProductRepository.class));
    }
}
