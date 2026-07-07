package com.ecommerce.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.PaymentOrder;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.PaymentService;
import com.ecommerce.backend.service.SellerReportService;
import com.ecommerce.backend.service.TransactionService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/payment")
@Tag(name = "Payment")
public class PaymentController {

        private final PaymentService paymentService;
        private final TransactionService transactionService;
        private final SellerReportService sellerReportService;

        public PaymentController(
                        PaymentService paymentService,
                        TransactionService transactionService,
                        SellerReportService sellerReportService) {

                this.paymentService = paymentService;
                this.transactionService = transactionService;
                this.sellerReportService = sellerReportService;
        }

        @GetMapping("/{paymentId}")
        public ResponseEntity<ApiResponse> paymentSuccess(

                        @PathVariable String paymentId,

                        @RequestParam String paymentLinkId)

                        throws Exception {

                PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

                boolean success = paymentService.proceedPaymentOrder(
                                paymentOrder,
                                paymentId,
                                paymentLinkId);

                if (success) {

                        for (Order order : paymentOrder.getOrders()) {

                                transactionService.createTransaction(order);

                                sellerReportService.updateAfterSuccessfulPayment(order);
                        }
                }

                ApiResponse response = new ApiResponse();

                response.setSuccess(success);

                response.setMessege(
                                success
                                                ? "Payment Success"
                                                : "Payment Failed");

                return ResponseEntity.ok(response);
        }

}