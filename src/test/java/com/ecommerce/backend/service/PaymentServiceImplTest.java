package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.backend.domain.PaymentOrderStatus;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.PaymentDetails;
import com.ecommerce.backend.model.PaymentOrder;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.repository.PaymentOrderRepository;
import com.ecommerce.backend.service.Impl.PaymentServiceImpl;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentOrderRepository paymentOrderRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private User user;
    private Order order1;
    private Order order2;
    private PaymentOrder paymentOrder;

    @BeforeEach
    void setUp() throws Exception {

        user = new User();
        user.setId(1L);
        user.setEmail("thirumalesh@gmail.com");

        PaymentDetails paymentDetails = new PaymentDetails();

        order1 = new Order();
        order1.setId(1L);
        order1.setTotalSellingPrice(1000);
        order1.setPaymentDetails(paymentDetails);

        order2 = new Order();
        order2.setId(2L);
        order2.setTotalSellingPrice(500);
        order2.setPaymentDetails(paymentDetails);

        Set<Order> orders = new HashSet<>();
        orders.add(order1);
        orders.add(order2);

        paymentOrder = new PaymentOrder();
        paymentOrder.setId(10L);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);
        paymentOrder.setAmount(1000L);
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);

        // Inject Razorpay credentials
        Field apiKey = PaymentServiceImpl.class.getDeclaredField("apiKey");
        apiKey.setAccessible(true);
        apiKey.set(paymentService, "test_key");

        Field apiSecret = PaymentServiceImpl.class.getDeclaredField("apiSecret");
        apiSecret.setAccessible(true);
        apiSecret.set(paymentService, "test_secret");
    }

    // -------------------------------------------------------
    // CREATE ORDER
    // -------------------------------------------------------

    @Test
    void testCreateOrder() {

        Set<Order> orders = new HashSet<>();
        orders.add(order1);
        orders.add(order2);

        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentOrder result = paymentService.createOrder(user, orders);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(1000L, result.getAmount());
        assertEquals(PaymentOrderStatus.PENDING, result.getStatus());
        assertEquals(1, result.getOrders().size());

        verify(paymentOrderRepository).save(any(PaymentOrder.class));
    }

    // -------------------------------------------------------
    // GET PAYMENT ORDER BY ID - SUCCESS
    // -------------------------------------------------------

    @Test
    void testGetPaymentOrderById() {

        when(paymentOrderRepository.findById(10L))
                .thenReturn(Optional.of(paymentOrder));

        PaymentOrder result = paymentService.getPaymentOrderById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(1000L, result.getAmount());

        verify(paymentOrderRepository).findById(10L);
    }

    // -------------------------------------------------------
    // GET PAYMENT ORDER BY ID - NOT FOUND
    // -------------------------------------------------------

    @Test
    void testGetPaymentOrderById_NotFound() {

        when(paymentOrderRepository.findById(10L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentService.getPaymentOrderById(10L));

        assertEquals(
                "Payment Order Not Found",
                exception.getMessage());

        verify(paymentOrderRepository).findById(10L);
    }

    // -------------------------------------------------------
    // GET PAYMENT ORDER BY PAYMENT LINK ID
    // -------------------------------------------------------

    @Test
    void testGetPaymentOrderByPaymentId() {

        paymentOrder.setPaymentLinkId("plink_123");

        when(paymentOrderRepository.findByPaymentLinkId("plink_123"))
                .thenReturn(paymentOrder);

        PaymentOrder result = paymentService.getPaymentOrderByPaymentId("plink_123");

        assertNotNull(result);
        assertEquals("plink_123", result.getPaymentLinkId());

        verify(paymentOrderRepository)
                .findByPaymentLinkId("plink_123");
    }
    // -------------------------------------------------------
    // CREATE RAZORPAY PAYMENT LINK - EXCEPTION
    // -------------------------------------------------------

    @Test
    void testCreateRazorpayPaymentLink_Exception() {

        /*
         * This method internally creates:
         *
         * new RazorpayClient(apiKey, apiSecret)
         *
         * Since RazorpayClient is created directly inside the service,
         * Mockito cannot mock it.
         *
         * Without valid Razorpay credentials or constructor mocking
         * (Mockito Inline / PowerMockito), the method enters the
         * catch block and throws RuntimeException.
         */

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> paymentService.createRazorpayPaymentLink(
                        user,
                        1500L,
                        10L));

        assertEquals(
                "Unable to create payment link",
                exception.getMessage());
    }

    // -------------------------------------------------------
    // GET PAYMENT ORDER BY PAYMENT LINK ID - NOT FOUND
    // -------------------------------------------------------

    @Test
    void testGetPaymentOrderByPaymentId_NotFound() {

        when(paymentOrderRepository.findByPaymentLinkId("invalid"))
                .thenReturn(null);

        PaymentOrder result = paymentService.getPaymentOrderByPaymentId("invalid");

        assertNull(result);

        verify(paymentOrderRepository)
                .findByPaymentLinkId("invalid");
    }

    // -------------------------------------------------------
    // CREATE ORDER - SINGLE ORDER
    // -------------------------------------------------------

    @Test
    void testCreateOrder_WithSingleOrder() {

        Set<Order> orders = new HashSet<>();
        orders.add(order1);

        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentOrder result = paymentService.createOrder(user, orders);

        assertNotNull(result);
        assertEquals(1000L, result.getAmount());
        assertEquals(1, result.getOrders().size());
        assertEquals(PaymentOrderStatus.PENDING, result.getStatus());

        verify(paymentOrderRepository)
                .save(any(PaymentOrder.class));
    }

    // -------------------------------------------------------
    // CREATE ORDER - EMPTY ORDER SET
    // -------------------------------------------------------

    @Test
    void testCreateOrder_WithEmptyOrders() {

        Set<Order> orders = new HashSet<>();

        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentOrder result = paymentService.createOrder(user, orders);

        assertNotNull(result);
        assertEquals(0L, result.getAmount());
        assertTrue(result.getOrders().isEmpty());

        verify(paymentOrderRepository)
                .save(any(PaymentOrder.class));
    }

    // -------------------------------------------------------
    // VERIFY PAYMENT ORDER DEFAULT STATUS
    // -------------------------------------------------------

    @Test
    void testCreateOrder_DefaultStatus() {

        Set<Order> orders = new HashSet<>();
        orders.add(order1);

        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentOrder result = paymentService.createOrder(user, orders);

        assertEquals(
                PaymentOrderStatus.PENDING,
                result.getStatus());

        verify(paymentOrderRepository)
                .save(any(PaymentOrder.class));
    }

    // -------------------------------------------------------
    // VERIFY PAYMENT ORDER USER
    // -------------------------------------------------------

    @Test
    void testCreateOrder_UserAssigned() {

        Set<Order> orders = new HashSet<>();
        orders.add(order1);

        when(paymentOrderRepository.save(any(PaymentOrder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentOrder result = paymentService.createOrder(user, orders);

        assertEquals(user, result.getUser());

        verify(paymentOrderRepository)
                .save(any(PaymentOrder.class));
    }

    // -------------------------------------------------------
    // PROCEED PAYMENT - ALREADY PROCESSED
    // -------------------------------------------------------

    @Test
    void testProceedPaymentOrder_AlreadyProcessed() {

        paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);

        Boolean result = paymentService.proceedPaymentOrder(
                paymentOrder,
                "pay_123",
                "plink_123");

        assertFalse(result);

        verify(paymentOrderRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }
}