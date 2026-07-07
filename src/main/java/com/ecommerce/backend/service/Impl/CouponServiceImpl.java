package com.ecommerce.backend.service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.exceptions.CouponException;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.Coupon;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.repository.CouponRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.CouponService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CouponServiceImpl implements CouponService {

        private final UserRepository userRepository;
        private final CartRepository cartRepository;
        private final CouponRepository couponRepository;

        public CouponServiceImpl(
                        CartRepository cartRepository,
                        CouponRepository couponRepository,
                        UserRepository userRepository) {

                this.cartRepository = cartRepository;
                this.couponRepository = couponRepository;
                this.userRepository = userRepository;
        }

        // ================= APPLY COUPON =================
        @Override
        public Cart applyCoupon(String code, double orderValue, User user) throws CouponException {

                log.info("Apply coupon {} for userId {}", code, user.getId());

                Coupon coupon = couponRepository.findByCode(code);
                Cart cart = cartRepository.findByUserId(user.getId());

                if (coupon == null) {
                        log.error("Coupon not found with code {}", code);
                        throw new CouponException("Coupon is not valid");
                }

                if (cart == null) {
                        log.error("Cart not found for userId {}", user.getId());
                        throw new CouponException("Cart not found");
                }

                if (user.getUsedCoupons() != null && user.getUsedCoupons().contains(coupon)) {
                        log.warn("Coupon already used by userId {}", user.getId());
                        throw new CouponException("Coupon already used");
                }

                if (orderValue < coupon.getMinimumOrderValue()) {
                        log.warn("Order value is less than minimum required for coupon {}", code);
                        throw new CouponException(
                                        "Coupon valid only for minimum order value : " + coupon.getMinimumOrderValue());
                }

                boolean isCouponValid = coupon.isActive()
                                && !LocalDate.now().isBefore(coupon.getValidityStartDate())
                                && !LocalDate.now().isAfter(coupon.getValidityEndDate());

                if (!isCouponValid) {
                        log.warn("Coupon expired or inactive for code {}", code);
                        throw new CouponException("Coupon expired or inactive");
                }

                double discount = cart.getTotalSellingPrice() * coupon.getDiscountPercentage() / 100.0;
                int finalPrice = (int) (cart.getTotalSellingPrice() - discount);

                cart.setTotalSellingPrice(finalPrice);
                cart.setCouponCode(code);

                if (user.getUsedCoupons() != null) {
                        user.getUsedCoupons().add(coupon);
                        userRepository.save(user);
                }

                log.info("Coupon {} applied successfully for userId {}", code, user.getId());

                return cartRepository.save(cart);
        }

        // ================= REMOVE COUPON =================
        @Override
        public Cart removeCoupon(String code, User user) throws CouponException {

                log.info("Removing coupon {} for userId {}", code, user.getId());

                Coupon coupon = couponRepository.findByCode(code);

                if (coupon == null) {
                        log.error("Coupon not found with code {}", code);
                        throw new CouponException("Coupon not found");
                }

                Cart cart = cartRepository.findByUserId(user.getId());

                if (cart == null) {
                        log.error("Cart not found for userId {}", user.getId());
                        throw new CouponException("Cart not found");
                }

                if (coupon.getDiscountPercentage() >= 100) {
                        throw new CouponException("Invalid coupon discount percentage");
                }

                double originalPrice = cart.getTotalSellingPrice() / (1 - coupon.getDiscountPercentage() / 100.0);

                cart.setTotalSellingPrice((int) originalPrice);
                cart.setCouponCode(null);

                log.info("Coupon removed successfully for userId {}", user.getId());

                return cartRepository.save(cart);
        }

        // ================= FIND COUPON BY ID =================
        @Override
        public Coupon findCouponById(Long id) throws CouponException {

                log.debug("Finding coupon with id {}", id);

                return couponRepository.findById(id)
                                .orElseThrow(() -> {
                                        log.error("Coupon not found with id {}", id);
                                        return new CouponException("Coupon not found");
                                });
        }

        // ================= CREATE COUPON =================
        @Override
        @PreAuthorize("hasAuthority('ROLE_ADMIN')")
        public Coupon createCoupon(Coupon coupon) {

                log.info("Creating new coupon with code {}", coupon.getCode());

                Coupon savedCoupon = couponRepository.save(coupon);

                log.info("Coupon created successfully");

                return savedCoupon;
        }

        // ================= GET ALL COUPONS =================
        @Override
        @PreAuthorize("hasAuthority('ROLE_ADMIN')")
        public List<Coupon> findAllCoupons() {

                log.info("Fetching all coupons");

                return couponRepository.findAll();
        }

        // ================= DELETE COUPON =================
        @Override
        @PreAuthorize("hasAuthority('ROLE_ADMIN')")
        public void deleteCoupon(Long id) throws CouponException {

                log.info("Deleting coupon with id {}", id);

                findCouponById(id);
                couponRepository.deleteById(id);

                log.info("Coupon deleted successfully with id {}", id);
        }
}