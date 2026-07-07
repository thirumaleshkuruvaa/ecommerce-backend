package com.ecommerce.backend.exceptions;

public class UnauthorizedAcessException extends Exception {

    public UnauthorizedAcessException(String messege) {
        super(messege);
    }
}
