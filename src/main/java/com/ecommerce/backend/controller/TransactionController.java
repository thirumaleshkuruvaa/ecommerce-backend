package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.Transaction;
import com.ecommerce.backend.service.SellerService;
import com.ecommerce.backend.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/transactions")
@Slf4j
@Tag(name = "11. Transactions", description = "Seller and admin transaction history")
public class TransactionController {

    private final TransactionService transactionService;

    private final SellerService sellerService;

    public TransactionController(
            TransactionService transactionService,
            SellerService sellerService) {

        this.transactionService = transactionService;
        this.sellerService = sellerService;
    }

    // GET SELLER TRANSACTIONS
    @Operation(summary = "Get Seller Transactions", description = "Fetch transactions for logged-in seller")

    @GetMapping("/seller")
    public ResponseEntity<List<Transaction>> getTransactionBySeller(

            @RequestHeader("Authorization") String jwt)

            throws SellerException {

        log.info("Get seller transactions API called");

        Seller seller = sellerService.getSellerProfile(jwt);

        log.info("Fetching transactions for Seller ID : {}", seller.getId());

        List<Transaction> transactions = transactionService.getTransactionsBySellerId(seller);

        log.info("Total transactions fetched for seller : {}", transactions.size());

        return ResponseEntity.ok(transactions);
    }

    // GET ALL TRANSACTIONS

    @Operation(summary = "Get All Transactions", description = "Admin view of all transactions")
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {

        log.info("Get all transactions API called");

        List<Transaction> transactions = transactionService.getAllTransactions();

        log.info("Total transactions fetched : {}", transactions.size());

        return ResponseEntity.ok(transactions);
    }

}