package com.ecommerce.backend.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.Transaction;
import com.ecommerce.backend.repository.SellerRepository;
import com.ecommerce.backend.repository.TransactionRepository;
import com.ecommerce.backend.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

        private final TransactionRepository transactionRepository;

        private final SellerRepository sellerRepository;

        public TransactionServiceImpl(
                        TransactionRepository transactionRepository,
                        SellerRepository sellerRepository) {

                this.transactionRepository = transactionRepository;
                this.sellerRepository = sellerRepository;
        }

        // CREATE TRANSACTION
        @Override
        public Transaction createTransaction(Order order)
                        throws SellerException {

                // Already exists?
                Transaction existing = transactionRepository
                                .findByOrderId(order.getId())
                                .orElse(null);

                if (existing != null) {
                        return existing;
                }

                Seller seller = sellerRepository
                                .findById(order.getSellerId())
                                .orElseThrow(() -> new SellerException("Seller not found"));

                Transaction transaction = new Transaction();

                transaction.setSeller(seller);

                transaction.setCustomer(order.getUser());

                transaction.setOrder(order);

                transaction.setAmount(
                                Long.valueOf(order.getTotalSellingPrice()));

                transaction.setPaymentId(
                                order.getPaymentDetails().getPaymentId());

                transaction.setStatus(
                                order.getPaymentDetails().getPaymentStatus());

                return transactionRepository.save(transaction);
        }
        // GET SELLER TRANSACTIONS

        @Override
        public List<Transaction> getTransactionsBySellerId(
                        Seller seller) {

                log.info(
                                "Fetching transactions for sellerId : {}",
                                seller.getId());

                List<Transaction> transactions = transactionRepository.findBySellerId(
                                seller.getId());

                log.info(
                                "Total transactions found : {} for sellerId : {}",
                                transactions.size(),
                                seller.getId());

                return transactions;
        }

        // GET ALL TRANSACTIONS

        @Override
        public List<Transaction> getAllTransactions() {

                log.info("Fetching all transactions");

                List<Transaction> transactions = transactionRepository.findAll();

                log.info(
                                "Total transactions fetched : {}",
                                transactions.size());

                return transactions;
        }
}