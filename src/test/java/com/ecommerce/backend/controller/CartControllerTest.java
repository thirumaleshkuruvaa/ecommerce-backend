package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.service.CartItemService;
import com.ecommerce.backend.service.CartService;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.UserService;

class CartControllerTest {

    @Test
    void shouldFetchUserCart() {
        CartService cartService = mock(CartService.class);
        CartItemService cartItemService = mock(CartItemService.class);
        UserService userService = mock(UserService.class);
        ProductService productService = mock(ProductService.class);
        CartController controller = new CartController(cartService, cartItemService, userService, productService);

        User user = new User();
        user.setId(7L);
        Cart cart = new Cart();
        cart.setId(11L);

        when(userService.findUserByJwtToken("jwt")).thenReturn(user);
        when(cartService.findUserCart(user)).thenReturn(cart);

        ResponseEntity<Cart> response = controller.findUserCartHandler("jwt");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }
}
