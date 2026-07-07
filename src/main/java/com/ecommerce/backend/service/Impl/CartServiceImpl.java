package com.ecommerce.backend.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.CartItem;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.CartItemRepository;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.service.CartService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(
            CartItemRepository cartItemRepository,
            CartRepository cartRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    // DISCOUNT
    public int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {

        if (mrpPrice == 0)
            return 0;

        return ((mrpPrice - sellingPrice) * 100) / mrpPrice;
    }

    // ADD ITEM
    @Override
    public CartItem addCartItem(User user, Product product, List<String> sizes, int quantity) {

        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
            throw new RuntimeException("Cart not found for user");
        }

        // check duplicate (better logic)
        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct().getId().equals(product.getId())
                    && item.getSizes().containsAll(sizes)
                    && item.getSizes().size() == sizes.size()) {
                return item;
            }
        }

        CartItem cartItem = new CartItem();

        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setSizes(sizes);
        cartItem.setUserId(user.getId());

        int mrp = product.getMrpPrice() * quantity;
        int selling = product.getSellingPrice() * quantity;

        cartItem.setMrpPrice(mrp);
        cartItem.setSellingPrice(selling);

        cart.getCartItems().add(cartItem);

        CartItem savedItem = cartItemRepository.save(cartItem);

        cartRepository.save(cart);

        return savedItem;
    }

    // GET CART
    @Override
    public Cart findUserCart(User user) {

        Cart cart = cartRepository.findByUserId(user.getId());

        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        int mrp = 0;
        int selling = 0;
        int items = 0;

        for (CartItem item : cart.getCartItems()) {
            mrp += item.getMrpPrice();
            selling += item.getSellingPrice();
            items += item.getQuantity();
        }

        cart.setTotalMrpPrice(mrp);
        cart.setTotalSellingPrice(selling);
        cart.setTotalItems(items);

        cart.setDiscount(calculateDiscountPercentage(mrp, selling));

        return cart;
    }
}