package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ecommerce.backend.domain.HomeCategorySection;
import com.ecommerce.backend.model.Deal;
import com.ecommerce.backend.model.Home;
import com.ecommerce.backend.model.HomeCategory;
import com.ecommerce.backend.repository.DealRepository;
import com.ecommerce.backend.service.Impl.HomeServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HomeServiceImplTest {

    @Mock
    private DealRepository dealRepository;

    @InjectMocks
    private HomeServiceImpl homeService;

    private HomeCategory gridCategory;
    private HomeCategory shopCategory;
    private HomeCategory electricCategory;
    private HomeCategory dealCategory;

    @BeforeEach
    void setUp() {

        gridCategory = new HomeCategory();
        gridCategory.setSection(HomeCategorySection.GRID);

        shopCategory = new HomeCategory();
        shopCategory.setSection(HomeCategorySection.SHOP_BY_CATEGORIES);

        electricCategory = new HomeCategory();
        electricCategory.setSection(HomeCategorySection.ELECTRIC_CATEGORIES);

        dealCategory = new HomeCategory();
        dealCategory.setSection(HomeCategorySection.DEALS_OF_THE_DAY);
    }

    @Test
    void testCreateHomePageData_WithExistingDeals() {

        Deal deal = new Deal();
        deal.setDiscount(20);
        deal.setCategory(dealCategory);

        when(dealRepository.findAll())
                .thenReturn(List.of(deal));

        List<HomeCategory> categories = Arrays.asList(
                gridCategory,
                shopCategory,
                electricCategory,
                dealCategory);

        Home home = homeService.createHomePageData(categories);

        assertNotNull(home);

        assertEquals(1, home.getGrid().size());
        assertEquals(1, home.getShopByCategories().size());
        assertEquals(1, home.getElectricCategories().size());
        assertEquals(1, home.getDealsOfTheDay().size());
        assertEquals(1, home.getDeals().size());

        assertEquals(20, home.getDeals().get(0).getDiscount());

        verify(dealRepository).findAll();
        verify(dealRepository, never()).saveAll(anyList());
    }

    @Test
    void testCreateHomePageData_WhenNoDealsExist() {

        when(dealRepository.findAll())
                .thenReturn(Collections.emptyList());

        when(dealRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<HomeCategory> categories = Arrays.asList(
                gridCategory,
                shopCategory,
                electricCategory,
                dealCategory);

        Home home = homeService.createHomePageData(categories);

        assertNotNull(home);

        assertEquals(1, home.getGrid().size());
        assertEquals(1, home.getShopByCategories().size());
        assertEquals(1, home.getElectricCategories().size());
        assertEquals(1, home.getDealsOfTheDay().size());
        assertEquals(1, home.getDeals().size());

        Deal createdDeal = home.getDeals().get(0);

        assertEquals(10, createdDeal.getDiscount());
        assertEquals(dealCategory, createdDeal.getCategory());

        verify(dealRepository).findAll();
        verify(dealRepository).saveAll(anyList());
    }
}