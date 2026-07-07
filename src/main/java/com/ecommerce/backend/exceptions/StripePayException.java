package com.ecommerce.backend.exceptions;

public class StripePayException extends Exception {

    public StripePayException(String messege) {
        super(messege);
    }

}
