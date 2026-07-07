package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.backend.exceptions.CouponException;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.Coupon;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.repository.CouponRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.Impl.CouponServiceImpl;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    private User user;
    private Cart cart;
    private Coupon coupon;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setUsedCoupons(new HashSet<>());

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setTotalSellingPrice(1000);

        coupon = new Coupon();
        coupon.setId(1L);
        coupon.setCode("SAVE10");
        coupon.setDiscountPercentage(10);
        coupon.setMinimumOrderValue(500);
        coupon.setActive(true);
        coupon.setValidityStartDate(LocalDate.now().minusDays(1));
        coupon.setValidityEndDate(LocalDate.now().plusDays(10));
    }

    // =====================================================
    // APPLY COUPON SUCCESS
    // =====================================================

    @Test
    void testApplyCouponSuccess() throws Exception {

        when(couponRepository.findByCode("SAVE10"))
                .thenReturn(coupon);

        when(cartRepository.findByUserId(1L))
                .thenReturn(cart);

        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        Cart result = couponService.applyCoupon(
                "SAVE10",
                1000,
                user);

        assertNotNull(result);

        assertEquals(
                900,
                result.getTotalSellingPrice());

        assertEquals(
                "SAVE10",
                result.getCouponCode());

        verify(userRepository).save(user);

        verify(cartRepository).save(cart);
    }

    // =====================================================
    // COUPON NOT FOUND
    // =====================================================

    @Test
    void testApplyCouponCouponNotFound() {

        when(couponRepository.findByCode("SAVE10"))
                .thenReturn(null);

        CouponException exception = assertThrows(
                CouponException.class,
                () -> couponService.applyCoupon(
                        "SAVE10",
                        1000,
                        user));

        assertEquals(
                "Coupon is not valid",
                exception.getMessage());

        verify(cartRepository, never())
                .save(any());
    }

    // =====================================================
    // CART NOT FOUND
    // =====================================================

    @Test
    void testApplyCouponCartNotFound() {

        when(couponRepository.findByCode("SAVE10"))
                .thenReturn(coupon);

        when(cartRepository.findByUserId(1L))
                .thenReturn(null);

        CouponException exception = assertThrows(
                CouponException.class,
                () -> couponService.applyCoupon(
                        "SAVE10",
                        1000,
                        user));

        assertEquals(
                "Cart not found",
                exception.getMessage());
    }

    // =====================================================
    // ALREADY USED
    // =====================================================

    @Test
    void testApplyCouponAlreadyUsed() {

        user.getUsedCoupons().add(coupon);

        when(couponRepository.findByCode("SAVE10"))
                .thenReturn(coupon);

        when(cartRepository.findByUserId(1L))
                .thenReturn(cart);

        CouponException exception = assertThrows(
                CouponException.class,
                () -> couponService.applyCoupon(
                        "SAVE10",
                        1000,
                        user));

        assertEquals(
                "Coupon already used",
                exception.getMessage());
    }

    // =====================================================
    // MINIMUM ORDER VALUE
    // =====================================================

    @Test
    void testApplyCouponMinimumOrderValue() {

        when(couponRepository.findByCode("SAVE10"))
                .thenReturn(coupon);

        when(cartRepository.findByUserId(1L))
                .thenReturn(cart);

        CouponException exception = assertThrows(
                CouponException.class,
                () -> couponService.applyCoupon(
                        "SAVE10",
                        100,
                        user));

        assertTrue(
                exception.getMessage().contains(
                        "Coupon valid only"));
    }

    // =====================================================
    // EXPIRED COUPON
    // =====================================================

    @Test
    void testApplyCouponExpired() {

        coupon.setValidityStartDate(LocalDate.now().minusDays(20));
        coupon.setValidityEndDate(LocalDate.now().minusDays(10));

        when(couponRepository.findByCode("SAVE10"))
                .thenReturn(coupon);

        when(cartRepository.findByUserId(1L))
                .thenReturn(cart);

        CouponException exception = assertThrows(
                CouponException.class,
                () -> couponService.applyCoupon(
                        "SAVE10",
                        1000,
                        user));

        assertEquals(
                "Coupon expired or inactive",
                exception.getMessage());
    }

    // =====================================================
    // INACTIVE COUPON
    // =====================================================

    @Test
    void testApplyCouponInactiveCoupon() {

        coupon.setActive(false);

        when(couponRepository.findByCode("SAVE10"))
                .thenReturn(coupon);

        when(cartRepository.findByUserId(1L))
                .thenReturn(cart);

        CouponException exception = assertThrows(
                CouponException.class,
                () -> couponService.applyCoupon(
                        "SAVE10",
                        1000,
                        user));

        assertEquals(
                "Coupon expired or inactive",
                exception.getMessage());
    }
    // =====================================================
    // REMOVE COUPON SUCCESS
    // =====================================================

    @Test
    void testRemoveCouponSuccess() throws Exception {

        cart.setCouponCode("SAVE10");
        cart.setTotalSellingPrice(900);

        when(couponRepository.findByCode("SAVE10"))
                .thenReturn(coupon);

        when(cartRepository.findByUserId(1L))
                .thenReturn(cart);

        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        Cart result = couponService.removeCoupon(
                "SAVE10",
                user);

        assertNotNull(result);

        assertNull(result.getCouponCode());

        assertEquals(
                1000,
                result.getTotalSellingPrice());

        verify(cartRepository).save(cart);
    }

    // =====================================================
    // REMOVE COUPON NOT FOUND
    // =====================================================

    @Test
    void testRemoveCouponCouponNotFound() {

        when(couponRepository.findByCode("SAVE10"))
                .thenReturn(null);

        CouponException exception = assertThrows(
                CouponException.class,
                () -> couponService.removeCoupon(
                        "SAVE10",
                        user));

        assertEquals(
                "Coupon not found",
                exception.getMessage());
    }

    // =====================================================
    // REMOVE COUPON CART NOT FOUND
    // =====================================================

    @Test
    void testRemoveCouponCartNotFound() {

        when(couponRepository.findByCode("SAVE10"))
                .thenReturn(coupon);

        when(cartRepository.findByUserId(1L))
                .thenReturn(null);

        CouponException exception = assertThrows(
                CouponException.class,
                () -> couponService.removeCoupon(
                        "SAVE10",
                        user));

        assertEquals(
                "Cart not found",
                exception.getMessage());
    }

    // =====================================================
    // REMOVE COUPON INVALID DISCOUNT
    // =====================================================

    @Test
    void testRemoveCouponInvalidDiscount() {

        coupon.setDiscountPercentage(100);

        when(couponRepository.findByCode("SAVE10"))
                .thenReturn(coupon);

        when(cartRepository.findByUserId(1L))
                .thenReturn(cart);

        CouponException exception = assertThrows(
                CouponException.class,
                () -> couponService.removeCoupon(
                        "SAVE10",
                        user));

        assertEquals(
                "Invalid coupon discount percentage",
                exception.getMessage());
    }

    // =====================================================
    // FIND COUPON BY ID SUCCESS
    // =====================================================

    @Test
    void testFindCouponByIdSuccess() throws Exception {

        when(couponRepository.findById(1L))
                .thenReturn(Optional.of(coupon));

        Coupon result = couponService.findCouponById(1L);

        assertNotNull(result);

        assertEquals(
                "SAVE10",
                result.getCode());
    }

    // =====================================================
    // FIND COUPON BY ID EXCEPTION
    // =====================================================

    @Test
    void testFindCouponByIdException() {

        when(couponRepository.findById(1L))
                .thenReturn(Optional.empty());

        CouponException exception = assertThrows(
                CouponException.class,
                () -> couponService.findCouponById(1L));

        assertEquals(
                "Coupon not found",
                exception.getMessage());
    }

    // =====================================================
    // CREATE COUPON
    // =====================================================

    @Test
    void testCreateCoupon() {

        when(couponRepository.save(any(Coupon.class)))
                .thenReturn(coupon);

        Coupon saved = couponService.createCoupon(coupon);

        assertNotNull(saved);

        assertEquals(
                "SAVE10",
                saved.getCode());

        verify(couponRepository).save(coupon);
    }

    // =====================================================
    // FIND ALL COUPONS
    // =====================================================

    @Test
    void testFindAllCoupons() {

        when(couponRepository.findAll())
                .thenReturn(List.of(coupon));

        List<Coupon> coupons = couponService.findAllCoupons();

        assertEquals(
                1,
                coupons.size());

        verify(couponRepository).findAll();
    }

    // =====================================================
    // DELETE COUPON SUCCESS
    // =====================================================

    @Test
    void testDeleteCouponSuccess() throws Exception {

        when(couponRepository.findById(1L))
                .thenReturn(Optional.of(coupon));

        doNothing().when(couponRepository)
                .deleteById(1L);

        couponService.deleteCoupon(1L);

        verify(couponRepository)
                .deleteById(1L);
    }

    // =====================================================
    // DELETE COUPON EXCEPTION
    // =====================================================

    @Test
    void testDeleteCouponException() {

        when(couponRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                CouponException.class,
                () -> couponService.deleteCoupon(1L));

        verify(couponRepository, never())
                .deleteById(anyLong());
    }

}