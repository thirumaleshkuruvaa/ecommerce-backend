package com.ecommerce.backend.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Embeddable
public class BusinessDetails {

    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotBlank(message = "Business email is required")
    @Email(message = "Invalid business email format")
    private String businessEmail;

    @NotBlank(message = "Business phone number is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Invalid mobile number")
    private String businessPhoneNumber;

    @NotBlank(message = "Business address is required")
    private String businessAddress;

    // optional fields (can be empty or null)
    private String logo;

    private String banner;
}