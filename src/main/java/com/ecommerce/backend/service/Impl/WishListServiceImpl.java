package com.ecommerce.backend.service.Impl;

import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.WishList;
import com.ecommerce.backend.repository.WishListRepository;
import com.ecommerce.backend.service.WishListService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WishListServiceImpl implements WishListService {

        private final WishListRepository wishListRepository;

        public WishListServiceImpl(
                        WishListRepository wishListRepository) {

                this.wishListRepository = wishListRepository;
        }

        // CREATE WISHLIST

        @Override
        public WishList createWishList(User user) {

                log.info(
                                "Creating wishlist for userId : {}",
                                user.getId());

                WishList wishList = new WishList();

                wishList.setUser(user);

                WishList savedWishList = wishListRepository.save(wishList);

                log.info(
                                "Wishlist created successfully with wishlistId : {}",
                                savedWishList.getId());

                return savedWishList;
        }

        // GET WISHLIST BY USER ID

        @Override
        public WishList getWishListByUserId(User user) {

                log.info(
                                "Fetching wishlist for userId : {}",
                                user.getId());

                WishList wishList = wishListRepository.findByUserId(
                                user.getId());

                // CREATE NEW WISHLIST IF NOT EXISTS

                if (wishList == null) {

                        log.warn(
                                        "Wishlist not found for userId : {}, creating new wishlist",
                                        user.getId());

                        wishList = createWishList(user);
                }

                log.info(
                                "Wishlist fetched successfully for userId : {}",
                                user.getId());

                return wishList;
        }

        // ADD / REMOVE PRODUCT FROM WISHLIST

        @Override
        public WishList addProductToWishList(User user, Product product) {

                WishList wishList = wishListRepository.findByUserId(user.getId());

                if (wishList == null) {
                        wishList = createWishList(user);
                }

                if (wishList.getProducts() == null) {
                        wishList.setProducts(new HashSet<>());
                }

                if (wishList.getProducts().contains(product)) {
                        wishList.getProducts().remove(product);
                } else {
                        wishList.getProducts().add(product);
                }

                return wishListRepository.save(wishList);
        }
}