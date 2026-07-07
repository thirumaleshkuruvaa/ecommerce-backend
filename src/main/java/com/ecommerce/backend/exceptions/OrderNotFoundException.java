package com.ecommerce.backend.exceptions;

public class OrderNotFoundException extends Exception {

    public OrderNotFoundException(String messege) {
        super(messege);
    }
}