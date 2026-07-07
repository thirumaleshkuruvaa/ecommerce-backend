package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.CartItem;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;

public interface CartService {

    public CartItem addCartItem(
            User user,
            Product product,
            List<String> sizes,
            // String size,
            int quantity);

    public Cart findUserCart(User user);

}
