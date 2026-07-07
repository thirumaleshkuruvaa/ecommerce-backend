package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Review;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.ReviewService;
import com.ecommerce.backend.service.UserService;

class ReviewsControllerTest {

    @Test
    void shouldFetchReviewsForProduct() {
        ReviewService reviewService = mock(ReviewService.class);
        UserService userService = mock(UserService.class);
        ProductService productService = mock(ProductService.class);
        ReviewsController controller = new ReviewsController(reviewService, userService, productService);

        Product product = new Product();
        product.setId(2L);
        Review review = new Review();
        review.setId(11L);

        when(reviewService.getReviewByProductId(2L)).thenReturn(List.of(review));

        ResponseEntity<List<Review>> response = controller.getReviewByProductId(2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }
}
