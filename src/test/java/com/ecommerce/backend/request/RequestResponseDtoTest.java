package com.ecommerce.backend.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.response.AuthResponse;
import com.ecommerce.backend.response.PaymentLinkResponse;

class RequestResponseDtoTest {

    @Test
    void loginRequestShouldExposeFields() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setOtp("123456");

        assertEquals("user@example.com", request.getEmail());
        assertEquals("123456", request.getOtp());
    }

    @Test
    void signupRequestShouldExposeFields() {
        SignupRequest request = new SignupRequest();
        request.setEmail("user@example.com");
        request.setPassword("secret");
        request.setFullName("Jane Doe");
        request.setOtp("654321");
        request.setPhoneNumber("9876543210");

        assertEquals("user@example.com", request.getEmail());
        assertEquals("secret", request.getPassword());
        assertEquals("Jane Doe", request.getFullName());
        assertEquals("654321", request.getOtp());
        assertEquals("9876543210", request.getPhoneNumber());
    }

    @Test
    void apiResponseShouldMutateAndReadProperties() {
        ApiResponse response = new ApiResponse();
        response.setMessege("ok");
        response.setSuccess(true);

        assertEquals("ok", response.getMessege());
        assertEquals(true, response.isSuccess());
    }

    @Test
    void authResponseShouldMutateAndReadProperties() {
        AuthResponse response = new AuthResponse();
        response.setJwt("token");
        response.setMessege("done");
        response.setRole(com.ecommerce.backend.domain.USER_ROLE.ROLE_CUSTOMER);

        assertEquals("token", response.getJwt());
        assertEquals("done", response.getMessege());
        assertEquals(com.ecommerce.backend.domain.USER_ROLE.ROLE_CUSTOMER, response.getRole());
    }

    @Test
    void paymentLinkResponseShouldMutateAndReadProperties() {
        PaymentLinkResponse response = new PaymentLinkResponse();
        response.setPaymentLinkId("pay_123");
        response.setPaymentLinkUrl("https://example.com/pay");

        assertEquals("pay_123", response.getPaymentLinkId());
        assertEquals("https://example.com/pay", response.getPaymentLinkUrl());
    }

    @Test
    void createAdminRequestShouldExposeFields() {
        com.ecommerce.backend.domain.CreateAdminRequest request = new com.ecommerce.backend.domain.CreateAdminRequest();
        request.setEmail("admin@example.com");
        request.setPassword("admin-pass");

        assertEquals("admin@example.com", request.getEmail());
        assertEquals("admin-pass", request.getPassword());
    }

    @Test
    void defaultValuesShouldBeNullOrFalse() {
        ApiResponse response = new ApiResponse();
        assertNull(response.getMessege());
        assertEquals(false, response.isSuccess());
    }
}
