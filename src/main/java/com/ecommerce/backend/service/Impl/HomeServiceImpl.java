package com.ecommerce.backend.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.domain.HomeCategorySection;
import com.ecommerce.backend.model.Deal;
import com.ecommerce.backend.model.Home;
import com.ecommerce.backend.model.HomeCategory;
import com.ecommerce.backend.repository.DealRepository;
import com.ecommerce.backend.service.HomeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HomeServiceImpl implements HomeService {

    private final DealRepository dealRepository;

    public HomeServiceImpl(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {

        log.info("Create homepage data");

        // GRID CATEGORIES

        log.info("Filtering GRID categories");

        List<HomeCategory> gridCategories = allCategories.stream()
                .filter(category -> category.getSection() == HomeCategorySection.GRID).toList();

        // SHOP BY CATEGORIES

        log.debug("Filtering SHOP_BY_CATEGORIES");

        List<HomeCategory> shopByCategories = allCategories.stream()
                .filter(category -> category.getSection() == HomeCategorySection.SHOP_BY_CATEGORIES).toList();

        // ELECTRIC CATEGORIES

        log.debug("Filtering ELECTRIC_CATEGORIES");

        List<HomeCategory> electricCategories = allCategories.stream()
                .filter(category -> category.getSection() == HomeCategorySection.ELECTRIC_CATEGORIES).toList();

        // DEALS OF THE DAY CATEGORIES

        log.debug("Filtering DEALS_OF_THE_DAY categories");

        List<HomeCategory> dealCategories = allCategories.stream()
                .filter(category -> category.getSection() == HomeCategorySection.DEALS_OF_THE_DAY).toList();

        // CREATE DEALS

        log.info("Fetching existing deals");

        List<Deal> existingDeals = dealRepository.findAll();

        List<Deal> createdDeals;

        if (existingDeals.isEmpty()) {

            log.warn("No existing deals found, creating default deals");

            List<Deal> deals = dealCategories.stream()
                    .map(category -> {

                        Deal deal = new Deal();

                        deal.setDiscount(10);

                        deal.setCategory(category);

                        return deal;

                    }).toList();

            createdDeals = dealRepository.saveAll(deals);

            log.info("Default deals created successfully");

        } else {

            log.info("Existing deals found");

            createdDeals = existingDeals;
        }

        // CREATE HOME OBJECT
        log.debug("Building Home response object");

        Home home = new Home();

        home.setGrid(gridCategories);

        home.setShopByCategories(shopByCategories);

        home.setElectricCategories(electricCategories);

        home.setDeals(createdDeals);

        home.setDealsOfTheDay(dealCategories);

        log.info("HomePage data created successfully");

        return home;
    }
}