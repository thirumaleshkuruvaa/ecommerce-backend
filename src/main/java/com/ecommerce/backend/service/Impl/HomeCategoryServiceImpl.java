package com.ecommerce.backend.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.exceptions.HomeCategoryException;
import com.ecommerce.backend.model.HomeCategory;
import com.ecommerce.backend.repository.HomeCategoryRepository;
import com.ecommerce.backend.service.HomeCategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HomeCategoryServiceImpl implements HomeCategoryService {

    private final HomeCategoryRepository homeCategoryRepository;

    public HomeCategoryServiceImpl(HomeCategoryRepository homeCategoryRepository) {
        this.homeCategoryRepository = homeCategoryRepository;
    }

    // CREATE SINGLE HOME CATEGORY
    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        log.info("Creating home category with name {}", homeCategory.getName());

        HomeCategory savedCategory = homeCategoryRepository.save(homeCategory);

        log.info("Home category created successfully");

        return savedCategory;
    }

    // CREATE MULTIPLE HOME CATEGORIES
    @Override
    public List<HomeCategory> createHomeCategories(List<HomeCategory> homeCategories) {
        log.info("Creating multiple home categories");

        List<HomeCategory> savedCategories = homeCategoryRepository.saveAll(homeCategories);

        log.info("Multiple home categories created successfully");

        return savedCategories;
    }

    // UPDATE HOME CATEGORY
    @Override
    public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id)
            throws HomeCategoryException {

        log.info("Updating home category with id {}", id);

        HomeCategory existingCategory = homeCategoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Home category not found with id {}", id);
                    return new HomeCategoryException("Home category not found");
                });

        if (homeCategory.getName() != null) {
            existingCategory.setName(homeCategory.getName());
        }

        if (homeCategory.getImageUrl() != null) {
            existingCategory.setImageUrl(homeCategory.getImageUrl());
        }

        if (homeCategory.getCategoryId() != null) {
            existingCategory.setCategoryId(homeCategory.getCategoryId());
        }

        // if section update also needed
        if (homeCategory.getSection() != null) {
            existingCategory.setSection(homeCategory.getSection());
        }

        HomeCategory updatedCategory = homeCategoryRepository.save(existingCategory);

        log.info("Home category updated successfully with id {}", id);

        return updatedCategory;
    }

    // GET ALL HOME CATEGORIES
    @Override
    public List<HomeCategory> getAllHomeCategories() {
        log.info("Fetching all home categories");
        return homeCategoryRepository.findAll();
    }
}