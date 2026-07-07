package com.ecommerce.backend.model;

import com.ecommerce.backend.domain.PaymentStatus;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class PaymentDetails {

    private String paymentId;

    private String paymentLinkId;

    private String paymentReferenceId;

    private String paymentLinkStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

}