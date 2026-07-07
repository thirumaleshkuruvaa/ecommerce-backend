package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.exceptions.CouponException;
import com.ecommerce.backend.model.Coupon;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.CouponService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/coupons/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminCouponController {

    private final CouponService couponService;

    public AdminCouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    // CREATE COUPON
    @PostMapping("/create")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {

        log.info("Admin request received to create coupon: {}", coupon.getCode());

        Coupon created = couponService.createCoupon(coupon);

        log.info("Coupon created successfully with id: {}", created.getId());

        return ResponseEntity.ok(created);
    }

    // GET ALL COUPONS
    @GetMapping("/all")
    public ResponseEntity<List<Coupon>> getAllCoupons() {

        log.info("Admin request received to fetch all coupons");

        List<Coupon> coupons = couponService.findAllCoupons();

        log.info("Total coupons fetched: {}", coupons.size());

        return ResponseEntity.ok(coupons);
    }

    // DELETE COUPON
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteCoupon(@PathVariable Long id) throws CouponException {

        log.info("Admin request received to delete coupon with id: {}", id);

        couponService.deleteCoupon(id);

        log.info("Coupon deleted successfully with id: {}", id);

        ApiResponse res = new ApiResponse();
        res.setMessege("Coupon deleted successfully");

        return ResponseEntity.ok(res);
    }
}