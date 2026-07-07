package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.Transaction;
import com.ecommerce.backend.service.SellerService;
import com.ecommerce.backend.service.TransactionService;

class TransactionControllerTest {

    @Test
    void shouldFetchTransactions() throws Exception {
        TransactionService transactionService = mock(TransactionService.class);
        SellerService sellerService = mock(SellerService.class);
        TransactionController controller = new TransactionController(transactionService, sellerService);

        Seller seller = new Seller();
        seller.setId(12L);
        Transaction transaction = new Transaction();
        transaction.setId(7L);

        when(sellerService.getSellerProfile("jwt")).thenReturn(seller);
        when(transactionService.getTransactionsBySellerId(seller)).thenReturn(List.of(transaction));

        ResponseEntity<List<Transaction>> response = controller.getTransactionBySeller("jwt");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }
}
