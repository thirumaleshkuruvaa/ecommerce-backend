package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.Transaction;

public interface TransactionService {

    Transaction createTransaction(Order order)
            throws SellerException;

    List<Transaction> getTransactionsBySellerId(Seller seller);

    List<Transaction> getAllTransactions();

}