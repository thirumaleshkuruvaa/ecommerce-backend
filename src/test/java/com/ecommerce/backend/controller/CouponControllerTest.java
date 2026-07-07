package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.service.CouponService;
import com.ecommerce.backend.service.UserService;

class CouponControllerTest {

    @Test
    void shouldApplyCoupon() throws Exception {
        CouponService couponService = mock(CouponService.class);
        UserService userService = mock(UserService.class);
        CouponController controller = new CouponController(couponService, userService);

        User user = new User();
        user.setId(3L);
        Cart cart = new Cart();
        cart.setId(99L);

        when(userService.findUserByJwtToken("jwt")).thenReturn(user);
        when(couponService.applyCoupon("SAVE10", 100.0, user)).thenReturn(cart);

        ResponseEntity<Cart> response = controller.applyCoupon(true, "SAVE10", 100.0, "jwt");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(cart, response.getBody());
    }
}
