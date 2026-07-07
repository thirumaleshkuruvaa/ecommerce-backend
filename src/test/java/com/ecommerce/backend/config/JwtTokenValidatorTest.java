package com.ecommerce.backend.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JwtTokenValidatorTest {

    @Test
    void shouldProcessRequestsWithoutThrowing() {
        JwtTokenValidator validator = new JwtTokenValidator();
        assertDoesNotThrow(() -> validator.doFilterInternal(
                new MockHttpServletRequest(),
                new MockHttpServletResponse(),
                new MockFilterChain()));
    }
}
