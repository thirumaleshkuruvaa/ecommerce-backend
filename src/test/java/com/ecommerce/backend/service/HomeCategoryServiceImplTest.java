package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.backend.domain.HomeCategorySection;
import com.ecommerce.backend.exceptions.HomeCategoryException;
import com.ecommerce.backend.model.HomeCategory;
import com.ecommerce.backend.repository.HomeCategoryRepository;
import com.ecommerce.backend.service.Impl.HomeCategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class HomeCategoryServiceImplTest {

    @Mock
    private HomeCategoryRepository homeCategoryRepository;

    @InjectMocks
    private HomeCategoryServiceImpl homeCategoryService;

    private HomeCategory homeCategory;

    @BeforeEach
    void setUp() {

        homeCategory = new HomeCategory();
        homeCategory.setId(1L);
        homeCategory.setName("Electronics");
        homeCategory.setImageUrl("image.jpg");
        homeCategory.setCategoryId("CAT101");
        homeCategory.setSection(HomeCategorySection.ELECTRIC_CATEGORIES);
    }

    // -------------------------------------------------------
    // CREATE HOME CATEGORY
    // -------------------------------------------------------

    @Test
    void testCreateHomeCategory() {

        when(homeCategoryRepository.save(homeCategory)).thenReturn(homeCategory);

        HomeCategory result = homeCategoryService.createHomeCategory(homeCategory);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        assertEquals("CAT101", result.getCategoryId());

        verify(homeCategoryRepository).save(homeCategory);
    }

    // -------------------------------------------------------
    // CREATE MULTIPLE HOME CATEGORIES
    // -------------------------------------------------------

    @Test
    void testCreateHomeCategories() {

        HomeCategory category2 = new HomeCategory();
        category2.setId(2L);
        category2.setName("Fashion");
        category2.setCategoryId("CAT102");
        category2.setImageUrl("fashion.jpg");
        category2.setSection(HomeCategorySection.GRID);

        List<HomeCategory> categories = Arrays.asList(homeCategory, category2);

        when(homeCategoryRepository.saveAll(categories))
                .thenReturn(categories);

        List<HomeCategory> result = homeCategoryService.createHomeCategories(categories);

        assertEquals(2, result.size());

        verify(homeCategoryRepository).saveAll(categories);
    }

    // -------------------------------------------------------
    // UPDATE HOME CATEGORY SUCCESS
    // -------------------------------------------------------

    @Test
    void testUpdateHomeCategory() throws Exception {

        HomeCategory request = new HomeCategory();
        request.setName("Mobiles");
        request.setImageUrl("mobile.jpg");
        request.setCategoryId("CAT500");
        request.setSection(HomeCategorySection.SHOP_BY_CATEGORIES);

        when(homeCategoryRepository.findById(1L))
                .thenReturn(Optional.of(homeCategory));

        when(homeCategoryRepository.save(any(HomeCategory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HomeCategory result = homeCategoryService.updateHomeCategory(request, 1L);

        assertEquals("Mobiles", result.getName());
        assertEquals("mobile.jpg", result.getImageUrl());
        assertEquals("CAT500", result.getCategoryId());
        assertEquals(HomeCategorySection.SHOP_BY_CATEGORIES,
                result.getSection());

        verify(homeCategoryRepository).findById(1L);
        verify(homeCategoryRepository).save(homeCategory);
    }

    // -------------------------------------------------------
    // UPDATE HOME CATEGORY - NOT FOUND
    // -------------------------------------------------------

    @Test
    void testUpdateHomeCategory_NotFound() {

        when(homeCategoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        HomeCategoryException exception = assertThrows(HomeCategoryException.class,
                () -> homeCategoryService.updateHomeCategory(
                        new HomeCategory(),
                        1L));

        assertEquals(
                "Home category not found",
                exception.getMessage());

        verify(homeCategoryRepository).findById(1L);
        verify(homeCategoryRepository, never()).save(any());
    }

    // -------------------------------------------------------
    // UPDATE ONLY NAME
    // -------------------------------------------------------

    @Test
    void testUpdateOnlyName() throws Exception {

        HomeCategory request = new HomeCategory();
        request.setName("Furniture");

        when(homeCategoryRepository.findById(1L))
                .thenReturn(Optional.of(homeCategory));

        when(homeCategoryRepository.save(any(HomeCategory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HomeCategory result = homeCategoryService.updateHomeCategory(request, 1L);

        assertEquals("Furniture", result.getName());
        assertEquals("image.jpg", result.getImageUrl());
        assertEquals("CAT101", result.getCategoryId());

        verify(homeCategoryRepository).save(homeCategory);
    }

    // -------------------------------------------------------
    // UPDATE ONLY IMAGE
    // -------------------------------------------------------

    @Test
    void testUpdateOnlyImage() throws Exception {

        HomeCategory request = new HomeCategory();
        request.setImageUrl("updated.jpg");

        when(homeCategoryRepository.findById(1L))
                .thenReturn(Optional.of(homeCategory));

        when(homeCategoryRepository.save(any(HomeCategory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HomeCategory result = homeCategoryService.updateHomeCategory(request, 1L);

        assertEquals("updated.jpg", result.getImageUrl());

        verify(homeCategoryRepository).save(homeCategory);
    }

    // -------------------------------------------------------
    // UPDATE ONLY CATEGORY ID
    // -------------------------------------------------------

    @Test
    void testUpdateOnlyCategoryId() throws Exception {

        HomeCategory request = new HomeCategory();
        request.setCategoryId("CAT999");

        when(homeCategoryRepository.findById(1L))
                .thenReturn(Optional.of(homeCategory));

        when(homeCategoryRepository.save(any(HomeCategory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HomeCategory result = homeCategoryService.updateHomeCategory(request, 1L);

        assertEquals("CAT999", result.getCategoryId());

        verify(homeCategoryRepository).save(homeCategory);
    }

    // -------------------------------------------------------
    // UPDATE ONLY SECTION
    // -------------------------------------------------------

    @Test
    void testUpdateOnlySection() throws Exception {

        HomeCategory request = new HomeCategory();
        request.setSection(HomeCategorySection.DEALS_OF_THE_DAY);

        when(homeCategoryRepository.findById(1L))
                .thenReturn(Optional.of(homeCategory));

        when(homeCategoryRepository.save(any(HomeCategory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HomeCategory result = homeCategoryService.updateHomeCategory(request, 1L);

        assertEquals(HomeCategorySection.DEALS_OF_THE_DAY,
                result.getSection());

        verify(homeCategoryRepository).save(homeCategory);
    }

    // -------------------------------------------------------
    // GET ALL HOME CATEGORIES
    // -------------------------------------------------------

    @Test
    void testGetAllHomeCategories() {

        List<HomeCategory> categories = Arrays.asList(homeCategory);

        when(homeCategoryRepository.findAll())
                .thenReturn(categories);

        List<HomeCategory> result = homeCategoryService.getAllHomeCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electronics",
                result.get(0).getName());

        verify(homeCategoryRepository).findAll();
    }

    // -------------------------------------------------------
    // GET ALL HOME CATEGORIES EMPTY
    // -------------------------------------------------------

    @Test
    void testGetAllHomeCategories_Empty() {

        when(homeCategoryRepository.findAll())
                .thenReturn(List.of());

        List<HomeCategory> result = homeCategoryService.getAllHomeCategories();

        assertTrue(result.isEmpty());

        verify(homeCategoryRepository).findAll();
    }

}