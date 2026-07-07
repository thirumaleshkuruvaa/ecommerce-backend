package com.ecommerce.backend.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class ApiResponseTest {

    @Test
    void shouldSetAndGetMessageAndSuccess() {
        ApiResponse response = new ApiResponse();
        response.setMessege("ok");
        response.setSuccess(true);

        assertEquals("ok", response.getMessege());
        assertFalse(!response.isSuccess());
    }
}
