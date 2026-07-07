package com.ecommerce.backend.service;

import com.ecommerce.backend.exceptions.CartItemException;
import com.ecommerce.backend.model.CartItem;

public interface CartItemService {

    CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException;

    CartItem findCartItemById(Long id) throws CartItemException;

    void removeCartItem(Long userId, Long cartItemId) throws CartItemException;
}
