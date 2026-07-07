package com.ecommerce.backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HomeCategorySectionTest {

    @Test
    void shouldExposeExpectedSectionValues() {
        assertEquals("ELECTRIC_CATEGORIES", HomeCategorySection.ELECTRIC_CATEGORIES.name());
        assertEquals("GRID", HomeCategorySection.GRID.name());
        assertEquals("SHOP_BY_CATEGORIES", HomeCategorySection.SHOP_BY_CATEGORIES.name());
        assertEquals("DEALS_OF_THE_DAY", HomeCategorySection.DEALS_OF_THE_DAY.name());
    }
}
