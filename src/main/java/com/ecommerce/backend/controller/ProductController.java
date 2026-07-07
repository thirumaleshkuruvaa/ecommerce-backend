
package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.exceptions.ProductException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/products")
@Tag(name = "3. Product Catalog", description = "Public product browsing, search, filter, pagination")
public class ProductController {

        private final ProductService productService;

        public ProductController(ProductService productService) {
                this.productService = productService;
        }

        // GET PRODUCT BY ID
        @Operation(summary = "Get Product By ID", description = "Fetch product details by product ID")
        @GetMapping("/{productId}")
        public ResponseEntity<Product> getProductById(@PathVariable Long productId) throws ProductException {

                log.info("Fetching product with id: {}", productId);

                Product product = productService.findProductById(productId);

                log.info("Product fetched successfully: {}", product.getTitle());

                return ResponseEntity.ok(product);
        }

        // SEARCH PRODUCTS
        @Operation(summary = "Search Products", description = "Search products using keyword")
        @GetMapping("/search")
        public ResponseEntity<List<Product>> searchProducts(
                        @RequestParam(required = false) String query) {

                log.info("Searching products with query: {}", query);

                List<Product> products = productService.searchProducts(query);

                log.info("Total products found: {}", products.size());

                return ResponseEntity.ok(products);
        }

        // GET ALL PRODUCTS
        @Operation(summary = "Get All Products", description = "Get products with filters, pagination, sorting")
        @GetMapping
        public ResponseEntity<Page<Product>> getAllProducts(

                        @RequestParam(required = false) String category,
                        @RequestParam(required = false) String brand,
                        @RequestParam(required = false) String colors,
                        @RequestParam(required = false) String sizes,
                        @RequestParam(required = false) Integer minPrice,
                        @RequestParam(required = false) Integer maxPrice,
                        @RequestParam(required = false) Integer minDiscount,
                        @RequestParam(required = false) String sort,
                        @RequestParam(required = false) String stock,
                        @RequestParam(defaultValue = "0") Integer pageNumber) {

                log.info("Fetching products with filters -> category: {}, brand: {}, colors: {}, sizes: {}, minPrice: {}, maxPrice: {}, minDiscount: {}, sort: {}, stock: {}, pageNumber: {}",
                                category, brand, colors, sizes, minPrice, maxPrice, minDiscount, sort, stock,
                                pageNumber);

                Page<Product> products = productService.getAllProducts(
                                category,
                                brand,
                                colors,
                                sizes,
                                minPrice,
                                maxPrice,
                                minDiscount,
                                sort,
                                stock,
                                pageNumber);

                log.info("Products fetched successfully. Total elements: {}, Total pages: {}",
                                products.getTotalElements(), products.getTotalPages());

                return new ResponseEntity<>(products, HttpStatus.OK);
        }
}