package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.exceptions.ProductException;
import com.ecommerce.backend.exceptions.ReviewNotFoundException;
import com.ecommerce.backend.exceptions.UnauthorizedAcessException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Review;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.CreateReviewRequest;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.ReviewService;
import com.ecommerce.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
@Tag(name = "12. Reviews", description = "Product reviews management")
public class ReviewsController {

        private final ReviewService reviewService;

        private final UserService userService;

        private final ProductService productService;

        public ReviewsController(
                        ReviewService reviewService,
                        UserService userService,
                        ProductService productService) {

                this.reviewService = reviewService;
                this.userService = userService;
                this.productService = productService;
        }

        // CREATE REVIEW
        @Operation(summary = "Create Review", description = "Add review for product")

        @PostMapping("/products/{productId}")
        public ResponseEntity<Review> writeReview(

                        @Valid @RequestBody CreateReviewRequest createReviewRequest,

                        @PathVariable Long productId,

                        @RequestHeader("Authorization") String jwt)

                        throws ProductException {

                log.info("Creating review for productId : {}", productId);

                User user = userService.findUserByJwtToken(jwt);

                log.info("Authenticated user : {}", user.getEmail());

                Product product = productService.findProductById(productId);

                Review review = reviewService.createReview(createReviewRequest, user, product);

                log.info("Review created successfully with reviewId : {}", review.getId());

                return new ResponseEntity<>(review, HttpStatus.CREATED);
        }

        // GET REVIEWS BY PRODUCT ID
        @Operation(summary = "Get Reviews", description = "Fetch reviews for product")

        @GetMapping("/products/{productId}")
        public ResponseEntity<List<Review>> getReviewByProductId(

                        @PathVariable Long productId) {

                log.info("Fetching reviews for productId : {}", productId);

                List<Review> reviews = reviewService.getReviewByProductId(productId);

                log.info("Successfully fetched {} reviews for productId : {}", reviews.size(), productId);

                return ResponseEntity.ok(reviews);
        }

        // UPDATE REVIEW

        @Operation(summary = "Update Review", description = "Update user review")

        @PatchMapping("/{reviewId}")
        public ResponseEntity<Review> updateReview(

                        @RequestBody CreateReviewRequest createReviewRequest,

                        @PathVariable Long reviewId,

                        @RequestHeader("Authorization") String jwt)

                        throws ReviewNotFoundException,
                        UnauthorizedAcessException {

                log.info("Updating review with reviewId : {}", reviewId);

                User user = userService.findUserByJwtToken(jwt);

                log.info("Authenticated user : {}", user.getEmail());

                Review review = reviewService.updateReview(reviewId, createReviewRequest.getReviewText(),
                                createReviewRequest.getReviewRating(), user.getId());

                log.info("Review updated successfully with reviewId : {}", reviewId);

                return ResponseEntity.ok(review);
        }

        // DELETE REVIEW

        @Operation(summary = "Delete Review", description = "Remove review")
        @DeleteMapping("/{reviewId}")
        public ResponseEntity<ApiResponse> deleteReview(

                        @PathVariable Long reviewId,

                        @RequestHeader("Authorization") String jwt)

                        throws ReviewNotFoundException,
                        UnauthorizedAcessException {

                log.info("Deleting review with reviewId : {}", reviewId);

                User user = userService.findUserByJwtToken(jwt);

                log.info("Authenticated user : {}", user.getEmail());

                reviewService.deleteReview(reviewId, user.getId());

                log.info("Review deleted successfully with reviewId : {}", reviewId);

                ApiResponse response = new ApiResponse();

                response.setMessege("Review deleted successfully");

                response.setSuccess(true);

                return ResponseEntity.ok(response);
        }

}
