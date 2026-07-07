package com.ecommerce.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.backend.model.*;

@Repository
public interface ReviewsRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);
}
