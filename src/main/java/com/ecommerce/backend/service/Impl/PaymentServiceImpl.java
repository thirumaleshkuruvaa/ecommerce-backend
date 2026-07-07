package com.ecommerce.backend.service.Impl;

import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.backend.domain.PaymentOrderStatus;
import com.ecommerce.backend.domain.PaymentStatus;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.PaymentOrder;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.repository.PaymentOrderRepository;
import com.ecommerce.backend.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

        private final PaymentOrderRepository paymentOrderRepository;
        private final OrderRepository orderRepository;

        @Value("${razorpay.api.key}")
        private String apiKey;

        @Value("${razorpay.api.secret}")
        private String apiSecret;

        public PaymentServiceImpl(
                        PaymentOrderRepository paymentOrderRepository,
                        OrderRepository orderRepository) {

                this.paymentOrderRepository = paymentOrderRepository;
                this.orderRepository = orderRepository;
        }

        @Override
        public PaymentOrder createOrder(User user, Set<Order> orders) {

                Long amount = orders.stream()
                                .mapToLong(Order::getTotalSellingPrice)
                                .sum();

                PaymentOrder paymentOrder = new PaymentOrder();

                paymentOrder.setUser(user);
                paymentOrder.setOrders(orders);
                paymentOrder.setAmount(amount);
                paymentOrder.setStatus(PaymentOrderStatus.PENDING);

                return paymentOrderRepository.save(paymentOrder);
        }

        @Override
        public PaymentOrder getPaymentOrderById(Long id) {

                return paymentOrderRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Payment Order Not Found"));
        }

        @Override
        public PaymentOrder getPaymentOrderByPaymentId(String paymentLinkId) {

                return paymentOrderRepository.findByPaymentLinkId(paymentLinkId);
        }

        @Override
        public PaymentLink createRazorpayPaymentLink(
                        User user,
                        Long amount,
                        Long paymentOrderId) {

                try {

                        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

                        JSONObject request = new JSONObject();

                        request.put("amount", amount * 100);

                        request.put("currency", "INR");

                        JSONObject customer = new JSONObject();

                        customer.put("email", user.getEmail());

                        request.put("customer", customer);

                        request.put("notify",
                                        new JSONObject().put("email", true));

                        request.put(
                                        "callback_url",
                                        "http://localhost:5173/payment-success/" + paymentOrderId);

                        request.put(
                                        "callback_method",
                                        "get");

                        PaymentLink paymentLink = razorpay.paymentLink.create(request);

                        PaymentOrder paymentOrder = getPaymentOrderById(paymentOrderId);

                        paymentOrder.setPaymentLinkId(
                                        paymentLink.get("id"));

                        paymentOrderRepository.save(paymentOrder);

                        return paymentLink;

                }

                catch (Exception e) {

                        throw new RuntimeException(
                                        "Unable to create payment link");
                }
        }

        @Transactional
        @Override
        public Boolean proceedPaymentOrder(
                        PaymentOrder paymentOrder,
                        String paymentId,
                        String paymentLinkId) {

                try {

                        if (paymentOrder.getStatus() != PaymentOrderStatus.PENDING) {

                                return false;
                        }

                        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

                        Payment payment = razorpay.payments.fetch(paymentId);

                        String paymentStatus = payment.get("status");

                        log.info(paymentStatus);

                        if (paymentStatus.equals("captured")
                                        || paymentStatus.equals("authorized")
                                        || paymentStatus.equals("paid")) {

                                paymentOrder.setStatus(
                                                PaymentOrderStatus.SUCCESS);

                                paymentOrderRepository.save(paymentOrder);

                                for (Order order : paymentOrder.getOrders()) {

                                        order.getPaymentDetails()
                                                        .setPaymentId(paymentId);

                                        order.getPaymentDetails()
                                                        .setPaymentStatus(PaymentStatus.SUCCESS);

                                        order.getPaymentDetails()
                                                        .setPaymentLinkId(paymentLinkId);

                                        order.getPaymentDetails()
                                                        .setPaymentLinkStatus("PAID");

                                        orderRepository.save(order);
                                }

                                return true;
                        }

                        paymentOrder.setStatus(PaymentOrderStatus.FAILED);

                        paymentOrderRepository.save(paymentOrder);

                        return false;

                }

                catch (Exception e) {

                        log.error(e.getMessage());

                        paymentOrder.setStatus(PaymentOrderStatus.FAILED);

                        paymentOrderRepository.save(paymentOrder);

                        return false;
                }
        }

}