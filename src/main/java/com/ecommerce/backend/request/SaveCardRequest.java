package com.ecommerce.backend.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveCardRequest {

    private String cardHolderName;
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cardType;
}