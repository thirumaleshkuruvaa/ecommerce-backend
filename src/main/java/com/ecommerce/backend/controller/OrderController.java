package com.ecommerce.backend.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.domain.PaymentMethod;
import com.ecommerce.backend.exceptions.OrderException;
import com.ecommerce.backend.exceptions.OrderNotFoundException;
import com.ecommerce.backend.exceptions.PaymentException;
import com.ecommerce.backend.exceptions.RazorePayException;
import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.exceptions.StripePayException;
import com.ecommerce.backend.exceptions.UnauthorizedAcessException;
import com.ecommerce.backend.exceptions.UserException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.OrderItem;
import com.ecommerce.backend.model.PaymentOrder;

import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.PaymentOrderRepository;
import com.ecommerce.backend.response.PaymentLinkResponse;
import com.ecommerce.backend.service.CartService;
import com.ecommerce.backend.service.OrderService;
import com.ecommerce.backend.service.PaymentService;
import com.ecommerce.backend.service.SellerReportService;
import com.ecommerce.backend.service.SellerService;
import com.ecommerce.backend.service.UserService;
import com.razorpay.PaymentLink;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@Tag(name = "6. Order Management", description = "Order creation, history, cancellation, tracking")
public class OrderController {

        private final OrderService orderService;

        private final SellerService sellerService;

        private final SellerReportService sellerReportService;

        private final UserService userService;

        private final PaymentService paymentService;

        private final CartService cartService;

        private final PaymentOrderRepository paymentOrderRepository;

        public OrderController(
                        OrderService orderService,
                        UserService userService,
                        CartService cartService,
                        SellerService sellerService,
                        PaymentOrderRepository paymentOrderRepository,
                        SellerReportService sellerReportService,
                        PaymentService paymentService) {

                this.orderService = orderService;
                this.userService = userService;
                this.cartService = cartService;
                this.sellerService = sellerService;
                this.paymentService = paymentService;
                this.sellerReportService = sellerReportService;
                this.paymentOrderRepository = paymentOrderRepository;
        }

        // CREATE ORDER

        @PostMapping
        public ResponseEntity<PaymentLinkResponse> createOrderHandler(
                        @RequestBody Address shippingAddress,
                        @RequestParam PaymentMethod paymentMethod,
                        @RequestHeader("Authorization") String jwt)
                        throws UserException, RazorePayException, StripePayException, PaymentException {

                log.info("Create order request received with payment method : {}", paymentMethod);

                User user = userService.findUserByJwtToken(jwt);
                Cart cart = cartService.findUserCart(user);

                Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);

                PaymentOrder paymentOrder = paymentService.createOrder(user, orders);

                PaymentLinkResponse response = new PaymentLinkResponse();

                if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
                        PaymentLink paymentLink = paymentService.createRazorpayPaymentLink(
                                        user,
                                        paymentOrder.getAmount(),
                                        paymentOrder.getId());
                        //
                        response.setPaymentLinkUrl(paymentLink.get("short_url"));
                        response.setPaymentLinkId(paymentLink.get("id"));

                        paymentOrder.setPaymentLinkId(paymentLink.get("id"));
                        paymentOrderRepository.save(paymentOrder);
                }

                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        // USER ORDER HISTORY
        @Operation(summary = "Get User Orders", description = "Fetch order history of logged-in user")

        @GetMapping("/user")
        public ResponseEntity<List<Order>> userOrderHistoryHandler(

                        @RequestHeader("Authorization") String jwt)

                        throws UserException {

                log.info("Fetching user order history");

                User user = userService.findUserByJwtToken(jwt);

                List<Order> orders = orderService.userOrderHistory(user.getId());

                log.info("Fetched {} orders for user id : {}", orders.size(), user.getId());

                return new ResponseEntity<>(
                                orders,
                                HttpStatus.OK);
        }

        // GET ORDER BY ID
        @Operation(summary = "Get Order By ID", description = "Fetch order details by ID")

        @GetMapping("/{orderId}")
        public ResponseEntity<Order> getOrderByIdHandler(

                        @PathVariable Long orderId,

                        @RequestHeader("Authorization") String jwt)

                        throws UserException,
                        OrderNotFoundException {

                log.info("Fetching order with id : {}", orderId);

                userService.findUserByJwtToken(jwt);

                Order order = orderService.findOrderById(orderId);

                log.info("Order fetched successfully : {}", orderId);

                return new ResponseEntity<>(order, HttpStatus.OK);
        }

        // GET ORDER ITEM BY ID
        @Operation(summary = "Get Order Item", description = "Fetch specific order item details")

        @GetMapping("/item/{orderItemId}")
        public ResponseEntity<OrderItem> getOrderItemByIdHandler(

                        @PathVariable Long orderItemId,

                        @RequestHeader("Authorization") String jwt)

                        throws UserException,
                        OrderException,
                        OrderNotFoundException {

                log.info("Fetching order item with id : {}", orderItemId);

                userService.findUserByJwtToken(jwt);

                OrderItem orderItem = orderService.getOrderItemById(orderItemId);

                log.info("Order item fetched successfully : {}", orderItemId);

                return new ResponseEntity<>(orderItem, HttpStatus.OK);
        }

        @Operation(summary = "Cancel Order", description = "Cancel user order")
        @PutMapping("/{orderId}/cancel")
        public ResponseEntity<Order> cancelOrderHandler(

                        @PathVariable Long orderId,

                        @RequestHeader("Authorization") String jwt)

                        throws UserException,
                        OrderException,
                        SellerException,
                        UnauthorizedAcessException,
                        OrderNotFoundException {

                log.info("Cancel order request received for order id : {}", orderId);

                User user = userService.findUserByJwtToken(jwt);

                Order order = orderService.cancelOrder(orderId, user);

                log.info("Order cancelled successfully : {}", orderId);

                return ResponseEntity.ok(order);
        }

        @Operation(summary = "Archive Order", description = "Archive completed/cancelled order")
        @PutMapping("/{orderId}/archive")
        public ResponseEntity<Order> archiveOrderHandler(

                        @PathVariable Long orderId,

                        @RequestHeader("Authorization") String jwt)

                        throws UserException,
                        UnauthorizedAcessException,
                        OrderNotFoundException {

                log.info("Archive request received for order : {}", orderId);

                User user = userService.findUserByJwtToken(jwt);

                Order order = orderService.archiveOrder(orderId, user);

                log.info("Order archived successfully : {}", orderId);

                return ResponseEntity.ok(order);
        }

        @Operation(summary = "Unarchive Order", description = "Move archived order back to My Orders")
        @PutMapping("/{orderId}/unarchive")
        public ResponseEntity<Order> unArchiveOrderHandler(

                        @PathVariable Long orderId,

                        @RequestHeader("Authorization") String jwt)

                        throws UserException,
                        UnauthorizedAcessException,
                        OrderNotFoundException {

                log.info("Unarchive request received for order : {}", orderId);

                User user = userService.findUserByJwtToken(jwt);

                Order order = orderService.unArchiveOrder(orderId, user);

                log.info("Order unarchived successfully : {}", orderId);

                return ResponseEntity.ok(order);
        }

        @Operation(summary = "Archived Orders", description = "Fetch archived orders")
        @GetMapping("/archive")
        public ResponseEntity<List<Order>> archivedOrdersHandler(

                        @RequestHeader("Authorization") String jwt)

                        throws UserException {

                log.info("Fetching archived orders");

                User user = userService.findUserByJwtToken(jwt);

                List<Order> orders = orderService.archivedOrders(user.getId());

                log.info("Archived orders fetched : {}", orders.size());

                return ResponseEntity.ok(orders);
        }
}