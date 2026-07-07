package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.PaymentOrderRepository;
import com.ecommerce.backend.service.CartService;
import com.ecommerce.backend.service.OrderService;
import com.ecommerce.backend.service.PaymentService;
import com.ecommerce.backend.service.SellerReportService;
import com.ecommerce.backend.service.SellerService;
import com.ecommerce.backend.service.UserService;

class OrderControllerTest {

    @Test
    void shouldFetchUserOrderHistory() throws Exception {
        OrderService orderService = mock(OrderService.class);
        UserService userService = mock(UserService.class);
        CartService cartService = mock(CartService.class);
        SellerService sellerService = mock(SellerService.class);
        PaymentOrderRepository paymentOrderRepository = mock(PaymentOrderRepository.class);
        SellerReportService sellerReportService = mock(SellerReportService.class);
        PaymentService paymentService = mock(PaymentService.class);
        OrderController controller = new OrderController(orderService, userService, cartService, sellerService,
                paymentOrderRepository, sellerReportService, paymentService);

        User user = new User();
        user.setId(14L);
        Order order = new Order();
        order.setId(55L);

        when(userService.findUserByJwtToken("jwt")).thenReturn(user);
        when(orderService.userOrderHistory(14L)).thenReturn(List.of(order));

        ResponseEntity<List<Order>> response = controller.userOrderHistoryHandler("jwt");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }
}
