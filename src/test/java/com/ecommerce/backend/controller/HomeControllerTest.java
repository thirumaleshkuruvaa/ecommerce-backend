package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Home;
import com.ecommerce.backend.model.HomeCategory;
import com.ecommerce.backend.service.HomeCategoryService;
import com.ecommerce.backend.service.HomeService;

class HomeControllerTest {

    @Test
    void shouldReturnHomePageData() {
        HomeService homeService = mock(HomeService.class);
        HomeCategoryService homeCategoryService = mock(HomeCategoryService.class);
        HomeController controller = new HomeController(homeService, homeCategoryService);

        HomeCategory category = new HomeCategory();
        category.setName("Trending");
        Home home = new Home();
        home.setGrid(List.of(category));

        when(homeCategoryService.getAllHomeCategories()).thenReturn(List.of(category));
        when(homeService.createHomePageData(List.of(category))).thenReturn(home);

        ResponseEntity<Home> response = controller.getHomePageData();

        assertNotNull(response);
        assertNotNull(response.getBody());
    }
}
