package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.exceptions.HomeCategoryException;
import com.ecommerce.backend.model.HomeCategory;

public interface HomeCategoryService {

    HomeCategory createHomeCategory(HomeCategory homeCategory) throws HomeCategoryException;

    List<HomeCategory> createHomeCategories(List<HomeCategory> homeCategories);

    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) throws HomeCategoryException;

    List<HomeCategory> getAllHomeCategories();

}
