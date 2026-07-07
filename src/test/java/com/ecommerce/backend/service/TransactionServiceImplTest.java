package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ecommerce.backend.domain.PaymentStatus;
import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.PaymentDetails;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.Transaction;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.SellerRepository;
import com.ecommerce.backend.repository.TransactionRepository;
import com.ecommerce.backend.service.Impl.TransactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Seller seller;
    private User user;
    private Order order;
    private PaymentDetails paymentDetails;
    private Transaction transaction;

    @BeforeEach
    void setUp() {

        seller = new Seller();
        seller.setId(1L);

        user = new User();
        user.setId(10L);

        paymentDetails = new PaymentDetails();
        paymentDetails.setPaymentId("PAY123");
        paymentDetails.setPaymentStatus(PaymentStatus.SUCCESS);

        order = new Order();
        order.setId(100L);
        order.setSellerId(1L);
        order.setUser(user);
        order.setTotalSellingPrice(1000);
        order.setPaymentDetails(paymentDetails);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSeller(seller);
        transaction.setCustomer(user);
        transaction.setOrder(order);
        transaction.setAmount(1000L);
        transaction.setPaymentId("PAY123");
        transaction.setStatus(PaymentStatus.SUCCESS);
    }

    @Test
    void testCreateTransaction_WhenAlreadyExists() throws SellerException {

        when(transactionRepository.findByOrderId(100L))
                .thenReturn(Optional.of(transaction));

        Transaction result = transactionService.createTransaction(order);

        assertNotNull(result);
        assertEquals(transaction, result);

        verify(transactionRepository).findByOrderId(100L);
    }

    @Test
    void testCreateTransaction_Success() throws SellerException {

        when(transactionRepository.findByOrderId(100L))
                .thenReturn(Optional.empty());

        when(sellerRepository.findById(1L))
                .thenReturn(Optional.of(seller));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.createTransaction(order);

        assertNotNull(result);
        assertEquals(seller, result.getSeller());
        assertEquals(user, result.getCustomer());
        assertEquals(order, result.getOrder());
        assertEquals(1000L, result.getAmount());
        assertEquals("PAY123", result.getPaymentId());
        assertEquals(PaymentStatus.SUCCESS, result.getStatus());

        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_SellerNotFound() {

        when(transactionRepository.findByOrderId(100L))
                .thenReturn(Optional.empty());

        when(sellerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                SellerException.class,
                () -> transactionService.createTransaction(order));

        verify(sellerRepository).findById(1L);
    }

    @Test
    void testGetTransactionsBySellerId() {

        List<Transaction> transactions = Arrays.asList(transaction, new Transaction());

        when(transactionRepository.findBySellerId(1L))
                .thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactionsBySellerId(seller);

        assertEquals(2, result.size());

        verify(transactionRepository).findBySellerId(1L);
    }

    @Test
    void testGetAllTransactions() {

        List<Transaction> transactions = Arrays.asList(transaction, new Transaction());

        when(transactionRepository.findAll())
                .thenReturn(transactions);

        List<Transaction> result = transactionService.getAllTransactions();

        assertEquals(2, result.size());

        verify(transactionRepository).findAll();
    }
}