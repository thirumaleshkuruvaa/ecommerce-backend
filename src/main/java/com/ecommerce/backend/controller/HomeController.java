package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.model.Home;
import com.ecommerce.backend.model.HomeCategory;
import com.ecommerce.backend.service.HomeCategoryService;
import com.ecommerce.backend.service.HomeService;

@RestController
@RequestMapping("/api")
public class HomeController {

    private final HomeService homeService;
    private final HomeCategoryService homeCategoryService;

    public HomeController(HomeService homeService,
            HomeCategoryService homeCategoryService) {
        this.homeService = homeService;
        this.homeCategoryService = homeCategoryService;
    }

    @GetMapping("/home")
    public ResponseEntity<Home> getHomePageData() {
        List<HomeCategory> categories = homeCategoryService.getAllHomeCategories();
        Home home = homeService.createHomePageData(categories);
        return ResponseEntity.ok(home);
    }
}