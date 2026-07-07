package com.ecommerce.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.exceptions.ProductException;
import com.ecommerce.backend.exceptions.UserException;
import com.ecommerce.backend.exceptions.WishListException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.WishList;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.UserService;
import com.ecommerce.backend.service.WishListService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/wishlist")
@Slf4j
@Tag(name = "5. Wishlist", description = "Manage user's wishlist products")
public class WishListController {

    private final ProductService productService;

    private final WishListService wishListService;

    private final UserService userService;

    public WishListController(
            ProductService productService,
            UserService userService,
            WishListService wishListService) {

        this.userService = userService;
        this.wishListService = wishListService;
        this.productService = productService;
    }

    // ADD / REMOVE PRODUCT FROM WISHLIST
    @Operation(summary = "Add/Remove Product", description = "Toggle product in wishlist")

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<WishList> addProductToWishList(

            @PathVariable Long productId,

            @RequestHeader("Authorization") String jwt)

            throws ProductException,
            UserException,
            WishListException {

        log.info("Add/Remove product from wishlist API called for Product ID : {}", productId);

        Product product = productService.findProductById(productId);

        User user = userService.findUserByJwtToken(jwt);

        log.info("Authenticated User ID : {}", user.getId());

        WishList updatedWishList = wishListService.addProductToWishList(user, product);

        log.info("Wishlist updated successfully for User ID : {}", user.getId());

        return ResponseEntity.ok(updatedWishList);
    }

    // GET USER WISHLIST

    @GetMapping
    @Operation(summary = "Get Wishlist", description = "Fetch all wishlist items of user")
    public ResponseEntity<WishList> getWishListByUserId(

            @RequestHeader("Authorization") String jwt)

            throws UserException, WishListException {

        log.info("Get wishlist API called");

        User user = userService.findUserByJwtToken(jwt);

        log.info("Fetching wishlist for User ID : {}", user.getId());

        WishList wishList = wishListService.getWishListByUserId(user);

        log.info("Wishlist fetched successfully for User ID : {}", user.getId());

        return ResponseEntity.ok(wishList);
    }

}