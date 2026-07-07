package com.ecommerce.backend.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

class CouponRepositoryTest {

    @Test
    void shouldBeARepositoryInterfaceAndJpaRepository() {
        assertTrue(CouponRepository.class.isInterface());
        assertTrue(JpaRepository.class.isAssignableFrom(CouponRepository.class));
    }
}
