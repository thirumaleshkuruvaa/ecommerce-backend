package com.ecommerce.backend.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SellerLoginOtpRequest {

    @Email
    @NotBlank
    private String email;
}