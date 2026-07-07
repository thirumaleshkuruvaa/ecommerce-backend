package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.exceptions.ReviewNotFoundException;
import com.ecommerce.backend.exceptions.UnauthorizedAcessException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Review;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.CreateReviewRequest;

public interface ReviewService {

    Review createReview(CreateReviewRequest createReviewRequest, User user, Product product);

    List<Review> getReviewByProductId(Long productId);

    Review updateReview(Long reviewId, String reviewText, double rating, Long userId)
            throws ReviewNotFoundException, UnauthorizedAcessException;

    void deleteReview(Long reviewId, Long userId) throws ReviewNotFoundException, UnauthorizedAcessException;

    Review getReviewById(Long reviewId) throws ReviewNotFoundException;
}
