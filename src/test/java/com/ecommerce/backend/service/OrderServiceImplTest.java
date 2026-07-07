package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.backend.domain.OrderStatus;
import com.ecommerce.backend.domain.PaymentStatus;
import com.ecommerce.backend.exceptions.OrderException;
import com.ecommerce.backend.exceptions.OrderNotFoundException;
import com.ecommerce.backend.exceptions.UnauthorizedAcessException;
import com.ecommerce.backend.model.*;
import com.ecommerce.backend.repository.*;
import com.ecommerce.backend.service.Impl.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

        @Mock
        private OrderRepository orderRepository;

        @Mock
        private OrderItemRepository orderItemRepository;

        @Mock
        private AddressRepository addressRepository;

        @Mock
        private CartRepository cartRepository;

        @Mock
        private SellerReportService sellerReportService;

        @InjectMocks
        private OrderServiceImpl orderService;

        private User user;
        private Address address;
        private Cart cart;
        private Product product;
        private Seller seller;
        private CartItem cartItem;
        private Order order;
        private OrderItem orderItem;

        @BeforeEach
        void setup() {

                user = new User();
                user.setId(1L);

                address = new Address();

                seller = new Seller();
                seller.setId(5L);

                product = new Product();
                product.setSeller(seller);

                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(2);
                cartItem.setMrpPrice(1000);
                cartItem.setSellingPrice(900);
                cartItem.setSizes(List.of("M"));

                cart = new Cart();
                cart.getCartItems().add(cartItem);

                order = new Order();
                order.setId(1L);
                order.setSellerId(5L);
                order.setUser(user);

                PaymentDetails payment = new PaymentDetails();
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                order.setPaymentDetails(payment);

                orderItem = new OrderItem();
                orderItem.setId(1L);
        }

        @Test
        void testCreateOrder() {

                // Address
                when(addressRepository.save(any(Address.class)))
                                .thenReturn(address);

                // Order
                when(orderRepository.save(any(Order.class)))
                                .thenAnswer(invocation -> {
                                        Order order = invocation.getArgument(0);
                                        order.setId(1L);
                                        return order;
                                });

                // Order Item
                when(orderItemRepository.save(any(OrderItem.class)))
                                .thenAnswer(invocation -> {
                                        OrderItem item = invocation.getArgument(0);
                                        item.setId(1L);
                                        return item;
                                });

                when(cartRepository.save(any(Cart.class)))
                                .thenReturn(cart);

                // execute
                Set<Order> orders = orderService.createOrder(user, address, cart);

                // verify
                assertNotNull(orders);
                assertEquals(1, orders.size());

                Order savedOrder = orders.iterator().next();

                assertEquals(user, savedOrder.getUser());
                assertEquals(seller.getId(), savedOrder.getSellerId());
                assertEquals(OrderStatus.PENDING, savedOrder.getOrderStatus());

                verify(addressRepository, times(1))
                                .save(any(Address.class));

                verify(orderRepository, times(1))
                                .save(any(Order.class));

                verify(orderItemRepository, times(1))
                                .save(any(OrderItem.class));

                verify(cartRepository, times(1))
                                .save(any(Cart.class));

                assertTrue(cart.getCartItems().isEmpty());
        }

        @Test
        void testFindOrderById() throws Exception {

                when(orderRepository.findById(1L))
                                .thenReturn(Optional.of(order));

                Order found = orderService.findOrderById(1L);

                assertEquals(order, found);
        }

        @Test
        void testFindOrderByIdThrowsException() {

                when(orderRepository.findById(1L))
                                .thenReturn(Optional.empty());

                assertThrows(OrderNotFoundException.class,
                                () -> orderService.findOrderById(1L));
        }

        @Test
        void testGetOrderItemById() throws Exception {

                when(orderItemRepository.findById(1L))
                                .thenReturn(Optional.of(orderItem));

                OrderItem result = orderService.getOrderItemById(1L);

                assertEquals(orderItem, result);
        }

        @Test
        void testGetOrderItemThrowsException() {

                when(orderItemRepository.findById(1L))
                                .thenReturn(Optional.empty());

                assertThrows(OrderException.class,
                                () -> orderService.getOrderItemById(1L));
        }

        @Test
        void testArchiveOrder() throws Exception {

                when(orderRepository.findById(1L))
                                .thenReturn(Optional.of(order));

                when(orderRepository.save(any()))
                                .thenReturn(order);

                Order archived = orderService.archiveOrder(1L, user);

                assertTrue(archived.isArchived());

                verify(orderRepository).save(order);
        }

        @Test
        void testArchiveOrderUnauthorized() {

                User another = new User();
                another.setId(20L);

                when(orderRepository.findById(1L))
                                .thenReturn(Optional.of(order));

                assertThrows(
                                UnauthorizedAcessException.class,
                                () -> orderService.archiveOrder(1L, another));
        }

        @Test
        void testUnArchiveOrder() throws Exception {

                order.setArchived(true);

                when(orderRepository.findById(1L))
                                .thenReturn(Optional.of(order));

                when(orderRepository.save(any()))
                                .thenReturn(order);

                Order result = orderService.unArchiveOrder(1L, user);

                assertFalse(result.isArchived());
        }

        @Test
        void testUpdateOrderStatusDelivered() throws Exception {

                order.setOrderStatus(OrderStatus.PENDING);

                when(orderRepository.findById(1L))
                                .thenReturn(Optional.of(order));

                when(orderRepository.save(any()))
                                .thenReturn(order);

                Order updated = orderService.updateOrderStatus(
                                1L,
                                5L,
                                OrderStatus.DELIVERED);

                assertEquals(OrderStatus.DELIVERED,
                                updated.getOrderStatus());

                verify(sellerReportService)
                                .updateAfterSuccessfulPayment(any(Order.class));
        }

        @Test
        void testUpdateOrderStatusCancelled() throws Exception {

                order.setOrderStatus(OrderStatus.PENDING);

                when(orderRepository.findById(1L))
                                .thenReturn(Optional.of(order));

                when(orderRepository.save(any()))
                                .thenReturn(order);

                orderService.updateOrderStatus(
                                1L,
                                5L,
                                OrderStatus.CANCELLED);

                verify(sellerReportService)
                                .updateAfterCancelledOrder(any(Order.class));
        }

        @Test
        void testUpdateOrderUnauthorized() {

                when(orderRepository.findById(1L))
                                .thenReturn(Optional.of(order));

                assertThrows(
                                UnauthorizedAcessException.class,
                                () -> orderService.updateOrderStatus(
                                                1L,
                                                99L,
                                                OrderStatus.DELIVERED));
        }

        @Test
        void testCancelOrder() throws Exception {

                when(orderRepository.findById(1L))
                                .thenReturn(Optional.of(order));

                when(orderRepository.save(any()))
                                .thenReturn(order);

                Order cancelled = orderService.cancelOrder(1L, user);

                assertEquals(OrderStatus.CANCELLED,
                                cancelled.getOrderStatus());

                verify(sellerReportService)
                                .updateAfterCancelledOrder(any(Order.class));
        }

        @Test
        void testCancelOrderUnauthorized() {

                User another = new User();
                another.setId(20L);

                when(orderRepository.findById(1L))
                                .thenReturn(Optional.of(order));

                assertThrows(
                                UnauthorizedAcessException.class,
                                () -> orderService.cancelOrder(1L, another));
        }

        @Test
        void testUserOrderHistory() {

                when(orderRepository.findByUserId(1L))
                                .thenReturn(List.of(order));

                List<Order> list = orderService.userOrderHistory(1L);

                assertEquals(1, list.size());
        }

        @Test
        void testSellerOrders() {

                when(orderRepository.findBySellerId(5L))
                                .thenReturn(List.of(order));

                List<Order> list = orderService.sellersOrder(5L);

                assertEquals(1, list.size());
        }
}