package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.CartItem;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.CartItemRepository;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.service.Impl.CartServiceImpl;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Cart cart;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(100L);
        product.setMrpPrice(1000);
        product.setSellingPrice(800);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setCartItems(new HashSet<>());

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setSizes(Arrays.asList("M"));

        cartItem.setMrpPrice(2000);
        cartItem.setSellingPrice(1600);
    }

    // ----------------------------------------------------
    // calculateDiscountPercentage()
    // ----------------------------------------------------

    @Test
    void testCalculateDiscountPercentage() {

        int result = cartService.calculateDiscountPercentage(1000, 800);

        assertEquals(20, result);
    }

    @Test
    void testCalculateDiscountPercentage_WhenMrpZero() {

        int result = cartService.calculateDiscountPercentage(0, 0);

        assertEquals(0, result);
    }

    // ----------------------------------------------------
    // addCartItem()
    // ----------------------------------------------------

    @Test
    void testAddCartItem() {

        when(cartRepository.findByUserId(user.getId())).thenReturn(cart);

        when(cartItemRepository.save(any(CartItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CartItem result = cartService.addCartItem(
                user,
                product,
                Arrays.asList("M"),
                2);

        assertNotNull(result);

        assertEquals(product, result.getProduct());

        assertEquals(2, result.getQuantity());

        assertEquals(2000, result.getMrpPrice());

        assertEquals(1600, result.getSellingPrice());

        verify(cartItemRepository).save(any(CartItem.class));

        verify(cartRepository).save(cart);
    }

    @Test
    void testAddCartItem_WhenCartNotFound() {

        when(cartRepository.findByUserId(user.getId())).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cartService.addCartItem(user, product, Arrays.asList("M"), 2));

        assertEquals("Cart not found for user", ex.getMessage());

        verify(cartItemRepository, never()).save(any());
    }

    @Test
    void testAddCartItem_WhenDuplicateItemExists() {

        cart.getCartItems().add(cartItem);

        when(cartRepository.findByUserId(user.getId())).thenReturn(cart);

        CartItem result = cartService.addCartItem(
                user,
                product,
                Arrays.asList("M"),
                2);

        assertEquals(cartItem, result);

        verify(cartItemRepository, never()).save(any());

        verify(cartRepository, never()).save(any());
    }

    // ----------------------------------------------------
    // findUserCart()
    // ----------------------------------------------------

    @Test
    void testFindUserCart() {

        cart.getCartItems().add(cartItem);

        when(cartRepository.findByUserId(user.getId())).thenReturn(cart);

        Cart result = cartService.findUserCart(user);

        assertEquals(2000, result.getTotalMrpPrice());

        assertEquals(1600, result.getTotalSellingPrice());

        assertEquals(2, result.getTotalItems());

        assertEquals(20, result.getDiscount());
    }

    @Test
    void testFindUserCart_WhenCartEmpty() {

        when(cartRepository.findByUserId(user.getId())).thenReturn(cart);

        Cart result = cartService.findUserCart(user);

        assertEquals(0, result.getTotalMrpPrice());

        assertEquals(0, result.getTotalSellingPrice());

        assertEquals(0, result.getTotalItems());

        assertEquals(0, result.getDiscount());
    }

    @Test
    void testFindUserCart_WhenCartNotFound() {

        when(cartRepository.findByUserId(user.getId())).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cartService.findUserCart(user));

        assertEquals("Cart not found", ex.getMessage());
    }

}