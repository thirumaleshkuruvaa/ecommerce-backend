package com.ecommerce.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.backend.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findBySellerId(Long sellerId);

    List<Order> findByUser_IdAndArchivedFalse(Long userId);

    List<Order> findByUser_IdAndArchivedTrue(Long userId);

}
