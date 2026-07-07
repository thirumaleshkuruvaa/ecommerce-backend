package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.PaymentOrder;
import com.ecommerce.backend.service.PaymentService;
import com.ecommerce.backend.service.SellerReportService;
import com.ecommerce.backend.service.TransactionService;

class PaymentControllerTest {

    @Test
    void shouldHandlePaymentResult() throws Exception {
        PaymentService paymentService = mock(PaymentService.class);
        TransactionService transactionService = mock(TransactionService.class);
        SellerReportService sellerReportService = mock(SellerReportService.class);
        PaymentController controller = new PaymentController(paymentService, transactionService, sellerReportService);

        PaymentOrder paymentOrder = new PaymentOrder();
        Order order = new Order();
        order.setId(33L);
        paymentOrder.setOrders(Set.of(order));

        when(paymentService.getPaymentOrderByPaymentId("link-1")).thenReturn(paymentOrder);
        when(paymentService.proceedPaymentOrder(paymentOrder, "pay-1", "link-1")).thenReturn(true);

        ResponseEntity<?> response = controller.paymentSuccess("pay-1", "link-1");

        assertEquals(200, response.getStatusCode().value());
    }
}
