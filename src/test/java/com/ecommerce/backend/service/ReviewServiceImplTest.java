package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.backend.exceptions.ReviewNotFoundException;
import com.ecommerce.backend.exceptions.UnauthorizedAcessException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Review;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.ReviewsRepository;
import com.ecommerce.backend.request.CreateReviewRequest;
import com.ecommerce.backend.service.Impl.ReviewServiceImpl;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewsRepository reviewsRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private User user;
    private Product product;
    private Review review;
    private CreateReviewRequest request;

    @BeforeEach
    void setup() {

        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setReviews(new ArrayList<>());

        review = new Review();
        review.setId(1L);
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText("Nice Product");
        review.setRating(4.5);

        request = new CreateReviewRequest();
        request.setReviewText("Excellent Product");
        request.setReviewRating(5.0);
        request.setProductImages(List.of("img1.jpg"));
    }

    @Test
    void testCreateReview() {

        when(reviewsRepository.save(any(Review.class))).thenReturn(review);

        when(reviewsRepository.findByProductId(1L))
                .thenReturn(List.of(review));

        Review saved = reviewService.createReview(request, user, product);

        assertNotNull(saved);

        verify(reviewsRepository).save(any(Review.class));

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testGetReviewByProductId() {

        when(reviewsRepository.findByProductId(1L))
                .thenReturn(List.of(review));

        List<Review> reviews = reviewService.getReviewByProductId(1L);

        assertEquals(1, reviews.size());

        verify(reviewsRepository).findByProductId(1L);
    }

    @Test
    void testGetReviewById() throws Exception {

        when(reviewsRepository.findById(1L))
                .thenReturn(Optional.of(review));

        Review result = reviewService.getReviewById(1L);

        assertEquals(review, result);
    }

    @Test
    void testGetReviewByIdThrowsException() {

        when(reviewsRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ReviewNotFoundException.class,
                () -> reviewService.getReviewById(1L));
    }

    @Test
    void testUpdateReview() throws Exception {

        when(reviewsRepository.findById(1L))
                .thenReturn(Optional.of(review));

        when(reviewsRepository.save(any()))
                .thenReturn(review);

        when(reviewsRepository.findByProductId(1L))
                .thenReturn(List.of(review));

        Review updated = reviewService.updateReview(
                1L,
                "Updated Review",
                4.0,
                1L);

        assertEquals("Updated Review",
                updated.getReviewText());

        verify(reviewsRepository).save(review);

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testUpdateReviewUnauthorized() {

        User another = new User();
        another.setId(10L);

        when(reviewsRepository.findById(1L))
                .thenReturn(Optional.of(review));

        assertThrows(
                UnauthorizedAcessException.class,
                () -> reviewService.updateReview(
                        1L,
                        "Updated",
                        5.0,
                        another.getId()));
    }

    @Test
    void testDeleteReview() throws Exception {

        when(reviewsRepository.findById(1L))
                .thenReturn(Optional.of(review));

        when(reviewsRepository.findByProductId(1L))
                .thenReturn(new ArrayList<>());

        doNothing().when(reviewsRepository).delete(review);

        reviewService.deleteReview(1L, 1L);

        verify(reviewsRepository).delete(review);

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testDeleteReviewUnauthorized() {

        User another = new User();
        another.setId(5L);

        when(reviewsRepository.findById(1L))
                .thenReturn(Optional.of(review));

        assertThrows(
                UnauthorizedAcessException.class,
                () -> reviewService.deleteReview(
                        1L,
                        another.getId()));
    }

    @Test
    void testUpdateProductRatingMultipleReviews() {

        Review r1 = new Review();
        r1.setRating(5);

        Review r2 = new Review();
        r2.setRating(3);

        when(reviewsRepository.save(any()))
                .thenReturn(review);

        when(reviewsRepository.findByProductId(1L))
                .thenReturn(List.of(r1, r2));

        reviewService.createReview(request, user, product);

        verify(productRepository).save(any(Product.class));

        assertEquals(4.0,
                product.getAverageRating());

        assertEquals(2,
                product.getTotalRatings());
    }
}