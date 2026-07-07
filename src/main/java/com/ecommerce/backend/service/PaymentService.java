package com.ecommerce.backend.service;

import java.util.Set;

import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.PaymentOrder;
import com.ecommerce.backend.model.User;
import com.razorpay.PaymentLink;

public interface PaymentService {

        PaymentOrder createOrder(User user, Set<Order> orders);

        PaymentOrder getPaymentOrderById(Long id);

        PaymentOrder getPaymentOrderByPaymentId(String paymentLinkId);

        Boolean proceedPaymentOrder(
                        PaymentOrder paymentOrder,
                        String paymentId,
                        String paymentLinkId);

        PaymentLink createRazorpayPaymentLink(
                        User user,
                        Long amount,
                        Long paymentOrderId);
}