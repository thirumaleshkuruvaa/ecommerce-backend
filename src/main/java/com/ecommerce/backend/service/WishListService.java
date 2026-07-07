package com.ecommerce.backend.service;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.WishList;

public interface WishListService {

    WishList createWishList(User user);

    WishList getWishListByUserId(User user);

    WishList addProductToWishList(User user, Product product);
}
