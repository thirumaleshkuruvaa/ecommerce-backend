package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.exceptions.HomeCategoryException;
import com.ecommerce.backend.model.Home;
import com.ecommerce.backend.model.HomeCategory;
import com.ecommerce.backend.service.HomeCategoryService;
import com.ecommerce.backend.service.HomeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "13. Home Page Management", description = "Home page categories and layout configuration")
public class HomeCategoryController {

    private final HomeService homeService;

    private final HomeCategoryService homeCategoryService;

    public HomeCategoryController(
            HomeService homeService,
            HomeCategoryService homeCategoryService) {

        this.homeService = homeService;
        this.homeCategoryService = homeCategoryService;
    }

    // CREATE HOME CATEGORIES
@Operation(summary = "Create Home Categories", description = "Admin creates home page category sections")

    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategories(
            @RequestBody List<HomeCategory> homeCategories) {

        log.info("Create home categories request received");

        log.info("Total categories received : {}", homeCategories.size());

        List<HomeCategory> categories = homeCategoryService.createHomeCategories(homeCategories);

        log.info("Home categories saved successfully");

        Home home = homeService.createHomePageData(categories);

        log.info("Home page data created successfully");

        return new ResponseEntity<>(
                home,
                HttpStatus.CREATED);
    }

    // GET ALL HOME CATEGORIES
    @Operation(summary = "Get Home Categories", description = "Fetch all home categories for admin")
  
    @GetMapping("/admin/home-categories")
    public ResponseEntity<List<HomeCategory>> getHomeCategories() {

        log.info("Fetching all home categories");

        List<HomeCategory> categories = homeCategoryService.getAllHomeCategories();

        log.info("Total home categories fetched : {}", categories.size());

        return ResponseEntity.ok(categories);
    }

    // UPDATE HOME CATEGORY

    @PatchMapping("/admin/home-categories/{id}")
    @Operation(summary = "Update Home Category", description = "Update category section or details")
    public ResponseEntity<HomeCategory> updateHomeCategory(

            @PathVariable Long id,

            @RequestBody HomeCategory homeCategory)

            throws HomeCategoryException {

        log.info("Update home category request received for categoryId : {}", id);

        HomeCategory updatedCategory = homeCategoryService.updateHomeCategory(homeCategory, id);

        log.info("Home category updated successfully with id : {}", updatedCategory.getId());

        return ResponseEntity.ok(updatedCategory);
    }
}