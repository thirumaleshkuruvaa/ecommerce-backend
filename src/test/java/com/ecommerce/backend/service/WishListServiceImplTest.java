package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.WishList;
import com.ecommerce.backend.repository.WishListRepository;
import com.ecommerce.backend.service.Impl.WishListServiceImpl;

@ExtendWith(MockitoExtension.class)
class WishListServiceImplTest {

    @Mock
    private WishListRepository wishListRepository;

    @InjectMocks
    private WishListServiceImpl wishListService;

    private User user;
    private Product product;
    private WishList wishList;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(101L);

        wishList = new WishList();
        wishList.setId(1L);
        wishList.setUser(user);
        wishList.setProducts(new HashSet<>());
    }

    @Test
    void testCreateWishList() {

        when(wishListRepository.save(any(WishList.class)))
                .thenReturn(wishList);

        WishList result = wishListService.createWishList(user);

        assertNotNull(result);
        assertEquals(user, result.getUser());

        verify(wishListRepository, times(1))
                .save(any(WishList.class));
    }

    @Test
    void testGetWishListByUserId_WhenWishlistExists() {

        when(wishListRepository.findByUserId(user.getId()))
                .thenReturn(wishList);

        WishList result = wishListService.getWishListByUserId(user);

        assertNotNull(result);
        assertEquals(wishList.getId(), result.getId());

        verify(wishListRepository, times(1))
                .findByUserId(user.getId());

        verify(wishListRepository, never())
                .save(any());
    }

    @Test
    void testGetWishListByUserId_WhenWishlistDoesNotExist() {

        when(wishListRepository.findByUserId(user.getId()))
                .thenReturn(null);

        when(wishListRepository.save(any(WishList.class)))
                .thenReturn(wishList);

        WishList result = wishListService.getWishListByUserId(user);

        assertNotNull(result);

        verify(wishListRepository).findByUserId(user.getId());
        verify(wishListRepository).save(any(WishList.class));
    }

    @Test
    void testAddProductToWishList_NewProduct() {

        when(wishListRepository.findByUserId(user.getId()))
                .thenReturn(wishList);

        when(wishListRepository.save(any(WishList.class)))
                .thenReturn(wishList);

        WishList result = wishListService.addProductToWishList(user, product);

        assertTrue(result.getProducts().contains(product));

        verify(wishListRepository).save(wishList);
    }

    @Test
    void testRemoveProductFromWishList_WhenAlreadyExists() {

        wishList.getProducts().add(product);

        when(wishListRepository.findByUserId(user.getId()))
                .thenReturn(wishList);

        when(wishListRepository.save(any(WishList.class)))
                .thenReturn(wishList);

        WishList result = wishListService.addProductToWishList(user, product);

        assertFalse(result.getProducts().contains(product));

        verify(wishListRepository).save(wishList);
    }

    @Test
    void testAddProduct_WhenWishlistDoesNotExist() {

        when(wishListRepository.findByUserId(user.getId()))
                .thenReturn(null);

        when(wishListRepository.save(any(WishList.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        WishList result = wishListService.addProductToWishList(user, product);

        assertNotNull(result);
        assertTrue(result.getProducts().contains(product));

        verify(wishListRepository, atLeastOnce())
                .save(any(WishList.class));
    }

    @Test
    void testAddProduct_WhenProductSetIsNull() {

        wishList.setProducts(null);

        when(wishListRepository.findByUserId(user.getId()))
                .thenReturn(wishList);

        when(wishListRepository.save(any(WishList.class)))
                .thenReturn(wishList);

        WishList result = wishListService.addProductToWishList(user, product);

        assertNotNull(result.getProducts());
        assertTrue(result.getProducts().contains(product));
    }
}