package com.ecommerce.backend.domain;

import lombok.Data;

@Data
public class AdminLoginRequest {

    private String email;

    private String password;

}
