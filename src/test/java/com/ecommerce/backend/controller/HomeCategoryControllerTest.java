package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Home;
import com.ecommerce.backend.model.HomeCategory;
import com.ecommerce.backend.service.HomeCategoryService;
import com.ecommerce.backend.service.HomeService;

class HomeCategoryControllerTest {

    @Test
    void shouldCreateAndFetchHomeCategories() throws Exception {
        HomeService homeService = mock(HomeService.class);
        HomeCategoryService homeCategoryService = mock(HomeCategoryService.class);
        HomeCategoryController controller = new HomeCategoryController(homeService, homeCategoryService);

        HomeCategory category = new HomeCategory();
        category.setId(2L);
        Home home = new Home();

        when(homeCategoryService.createHomeCategories(List.of(category))).thenReturn(List.of(category));
        when(homeService.createHomePageData(List.of(category))).thenReturn(home);
        when(homeCategoryService.getAllHomeCategories()).thenReturn(List.of(category));

        ResponseEntity<Home> createResponse = controller.createHomeCategories(List.of(category));
        ResponseEntity<List<HomeCategory>> listResponse = controller.getHomeCategories();

        assertEquals(201, createResponse.getStatusCode().value());
        assertEquals(home, createResponse.getBody());
        assertEquals(1, listResponse.getBody().size());
    }
}
