package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ecommerce.backend.exceptions.DealException;
import com.ecommerce.backend.model.Deal;
import com.ecommerce.backend.model.HomeCategory;
import com.ecommerce.backend.repository.DealRepository;
import com.ecommerce.backend.repository.HomeCategoryRepository;
import com.ecommerce.backend.service.Impl.DealServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {

    @Mock
    private DealRepository dealRepository;

    @Mock
    private HomeCategoryRepository homeCategoryRepository;

    @InjectMocks
    private DealServiceImpl dealService;

    private Deal deal;
    private HomeCategory category;

    @BeforeEach
    void setUp() {

        category = new HomeCategory();
        category.setId(1L);

        deal = new Deal();
        deal.setId(1L);
        deal.setCategory(category);
        deal.setDiscount(30);
    }

    // ==========================
    // getDeal()
    // ==========================

    @Test
    void testGetDeal() {

        when(dealRepository.findAll()).thenReturn(Arrays.asList(deal));

        List<Deal> deals = dealService.getDeal();

        assertEquals(1, deals.size());
        assertEquals(30, deals.get(0).getDiscount());

        verify(dealRepository).findAll();
    }

    // ==========================
    // createDeal()
    // ==========================

    @Test
    void testCreateDeal() throws Exception {

        when(homeCategoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(dealRepository.save(any(Deal.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Deal savedDeal = dealService.createDeal(deal);

        assertNotNull(savedDeal);
        assertEquals(30, savedDeal.getDiscount());
        assertEquals(category, savedDeal.getCategory());

        verify(homeCategoryRepository).findById(1L);
        verify(dealRepository).save(any(Deal.class));
    }

    @Test
    void testCreateDeal_CategoryNotFound() {

        when(homeCategoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        DealException ex = assertThrows(
                DealException.class,
                () -> dealService.createDeal(deal));

        assertEquals("Home category not found", ex.getMessage());

        verify(dealRepository, never()).save(any());
    }

    // ==========================
    // updateDeal()
    // ==========================

    @Test
    void testUpdateDeal() throws Exception {

        Deal update = new Deal();
        update.setDiscount(50);
        update.setCategory(category);

        when(dealRepository.findById(1L))
                .thenReturn(Optional.of(deal));

        when(homeCategoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(dealRepository.save(any(Deal.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Deal updated = dealService.updateDeal(update, 1L);

        assertEquals(50, updated.getDiscount());

        verify(dealRepository).findById(1L);
        verify(homeCategoryRepository).findById(1L);
        verify(dealRepository).save(any(Deal.class));
    }

    @Test
    void testUpdateDeal_DealNotFound() {

        when(dealRepository.findById(1L))
                .thenReturn(Optional.empty());

        DealException ex = assertThrows(
                DealException.class,
                () -> dealService.updateDeal(deal, 1L));

        assertEquals("Deal not found", ex.getMessage());

        verify(dealRepository, never()).save(any());
    }

    @Test
    void testUpdateDeal_CategoryNotFound() {

        Deal update = new Deal();
        update.setDiscount(60);
        update.setCategory(category);

        when(dealRepository.findById(1L))
                .thenReturn(Optional.of(deal));

        when(homeCategoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        DealException ex = assertThrows(
                DealException.class,
                () -> dealService.updateDeal(update, 1L));

        assertEquals("Home category not found", ex.getMessage());

        verify(dealRepository, never()).save(any());
    }

    // ==========================
    // deleteDeal()
    // ==========================

    @Test
    void testDeleteDeal() throws Exception {

        when(dealRepository.findById(1L))
                .thenReturn(Optional.of(deal));

        doNothing().when(dealRepository).delete(deal);

        dealService.deleteDeal(1L);

        verify(dealRepository).findById(1L);
        verify(dealRepository).delete(deal);
    }

    @Test
    void testDeleteDeal_NotFound() {

        when(dealRepository.findById(1L))
                .thenReturn(Optional.empty());

        DealException ex = assertThrows(
                DealException.class,
                () -> dealService.deleteDeal(1L));

        assertEquals("Deal not found", ex.getMessage());

        verify(dealRepository, never()).delete(any());
    }

}