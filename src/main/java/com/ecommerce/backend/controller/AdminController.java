package com.ecommerce.backend.controller;

import com.ecommerce.backend.domain.AccountStatus;
import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.service.SellerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@Tag(name = "16. Admin Management", description = "Admin operations like seller approval and system control")
public class AdminController {

    private final SellerService sellerService;

    public AdminController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    // UPDATE SELLER STATUS (ADMIN ONLY)
    @Operation(summary = "Update Seller Status", description = "Approve or block seller account")
    @PatchMapping("/seller/{id}/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seller> updateSellerStatus(
            @PathVariable Long id,
            @PathVariable("status") AccountStatus status)
            throws SellerException {

        log.info("Admin request to update seller {} status to {}", id, status);

        Seller updatedSeller = sellerService.updateSellerAccountStatus(id, status);

        log.info("Seller updated successfully: {} -> {}", updatedSeller.getId(),
                updatedSeller.getAccountStatus());

        return ResponseEntity.ok(updatedSeller);
    }
}