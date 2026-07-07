package com.ecommerce.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.backend.model.HomeCategory;

@Repository
public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long> {

}
