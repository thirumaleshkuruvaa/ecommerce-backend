package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.exceptions.DealException;
import com.ecommerce.backend.model.Deal;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.DealService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/deals")
@Tag(name = "14. Deals Management", description = "Manage product deals and offers")
public class DealController {

    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    // CREATE DEAL
    @Operation(summary = "Create Deal", description = "Admin creates a deal for category")

    @PostMapping("/admin/create")
    public ResponseEntity<Deal> createDeal(
            @RequestBody Deal deal)
            throws DealException {

        log.info("Create deal request received for categoryId:{}", deal.getCategory().getId());

        Deal createdDeal = dealService.createDeal(deal);

        log.info("Deal created successfully with id:{}", createdDeal.getId());

        return new ResponseEntity<>(
                createdDeal,
                HttpStatus.CREATED);
    }

    // GET ALL DEALS
    @Operation(summary = "Get All Deals", description = "Fetch all active deals")

    @GetMapping
    public ResponseEntity<List<Deal>> getAllDeals() {

        log.info("Fetching all deals ");

        List<Deal> deals = dealService.getDeal();

        log.info("Total deals fetch:{}", deals.size());

        return ResponseEntity.ok(deals);
    }

    // UPDATE DEAL
    @Operation(summary = "Update Deal", description = "Update deal details")

    @PatchMapping("/admin/{id}")
    public ResponseEntity<Deal> updateDeal(

            @RequestBody Deal deal,

            @PathVariable Long id)

            throws DealException {

        log.info("Update deal request received for dealId:{}", id);

        Deal updatedDeal = dealService.updateDeal(deal, id);

        log.info("Deal updated succcefully with id :{}", updatedDeal.getId());

        return ResponseEntity.ok(updatedDeal);
    }

    // DELETE DEAL
    @Operation(summary = "Delete Deal", description = "Remove deal")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse> deleteDeal(

            @PathVariable Long id)

            throws DealException {

        log.info("Delete deal request received for dealId:{}", id);

        dealService.deleteDeal(id);

        log.info("Deal deleted successfully with id : {}", id);

        ApiResponse response = new ApiResponse();

        response.setMessege("Deal deleted successfully");

        // response.setStatus(true);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK);
    }

}