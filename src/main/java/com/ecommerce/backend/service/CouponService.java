package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.Coupon;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.exceptions.CouponException;

public interface CouponService {

    Cart applyCoupon(String code, double orderValue, User user) throws CouponException;

    Cart removeCoupon(String code, User user) throws CouponException;

    Coupon findCouponById(Long id) throws CouponException;

    Coupon createCoupon(Coupon coupon);

    List<Coupon> findAllCoupons();

    void deleteCoupon(Long id) throws CouponException;

}
