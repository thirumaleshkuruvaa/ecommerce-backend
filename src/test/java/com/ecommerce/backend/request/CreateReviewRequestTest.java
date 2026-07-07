package com.ecommerce.backend.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

class CreateReviewRequestTest {

    @Test
    void shouldSetAndGetFields() {
        CreateReviewRequest request = new CreateReviewRequest();
        request.setReviewText("Great");
        request.setReviewRating(4.5);
        request.setProductImages(List.of("img1.png"));

        assertEquals("Great", request.getReviewText());
        assertEquals(4.5, request.getReviewRating());
        assertEquals(List.of("img1.png"), request.getProductImages());
    }

    @Test
    void shouldDefaultToNullValues() {
        CreateReviewRequest request = new CreateReviewRequest();
        assertNull(request.getReviewText());
        assertNull(request.getReviewRating());
        assertNull(request.getProductImages());
    }
}
