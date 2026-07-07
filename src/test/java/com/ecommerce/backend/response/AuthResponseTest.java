package com.ecommerce.backend.response;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.ecommerce.backend.domain.USER_ROLE;

class AuthResponseTest {

    @Test
    void shouldSetAndGetAuthFields() {
        AuthResponse response = new AuthResponse();
        response.setJwt("token");
        response.setMessege("done");
        response.setRole(USER_ROLE.ROLE_CUSTOMER);

        assertEquals("token", response.getJwt());
        assertEquals("done", response.getMessege());
        assertEquals(USER_ROLE.ROLE_CUSTOMER, response.getRole());
    }
}
