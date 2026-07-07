package com.ecommerce.backend.exceptions;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String messege) {
        super(messege);
    }
}
