package com.ecommerce.backend.request;

import com.ecommerce.backend.domain.USER_ROLE;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// @Data
public class LoginOtpRequest {

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    // @NotBlank(message = "OTP is required")
    private String otp;

    @NotNull(message = "Role is required")
    private USER_ROLE role;
}