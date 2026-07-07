package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.model.Home;
import com.ecommerce.backend.model.HomeCategory;

public interface HomeService {
    public Home createHomePageData(List<HomeCategory> allCategories);

}