package com.ecommerce.backend.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalException {

        // SELLER EXCEPTION
        @ExceptionHandler(SellerException.class)
        public ResponseEntity<ErrorDetails> handleSellerException(
                        SellerException sellerException,
                        WebRequest req) {

                log.error("SellerException occurred : {}", sellerException.getMessage(), sellerException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                sellerException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // PRODUCT EXCEPTION
        @ExceptionHandler(ProductException.class)
        public ResponseEntity<ErrorDetails> handleProductException(
                        ProductException productException,
                        WebRequest req) {

                log.error("ProductException occurred : {}", productException.getMessage(), productException);
                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                productException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // CartItem EXCEPTION
        @ExceptionHandler(CartItemException.class)
        public ResponseEntity<ErrorDetails> handleCartItemException(
                        CartItemException cartItemException,
                        WebRequest req) {

                log.error("CartItemException occurred : {}", cartItemException.getMessage(), cartItemException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                cartItemException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // ORDER EXCEPTION
        @ExceptionHandler(OrderException.class)
        public ResponseEntity<ErrorDetails> handleOrderException(
                        OrderException orderException,
                        WebRequest req) {

                log.error("OrderException occurred : {}", orderException.getMessage(), orderException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                orderException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // ORDERNOTFOUND EXCEPTION
        @ExceptionHandler(OrderNotFoundException.class)
        public ResponseEntity<ErrorDetails> handleOrderNotFoundException(
                        OrderNotFoundException orderNotFoundException,
                        WebRequest req) {

                log.error("OrderNotFoundException occurred : {}", orderNotFoundException.getMessage(),
                                orderNotFoundException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                orderNotFoundException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // USER EXCEPTION

        @ExceptionHandler(UserException.class)
        public ResponseEntity<ErrorDetails> handleUserException(
                        UserException userException,
                        WebRequest req) {

                log.error("UserException occurred : {}", userException.getMessage(), userException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                userException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // PAYMENT EXCEPTION
        @ExceptionHandler(PaymentException.class)
        public ResponseEntity<ErrorDetails> handlePaymentException(
                        PaymentException paymentException,
                        WebRequest req) {

                log.error("PaymentException occurred : {}", paymentException.getMessage(), paymentException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                paymentException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // RAZOREPAYMENT EXCEPTION
        @ExceptionHandler(RazorePayException.class)
        public ResponseEntity<ErrorDetails> handleRazorePayException(
                        RazorePayException razorePayException,
                        WebRequest req) {

                log.error("RazorePaymentexception occurred : {}", razorePayException.getMessage(), razorePayException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                razorePayException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // STRIPEPAYMENT EXCEPTION
        @ExceptionHandler(StripePayException.class)
        public ResponseEntity<ErrorDetails> handleStripePayException(
                        StripePayException stripePayException,
                        WebRequest req) {

                log.error("StripePaymentException occurred : {}", stripePayException.getMessage(), stripePayException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                stripePayException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // WishListException EXCEPTION
        @ExceptionHandler(WishListException.class)
        public ResponseEntity<ErrorDetails> handleWishListException(
                        WishListException wishListException,
                        WebRequest req) {

                log.error("WishListException occurred : {}", wishListException.getMessage(), wishListException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                wishListException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // ReviewNotFoundException EXCEPTION
        @ExceptionHandler(ReviewNotFoundException.class)
        public ResponseEntity<ErrorDetails> handleReviewNotFoundException(
                        ReviewNotFoundException reviewNotFoundException,
                        WebRequest req) {

                log.error("ReviewNotFoundException occurred : {}", reviewNotFoundException.getMessage(),
                                reviewNotFoundException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                reviewNotFoundException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // CouponException EXCEPTION
        @ExceptionHandler(CouponException.class)
        public ResponseEntity<ErrorDetails> handleCouponException(
                        CouponException couponException,
                        WebRequest req) {

                log.error("CouponException occurred : {}", couponException.getMessage(), couponException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                couponException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // HomeCategoryException EXCEPTION
        @ExceptionHandler(HomeCategoryException.class)
        public ResponseEntity<ErrorDetails> handleCouponException(
                        HomeCategoryException homeCategoryException,
                        WebRequest req) {

                log.error("HomeCategoryException occurred : {}", homeCategoryException.getMessage(),
                                homeCategoryException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                homeCategoryException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // DealException EXCEPTION
        @ExceptionHandler(DealException.class)
        public ResponseEntity<ErrorDetails> handleDealException(
                        DealException dealException,
                        WebRequest req) {

                log.error("DealException occurred : {}", dealException.getMessage(), dealException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                dealException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // OtpException EXCEPTION
        @ExceptionHandler(OtpException.class)
        public ResponseEntity<ErrorDetails> handleOtpException(
                        OtpException otpException,

                        WebRequest req) {

                log.error("OtpException occurred : {}", otpException.getMessage(), otpException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                otpException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // UserAlreadyExistsException EXCEPTION
        @ExceptionHandler(UserAlreadyExistsException.class)
        public ResponseEntity<ErrorDetails> handleUserAlreadyExistsException(
                        UserAlreadyExistsException userAlreadyExistsException,

                        WebRequest req) {

                log.error("UserAlreadyException occurred : {}", userAlreadyExistsException.getMessage(),
                                userAlreadyExistsException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                userAlreadyExistsException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.BAD_REQUEST);
        }

        // AddressNotFound Exception
        @ExceptionHandler(AddressNotFoundException.class)
        public ResponseEntity<ErrorDetails> handleAddressNotFoundException(

                        AddressNotFoundException addressNotFoundException,

                        WebRequest req) {

                log.error(
                                "AddressNotFound Exception occurred : {}",
                                addressNotFoundException.getMessage(),
                                addressNotFoundException);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                addressNotFoundException.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Common Exception
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorDetails> handleGlobalException(

                        Exception exception,

                        WebRequest req) {

                log.error(
                                "Unhandled Exception occurred : {}",
                                exception.getMessage(),
                                exception);

                ErrorDetails errorDetails = new ErrorDetails();

                errorDetails.setError(
                                exception.getMessage());

                errorDetails.setDetails(
                                req.getDescription(false));

                errorDetails.setTimestamp(
                                LocalDateTime.now());

                return new ResponseEntity<>(
                                errorDetails,
                                HttpStatus.INTERNAL_SERVER_ERROR);
        }
}