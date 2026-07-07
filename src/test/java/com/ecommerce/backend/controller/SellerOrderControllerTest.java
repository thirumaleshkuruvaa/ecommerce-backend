package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.service.OrderService;
import com.ecommerce.backend.service.SellerService;

class SellerOrderControllerTest {

    @Test
    void shouldFetchSellerOrders() throws Exception {
        SellerService sellerService = mock(SellerService.class);
        OrderService orderService = mock(OrderService.class);
        SellerOrderController controller = new SellerOrderController(sellerService, orderService);

        Seller seller = new Seller();
        seller.setId(6L);
        Order order = new Order();
        order.setId(88L);

        when(sellerService.getSellerProfile("jwt")).thenReturn(seller);
        when(orderService.sellersOrder(6L)).thenReturn(List.of(order));

        ResponseEntity<List<Order>> response = controller.getAllOrderHandler("jwt");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }
}
