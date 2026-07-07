package com.ecommerce.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.exceptions.CouponException;

import com.ecommerce.backend.service.UserService;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.service.CouponService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;
    private final UserService userService;

    public CouponController(CouponService couponService, UserService userService) {
        this.couponService = couponService;
        this.userService = userService;
    }

    // APPLY / REMOVE COUPON
    @PostMapping("/apply")
    public ResponseEntity<Cart> applyCoupon(
            @RequestParam Boolean apply,
            @RequestParam String code,
            @RequestParam double orderValue,
            @RequestHeader("Authorization") String jwt) throws CouponException {

        log.info("Coupon request received. apply: {}, code: {}, orderValue: {}",
                apply, code, orderValue);

        User user = userService.findUserByJwtToken(jwt);

        log.info("Authenticated user id: {}", user.getId());

        Cart cart;

        if (Boolean.TRUE.equals(apply)) {

            log.info("Applying coupon {} for user {}", code, user.getId());

            cart = couponService.applyCoupon(code, orderValue, user);

            log.info("Coupon applied successfully");

        } else {

            log.info("Removing coupon {} for user {}", code, user.getId());

            cart = couponService.removeCoupon(code, user);

            log.info("Coupon removed successfully");
        }

        return ResponseEntity.ok(cart);
    }
}