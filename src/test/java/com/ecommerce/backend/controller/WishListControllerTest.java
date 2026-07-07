package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.WishList;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.UserService;
import com.ecommerce.backend.service.WishListService;

class WishListControllerTest {

    @Test
    void shouldFetchWishlist() throws Exception {
        ProductService productService = mock(ProductService.class);
        UserService userService = mock(UserService.class);
        WishListService wishListService = mock(WishListService.class);
        WishListController controller = new WishListController(productService, userService, wishListService);

        User user = new User();
        user.setId(5L);
        Product product = new Product();
        product.setId(21L);
        WishList wishlist = new WishList();
        wishlist.setId(13L);

        when(productService.findProductById(21L)).thenReturn(product);
        when(userService.findUserByJwtToken("jwt")).thenReturn(user);
        when(wishListService.getWishListByUserId(user)).thenReturn(wishlist);

        ResponseEntity<WishList> response = controller.getWishListByUserId("jwt");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(wishlist, response.getBody());
    }
}
