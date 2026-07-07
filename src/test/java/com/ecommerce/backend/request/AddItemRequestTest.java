package com.ecommerce.backend.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

class AddItemRequestTest {

    @Test
    void shouldSetAndGetFields() {
        AddItemRequest request = new AddItemRequest();
        request.setSizes(List.of("M", "L"));
        request.setQuantity(2);
        request.setProductId(10L);

        assertEquals(List.of("M", "L"), request.getSizes());
        assertEquals(2, request.getQuantity());
        assertEquals(10L, request.getProductId());
    }

    @Test
    void shouldStartWithNullValues() {
        AddItemRequest request = new AddItemRequest();
        assertNull(request.getSizes());
        assertNull(request.getQuantity());
        assertNull(request.getProductId());
    }
}
