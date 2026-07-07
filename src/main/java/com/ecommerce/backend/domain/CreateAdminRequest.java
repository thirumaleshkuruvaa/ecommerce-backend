package com.ecommerce.backend.domain;

import lombok.Data;

@Data
public class CreateAdminRequest {

    private String email;

    private String password;
}