package com.ecommerce.backend.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.exceptions.ReviewNotFoundException;
import com.ecommerce.backend.exceptions.UnauthorizedAcessException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Review;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.ReviewsRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.request.CreateReviewRequest;
import com.ecommerce.backend.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

        private final ReviewsRepository reviewsRepository;

        private final ProductRepository productRepository;

        public ReviewServiceImpl(
                        ReviewsRepository reviewsRepository, ProductRepository productRepository) {

                this.reviewsRepository = reviewsRepository;
                this.productRepository = productRepository;
        }

        // CREATE REVIEW

        @Override
        public Review createReview(
                        CreateReviewRequest createReviewRequest,
                        User user,
                        Product product) {

                log.info("Creating review for product id {} by user id {}",
                                product.getId(),
                                user.getId());

                Review review = new Review();

                review.setUser(user);

                review.setProduct(product);

                review.setReviewText(
                                createReviewRequest.getReviewText());

                review.setRating(
                                createReviewRequest.getReviewRating());

                review.setProductImages(
                                createReviewRequest.getProductImages());

                product.getReviews().add(review);

                Review savedReview = reviewsRepository.save(review);

                updateProductRating(product);
                log.info("Review created successfully with id {}",
                                savedReview.getId());
                return savedReview;
        }

        // GET REVIEWS BY PRODUCT ID

        @Override
        public List<Review> getReviewByProductId(
                        Long productId) {

                log.info("Fetching reviews for product id {}",
                                productId);

                List<Review> reviews = reviewsRepository.findByProductId(productId);

                log.debug("Total reviews found : {}",
                                reviews.size());

                return reviews;
        }

        // UPDATE REVIEW

        @Override
        public Review updateReview(
                        Long reviewId,
                        String reviewText,
                        double rating,
                        Long userId)

                        throws ReviewNotFoundException,
                        UnauthorizedAcessException {

                log.info("Updating review id {} by user id {}",
                                reviewId,
                                userId);

                Review review = getReviewById(reviewId);

                // SECURITY CHECK

                if (!review.getUser()
                                .getId()
                                .equals(userId)) {

                        log.warn("Unauthorized review update attempt. User id {}",
                                        userId);

                        throw new UnauthorizedAcessException(
                                        "You can't update this review");
                }

                review.setReviewText(reviewText);

                review.setRating(rating);

                Review updatedReview = reviewsRepository.save(review);

                updateProductRating(review.getProduct());
                log.info("Review updated successfully with id {}",
                                reviewId);
                return updatedReview;
        }

        // DELETE REVIEW

        @Override
        public void deleteReview(
                        Long reviewId,
                        Long userId)

                        throws ReviewNotFoundException,
                        UnauthorizedAcessException {

                log.info("Deleting review id {} by user id {}",
                                reviewId,
                                userId);

                Review review = getReviewById(reviewId);

                // SECURITY CHECK

                if (!review.getUser()
                                .getId()
                                .equals(userId)) {

                        log.warn("Unauthorized review delete attempt. User id {}",
                                        userId);

                        throw new UnauthorizedAcessException(
                                        "You can't delete this review");
                }

                // reviewsRepository.delete(review);

                Product product = review.getProduct();

                reviewsRepository.delete(review);

                updateProductRating(product);
                log.info("Review deleted successfully with id {}",
                                reviewId);
        }

        // GET REVIEW BY ID

        @Override
        public Review getReviewById(Long reviewId)
                        throws ReviewNotFoundException {

                log.info("Fetching review by id {}",
                                reviewId);

                return reviewsRepository.findById(reviewId)

                                .orElseThrow(() -> {

                                        log.error("Review not found with id {}",
                                                        reviewId);

                                        return new ReviewNotFoundException(
                                                        "Review not found");
                                });
        }

        // updatedproductrating
        private void updateProductRating(Product product) {

                List<Review> reviews = reviewsRepository.findByProductId(product.getId());

                double avgRating = reviews.stream()
                                .mapToDouble(Review::getRating)
                                .average()
                                .orElse(0.0);

                avgRating = Math.round(avgRating * 10.0) / 10.0;

                product.setAverageRating(avgRating);

                product.setTotalRatings(reviews.size());

                productRepository.save(product);
        }
}