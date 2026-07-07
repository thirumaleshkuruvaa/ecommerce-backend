package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.service.ProductService;

class ProductControllerTest {

    @Test
    void shouldFetchProducts() throws Exception {
        ProductService productService = mock(ProductService.class);
        ProductController controller = new ProductController(productService);

        Product product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");

        when(productService.findProductById(1L)).thenReturn(product);
        when(productService.searchProducts("test")).thenReturn(List.of(product));
        when(productService.getAllProducts(null, null, null, null, null, null, null, null, null, 0))
                .thenReturn(new PageImpl<>(List.of(product)));

        ResponseEntity<Product> byIdResponse = controller.getProductById(1L);
        ResponseEntity<List<Product>> searchResponse = controller.searchProducts("test");
        ResponseEntity<Page<Product>> listResponse = controller.getAllProducts(null, null, null, null, null, null, null,
                null, null, 0);

        assertEquals(200, byIdResponse.getStatusCode().value());
        assertEquals(product, byIdResponse.getBody());
        assertEquals(1, searchResponse.getBody().size());
        assertEquals(1, listResponse.getBody().getTotalElements());
    }
}
