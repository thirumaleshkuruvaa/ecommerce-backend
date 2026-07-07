package com.ecommerce.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.backend.model.Deal;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

}
