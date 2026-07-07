package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.service.ProductService;
import com.ecommerce.backend.service.SellerService;

class SellerProductControllerTest {

    @Test
    void shouldFetchSellerProducts() throws Exception {
        ProductService productService = mock(ProductService.class);
        SellerService sellerService = mock(SellerService.class);
        SellerProductController controller = new SellerProductController(productService, sellerService);

        Seller seller = new Seller();
        seller.setId(9L);
        Product product = new Product();
        product.setId(30L);

        when(sellerService.getSellerProfile("jwt")).thenReturn(seller);
        when(productService.getProductBySellerId(9L)).thenReturn(List.of(product));

        ResponseEntity<List<Product>> response = controller.getSellerProducts("jwt");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }
}
