package com.ecommerce.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.CartItem;
import com.ecommerce.backend.model.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    ;
    CartItem findByCartAndProductAndQuantity(
            Cart cart,
            Product product,
            int quantity);

}
