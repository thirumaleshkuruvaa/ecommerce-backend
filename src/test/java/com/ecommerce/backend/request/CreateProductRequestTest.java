package com.ecommerce.backend.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

class CreateProductRequestTest {

    @Test
    void shouldSetAndGetFields() {
        CreateProductRequest request = new CreateProductRequest();
        request.setTitle("Phone");
        request.setDescription("Great phone");
        request.setMrpPrice(1000);
        request.setSellingPrice(900);
        request.setQuantity(3);
        request.setBrand("BrandX");
        request.setColor("Black");
        request.setImages(List.of("img1.png"));
        request.setCategory("Electronics");
        request.setCategory2("Mobiles");
        request.setCategory3("Smartphones");
        request.setSizes(List.of("128GB"));

        assertEquals("Phone", request.getTitle());
        assertEquals("Great phone", request.getDescription());
        assertEquals(1000, request.getMrpPrice());
        assertEquals(900, request.getSellingPrice());
        assertEquals(3, request.getQuantity());
        assertEquals("BrandX", request.getBrand());
        assertEquals("Black", request.getColor());
        assertEquals(List.of("img1.png"), request.getImages());
        assertEquals("Electronics", request.getCategory());
        assertEquals("Mobiles", request.getCategory2());
        assertEquals("Smartphones", request.getCategory3());
        assertEquals(List.of("128GB"), request.getSizes());
    }

    @Test
    void shouldDefaultToNullValues() {
        CreateProductRequest request = new CreateProductRequest();
        assertNull(request.getTitle());
        assertNull(request.getDescription());
        assertNull(request.getMrpPrice());
        assertNull(request.getSellingPrice());
        assertNull(request.getQuantity());
        assertNull(request.getBrand());
        assertNull(request.getColor());
        assertNull(request.getImages());
        assertNull(request.getCategory());
        assertNull(request.getCategory2());
        assertNull(request.getCategory3());
        assertNull(request.getSizes());
    }
}
