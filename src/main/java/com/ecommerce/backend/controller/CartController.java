package com.ecommerce.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.AddItemRequest;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.exceptions.CartItemException;
import com.ecommerce.backend.exceptions.ProductException;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.CartItem;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.service.CartItemService;
import com.ecommerce.backend.service.CartService;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "4. Cart Management", description = "User cart operations like add, update, remove items")
public class CartController {

        private static final Logger log = LoggerFactory.getLogger(CartController.class);

        private final ProductService productService;

        private final CartService cartService;

        private final CartItemService cartItemService;

        private final UserService userService;

        public CartController(
                        CartService cartService,
                        CartItemService cartItemService,
                        UserService userService,
                        ProductService productService) {

                this.cartService = cartService;
                this.cartItemService = cartItemService;
                this.userService = userService;
                this.productService = productService;
        }

        // ADD ITEM TO CART
        @Operation(summary = "Add Item To Cart", description = "Add product to user cart with size and quantity")

        @PostMapping("/add")
        public ResponseEntity<CartItem> addItemToCart(
                        @Valid @RequestBody AddItemRequest req,
                        @RequestHeader("Authorization") String jwt)
                        throws ProductException {

                log.info(
                                "Add to cart request received for productId : {}",
                                req.getProductId());

                User user = userService.findUserByJwtToken(jwt);

                log.info(
                                "User fetched successfully with id : {}",
                                user.getId());

                Product product = productService.findProductById(
                                req.getProductId());

                log.info(
                                "Product fetched successfully with id : {}",
                                product.getId());

                CartItem item = cartService.addCartItem(
                                user,
                                product,
                                req.getSizes(),
                                req.getQuantity());

                log.info(
                                "Product added to cart successfully for userId : {} and cartItemId : {}",
                                user.getId(),
                                item.getId());

                return new ResponseEntity<>(
                                item,
                                HttpStatus.CREATED);
        }

        // GET USER CART
        @Operation(summary = "Get User Cart", description = "Fetch current user's cart")

        @GetMapping
        public ResponseEntity<Cart> findUserCartHandler(
                        @RequestHeader("Authorization") String jwt) {

                log.info(
                                "Fetching user cart");

                User user = userService.findUserByJwtToken(jwt);

                log.info(
                                "User authenticated successfully with id : {}",
                                user.getId());

                Cart cart = cartService.findUserCart(user);

                log.info(
                                "Cart fetched successfully for userId : {}",
                                user.getId());

                return new ResponseEntity<>(
                                cart,
                                HttpStatus.OK);
        }

        // UPDATE CART ITEM
        @Operation(summary = "Update Cart Item", description = "Update quantity or size of cart item")

        @PutMapping("/item/{cartItemId}")
        public ResponseEntity<CartItem> updateCartItemHandler(
                        @PathVariable Long cartItemId,
                        @RequestBody CartItem cartItem,
                        @RequestHeader("Authorization") String jwt)
                        throws CartItemException {

                log.info(
                                "Update cart item request received for cartItemId : {}",
                                cartItemId);

                User user = userService.findUserByJwtToken(jwt);

                log.info(
                                "User authenticated successfully with id : {}",
                                user.getId());

                if (cartItem.getQuantity() <= 0) {

                        log.error(
                                        "Invalid quantity received : {}",
                                        cartItem.getQuantity());

                        throw new CartItemException(
                                        "Quantity must be greater than 0");
                }

                CartItem updatedCartItem = cartItemService.updateCartItem(
                                user.getId(),
                                cartItemId,
                                cartItem);

                log.info(
                                "Cart item updated successfully with id : {}",
                                updatedCartItem.getId());

                return new ResponseEntity<>(
                                updatedCartItem,
                                HttpStatus.OK);
        }

        // DELETE CART ITEM
        @Operation(summary = "Remove Cart Item", description = "Delete item from cart")
        @DeleteMapping("/item/{cartItemId}")
        public ResponseEntity<ApiResponse> deleteCartItemHandler(
                        @PathVariable Long cartItemId,
                        @RequestHeader("Authorization") String jwt)

                        throws CartItemException {

                log.info(
                                "Delete cart item request received for cartItemId : {}",
                                cartItemId);

                User user = userService.findUserByJwtToken(jwt);

                log.info(
                                "User authenticated successfully with id : {}",
                                user.getId());

                cartItemService.removeCartItem(
                                user.getId(),
                                cartItemId);

                log.info(
                                "Cart item deleted successfully with id : {}",
                                cartItemId);

                ApiResponse response = new ApiResponse();

                response.setMessege(
                                "Item Remove From Cart");

                return new ResponseEntity<>(
                                response,
                                HttpStatus.ACCEPTED);
        }
}