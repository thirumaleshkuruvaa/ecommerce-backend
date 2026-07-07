package com.ecommerce.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.backend.model.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Coupon findByCode(String code);
}
