package com.ecommerce.backend.service.Impl;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.exceptions.CartItemException;
import com.ecommerce.backend.model.CartItem;

import com.ecommerce.backend.repository.CartItemRepository;
import com.ecommerce.backend.service.CartItemService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartItemServiceImpl implements CartItemService {

        private final CartItemRepository cartItemRepository;

        public CartItemServiceImpl(CartItemRepository cartItemRepository) {
                this.cartItemRepository = cartItemRepository;
        }

        @Override
        public CartItem updateCartItem(Long userId, Long cartItemId, CartItem updatedCartItem)
                        throws CartItemException {

                CartItem existing = findCartItemById(cartItemId);

                if (!existing.getCart().getUser().getId().equals(userId)) {
                        throw new CartItemException("Unauthorized update");
                }

                existing.setQuantity(updatedCartItem.getQuantity());

                existing.setMrpPrice(existing.getQuantity() *
                                existing.getProduct().getMrpPrice());

                existing.setSellingPrice(existing.getQuantity() *
                                existing.getProduct().getSellingPrice());

                return cartItemRepository.save(existing);
        }

        @Override
        public CartItem findCartItemById(Long cartItemId) throws CartItemException {
                return cartItemRepository.findById(cartItemId)
                                .orElseThrow(() -> new CartItemException("Cart item not found: " + cartItemId));
        }

        @Override
        public void removeCartItem(Long userId, Long cartItemId) throws CartItemException {

                CartItem item = findCartItemById(cartItemId);

                if (!item.getCart().getUser().getId().equals(userId)) {
                        throw new CartItemException("Unauthorized delete");
                }

                cartItemRepository.delete(item);
        }
}