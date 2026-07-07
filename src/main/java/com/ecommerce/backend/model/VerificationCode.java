package com.ecommerce.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotBlank
    private String otp;

    @Email
    @NotBlank
    private String email;

    private String type; // SIGNUP / LOGIN / VERIFY

    @OneToOne
    private User user;

    @OneToOne
    private Seller seller;
}