
package com.ecommerce.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecommerce.backend.exceptions.ProductException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.request.CreateProductRequest;

public interface ProductService {

    Product createProduct(CreateProductRequest req, Seller seller);

    Product updateProduct(Long productId, Product product) throws ProductException;

    void deleteProduct(Long productId) throws ProductException;

    List<Product> searchProducts(String query);

    Product findProductById(Long productId) throws ProductException;

    int calculateDiscountPercentage(int mrpPrice, int sellingPrice);

    Page<Product> getAllProducts(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber);

    List<Product> getProductBySellerId(Long sellerId);
}