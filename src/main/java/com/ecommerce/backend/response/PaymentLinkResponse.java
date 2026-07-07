package com.ecommerce.backend.response;

import lombok.Getter;
import lombok.Setter;

// @Getter
// @Setter
public class PaymentLinkResponse {

    private String paymentLinkId;

    private String paymentLinkUrl;

    public String getPaymentLinkId() {
        return paymentLinkId;
    }

    public void setPaymentLinkId(String paymentLinkId) {
        this.paymentLinkId = paymentLinkId;
    }

    public String getPaymentLinkUrl() {
        return paymentLinkUrl;
    }

    public void setPaymentLinkUrl(String paymentLinkUrl) {
        this.paymentLinkUrl = paymentLinkUrl;
    }

}