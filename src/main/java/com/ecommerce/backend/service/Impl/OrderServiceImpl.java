package com.ecommerce.backend.service.Impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.domain.OrderStatus;
import com.ecommerce.backend.domain.PaymentStatus;
import com.ecommerce.backend.exceptions.OrderException;
import com.ecommerce.backend.exceptions.OrderNotFoundException;
import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.exceptions.UnauthorizedAcessException;
import com.ecommerce.backend.model.*;
import com.ecommerce.backend.repository.*;
import com.ecommerce.backend.service.OrderService;
import com.ecommerce.backend.service.SellerReportService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

        private final OrderRepository orderRepository;
        private final OrderItemRepository orderItemRepository;
        private final AddressRepository addressRepository;
        private final CartRepository cartRepository;
        private final SellerReportService sellerReportService;

        public OrderServiceImpl(
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        AddressRepository addressRepository,
                        CartRepository cartRepository, SellerReportService sellerReportService) {

                this.orderRepository = orderRepository;
                this.orderItemRepository = orderItemRepository;
                this.addressRepository = addressRepository;
                this.cartRepository = cartRepository;
                this.sellerReportService = sellerReportService;
        }

        @Override
        public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {

                log.info("Order creation started for userId {}", user.getId());

                Address address = addressRepository.save(shippingAddress);

                Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems()
                                .stream()
                                .collect(Collectors.groupingBy(
                                                item -> item.getProduct().getSeller().getId()));

                Set<Order> orders = new HashSet<>();

                for (Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()) {

                        Long sellerId = entry.getKey();
                        List<CartItem> items = entry.getValue();

                        int totalMrpPrice = items.stream()
                                        .mapToInt(item -> item.getMrpPrice() * item.getQuantity())
                                        .sum();

                        int totalSellingPrice = items.stream()
                                        .mapToInt(item -> item.getSellingPrice() * item.getQuantity())
                                        .sum();

                        int totalItems = items.stream()
                                        .mapToInt(CartItem::getQuantity)
                                        .sum();

                        Order order = new Order();
                        order.setUser(user);
                        order.setSellerId(sellerId);
                        order.setShippingAddress(address);
                        order.setTotalMrpPrice(totalMrpPrice);
                        order.setTotalSellingPrice(totalSellingPrice);
                        order.setTotalItem(totalItems);
                        order.setOrderStatus(OrderStatus.PENDING);
                        order.setOrderId(UUID.randomUUID().toString());

                        PaymentDetails paymentDetails = new PaymentDetails();
                        paymentDetails.setPaymentStatus(PaymentStatus.PENDING);
                        order.setPaymentDetails(paymentDetails);

                        Order savedOrder = orderRepository.save(order);

                        List<OrderItem> orderItems = new ArrayList<>();

                        for (CartItem item : items) {

                                OrderItem orderItem = new OrderItem();
                                orderItem.setOrder(savedOrder);
                                orderItem.setProduct(item.getProduct());
                                orderItem.setQuantity(item.getQuantity());
                                orderItem.setSizes(new ArrayList<>(item.getSizes()));
                                orderItem.setMrpPrice(item.getMrpPrice());
                                orderItem.setSellingPrice(item.getSellingPrice());
                                orderItem.setUserId(user.getId());

                                orderItemRepository.save(orderItem);

                                orderItems.add(orderItem);
                        }

                        // ONLY ADD ITEMS (DO NOT REPLACE COLLECTION)
                        savedOrder.getOrderItems().addAll(orderItems);

                        orders.add(savedOrder);
                }

                cart.getCartItems().clear();
                cartRepository.save(cart);

                log.info("Order creation completed for userId {}", user.getId());

                return orders;
        }

        @Override
        public Order findOrderById(Long id) throws OrderNotFoundException {
                return orderRepository.findById(id)
                                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        }

        @Override
        public OrderItem getOrderItemById(Long id) throws OrderException {
                return orderItemRepository.findById(id)
                                .orElseThrow(() -> new OrderException("Order item not found"));
        }

        @Override
        public List<Order> userOrderHistory(Long userId) {
                return orderRepository.findByUserId(userId);
        }
        // @Override
        // public List<Order> userOrderHistory(Long userId) {

        // return orderRepository.findByUserIdAndArchivedFalse(userId);

        // }

        @Override
        public List<Order> archivedOrders(Long userId) {

                return orderRepository.findByUser_IdAndArchivedTrue(userId);

        }

        @Override
        public Order archiveOrder(Long orderId, User user)
                        throws UnauthorizedAcessException,
                        OrderNotFoundException {

                Order order = findOrderById(orderId);

                if (!order.getUser().getId().equals(user.getId())) {
                        throw new UnauthorizedAcessException("No access");
                }

                order.setArchived(true);

                return orderRepository.save(order);
        }

        @Override
        public Order unArchiveOrder(Long orderId, User user)
                        throws UnauthorizedAcessException,
                        OrderNotFoundException {

                Order order = findOrderById(orderId);

                if (!order.getUser().getId().equals(user.getId())) {
                        throw new UnauthorizedAcessException("No access");
                }

                order.setArchived(false);

                return orderRepository.save(order);
        }

        @Override
        public List<Order> sellersOrder(Long sellerId) {
                return orderRepository.findBySellerId(sellerId);
        }

        // Update Order Status
        @Override
        public Order updateOrderStatus(Long orderId,
                        Long sellerId,
                        OrderStatus orderStatus)
                        throws OrderNotFoundException,
                        UnauthorizedAcessException,
                        SellerException {

                Order order = findOrderById(orderId);

                if (!order.getSellerId().equals(sellerId)) {
                        throw new UnauthorizedAcessException("Not authorized");
                }

                OrderStatus previousStatus = order.getOrderStatus();

                order.setOrderStatus(orderStatus);

                Order savedOrder = orderRepository.save(order);

                /*
                 * Seller report is updated ONLY when
                 * payment is successful and order becomes DELIVERED.
                 */
                if (orderStatus == OrderStatus.DELIVERED
                                && previousStatus != OrderStatus.DELIVERED) {

                        sellerReportService.updateAfterSuccessfulPayment(savedOrder);
                }

                // Reverse seller report only once.

                if (orderStatus == OrderStatus.CANCELLED
                                && previousStatus != OrderStatus.CANCELLED) {

                        sellerReportService.updateAfterCancelledOrder(savedOrder);
                }

                return savedOrder;
        }

        @Override
        public Order cancelOrder(Long orderId, User user)
                        throws OrderNotFoundException,
                        UnauthorizedAcessException,
                        SellerException {

                Order order = findOrderById(orderId);

                if (!user.getId().equals(order.getUser().getId())) {
                        throw new UnauthorizedAcessException("No access");
                }

                if (order.getOrderStatus() == OrderStatus.CANCELLED) {
                        return order;
                }

                order.setOrderStatus(OrderStatus.CANCELLED);

                Order savedOrder = orderRepository.save(order);

                // Only reverse report if payment was actually successful.

                if (savedOrder.getPaymentDetails().getPaymentStatus() == PaymentStatus.SUCCESS) {
                        sellerReportService.updateAfterCancelledOrder(savedOrder);
                }

                return savedOrder;
        }

}