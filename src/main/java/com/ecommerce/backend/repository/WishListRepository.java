package com.ecommerce.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.backend.model.WishList;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    WishList findByUserId(Long userId);

    @Modifying
    @Query(value = "DELETE FROM wish_list_products WHERE products_id = :productId", nativeQuery = true)
    void deleteFromWishlist(@Param("productId") Long productId);
}
