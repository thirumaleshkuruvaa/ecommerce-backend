package com.ecommerce.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.ecommerce.backend.config.AppConfig;
import com.ecommerce.backend.domain.HomeCategorySection;
import com.ecommerce.backend.model.Deal;
import com.ecommerce.backend.model.HomeCategory;
import com.ecommerce.backend.service.DealService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DealController.class)
@Import(AppConfig.class)
class DealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DealService dealService;

    private Deal createDeal() {

        HomeCategory category = new HomeCategory();

        category.setId(1L);
        category.setName("Mobiles");
        category.setCategoryId("100");
        category.setImageUrl("mobile.jpg");
        category.setSection(HomeCategorySection.DEALS_OF_THE_DAY);

        Deal deal = new Deal();
        deal.setId(10L);
        deal.setDiscount(25);
        deal.setCategory(category);

        return deal;
    }

    @Test
    void testCreateDeal() throws Exception {

        Deal deal = createDeal();

        when(dealService.createDeal(any(Deal.class))).thenReturn(deal);

        mockMvc.perform(post("/api/deals/admin/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deal)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.discount").value(25))
                .andExpect(jsonPath("$.category.id").value(1));
    }

    @Test
    void testGetAllDeals() throws Exception {

        List<Deal> deals = Arrays.asList(createDeal());

        when(dealService.getDeal()).thenReturn(deals);

        mockMvc.perform(get("/api/deals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].discount").value(25))
                .andExpect(jsonPath("$[0].category.id").value(1));
    }

    @Test
    void testUpdateDeal() throws Exception {

        Deal deal = createDeal();
        deal.setDiscount(50);

        when(dealService.updateDeal(any(Deal.class), eq(10L)))
                .thenReturn(deal);

        mockMvc.perform(patch("/api/deals/admin/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discount").value(50));
    }

    @Test
    void testDeleteDeal() throws Exception {

        doNothing().when(dealService).deleteDeal(10L);

        mockMvc.perform(delete("/api/deals/admin/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messege")
                        .value("Deal deleted successfully"));
    }
}