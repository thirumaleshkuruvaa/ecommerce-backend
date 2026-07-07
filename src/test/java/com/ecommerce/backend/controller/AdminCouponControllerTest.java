package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Coupon;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.CouponService;

class AdminCouponControllerTest {

    @Test
    void shouldCreateAndListAndDeleteCoupons() throws Exception {
        CouponService couponService = mock(CouponService.class);
        AdminCouponController controller = new AdminCouponController(couponService);

        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setCode("SAVE10");

        when(couponService.createCoupon(coupon)).thenReturn(coupon);
        when(couponService.findAllCoupons()).thenReturn(List.of(coupon));

        ResponseEntity<Coupon> createResponse = controller.createCoupon(coupon);
        ResponseEntity<List<Coupon>> listResponse = controller.getAllCoupons();
        ResponseEntity<ApiResponse> deleteResponse = controller.deleteCoupon(1L);

        assertEquals(200, createResponse.getStatusCode().value());
        assertEquals(coupon, createResponse.getBody());
        assertEquals(1, listResponse.getBody().size());
        assertEquals("Coupon deleted successfully", deleteResponse.getBody().getMessege());
    }
}
