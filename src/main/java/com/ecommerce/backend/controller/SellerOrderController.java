package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.domain.OrderStatus;
import com.ecommerce.backend.exceptions.OrderNotFoundException;
import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.exceptions.UnauthorizedAcessException;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.service.OrderService;
import com.ecommerce.backend.service.SellerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/sellers/orders")
@Tag(name = "10. Seller Orders", description = "Seller order management and status updates")
public class SellerOrderController {

    private final OrderService orderService;

    private final SellerService sellerService;

    public SellerOrderController(
            SellerService sellerService,
            OrderService orderService) {

        this.sellerService = sellerService;
        this.orderService = orderService;
    }

    // GET SELLER ORDERS
    @Operation(summary = "Get Seller Orders", description = "Fetch all orders for seller")

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrderHandler(
            @RequestHeader("Authorization") String jwt)
            throws SellerException {

        log.info("Fetching seller orders");

        Seller seller = sellerService.getSellerProfile(jwt);

        log.info("Authenticated seller with id : {}", seller.getId());

        List<Order> orders = orderService.sellersOrder(seller.getId());

        log.info("Successfully fetched {} orders for seller id : {}", orders.size(), seller.getId());

        return ResponseEntity.ok(orders);
    }

    // UPDATE ORDER STATUS
    @Operation(summary = "Update Order Status", description = "Update order status (SHIPPED, DELIVERED, etc.)")
    @PutMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderHandler(
            @RequestHeader("Authorization") String jwt,

            @PathVariable Long orderId,

            @PathVariable OrderStatus orderStatus)

            throws SellerException,
            OrderNotFoundException,
            UnauthorizedAcessException {

        log.info("Updating order status for orderId : {} to status : {}", orderId, orderStatus);

        Seller seller = sellerService.getSellerProfile(jwt);

        log.info("Authenticated seller with id : {}", seller.getId());

        Order order = orderService.updateOrderStatus(orderId, seller.getId(), orderStatus);

        log.info("Order status updated successfully for orderId : {}", orderId);

        return ResponseEntity.ok(order);
    }
}