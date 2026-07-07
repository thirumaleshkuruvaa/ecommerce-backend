package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.exceptions.ProductException;
import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.request.CreateProductRequest;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.SellerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/sellers/products")
@Tag(name = "9. Seller Products", description = "Seller product CRUD operations")
public class SellerProductController {

        private final ProductService productService;

        private final SellerService sellerService;

        public SellerProductController(
                        ProductService productService,
                        SellerService sellerService) {

                this.productService = productService;
                this.sellerService = sellerService;
        }

        // CREATE PRODUCT
        @Operation(summary = "Create Product", description = "Seller creates a new product")

        @PostMapping
        public ResponseEntity<Product> createProduct(

                        @Valid @RequestBody CreateProductRequest request,

                        @RequestHeader("Authorization") String jwt)

                        throws SellerException {

                log.info("Create product API called");

                Seller seller = sellerService.getSellerProfile(jwt);

                log.info("Seller authenticated successfully with ID : {}", seller.getId());

                Product product = productService.createProduct(
                                request,
                                seller);

                log.info("Product created successfully with ID : {}", product.getId());

                return new ResponseEntity<>(product, HttpStatus.CREATED);
        }

        // GET SELLER PRODUCTS
        @Operation(summary = "Get Seller Products", description = "Fetch all products of seller")

        @GetMapping
        public ResponseEntity<List<Product>> getSellerProducts(

                        @RequestHeader("Authorization") String jwt)

                        throws SellerException {

                log.info("Get seller products API called");

                Seller seller = sellerService.getSellerProfile(jwt);

                log.info("Fetching products for seller ID : {}", seller.getId());

                List<Product> products = productService.getProductBySellerId(seller.getId());

                log.info("Total products fetched : {}", products.size());

                return new ResponseEntity<>(products, HttpStatus.OK);
        }

        // UPDATE PRODUCT
        @Operation(summary = "Update Product", description = "Update product details")

        @PutMapping("/{productId}")
        public ResponseEntity<Product> updateProduct(

                        @PathVariable Long productId,

                        @RequestBody Product product)

                        throws ProductException {

                log.info("Update product API called for Product ID : {}", productId);

                Product updatedProduct = productService.updateProduct(productId, product);

                log.info("Product updated successfully with ID : {}", updatedProduct.getId());

                return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        }

        // DELETE PRODUCT
        @Operation(summary = "Delete Product", description = "Remove product from catalog")
        @DeleteMapping("/{productId}")
        public ResponseEntity<String> deleteProduct(
                        @PathVariable Long productId,
                        @RequestHeader("Authorization") String jwt)
                        throws ProductException, SellerException {

                Seller seller = sellerService.getSellerProfile(jwt);

                log.info("Delete product API called for Product ID : {}", productId);
                productService.deleteProduct(productId);

                log.info("Product deleted successfully with ID : {}", productId);
                return ResponseEntity.ok("Product Deleted Successfully");
        }

}