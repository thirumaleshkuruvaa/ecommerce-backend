package com.ecommerce.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.backend.domain.AccountStatus;
import com.ecommerce.backend.model.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    // Seller findByEmail(String email);

    Optional<Seller> findByEmail(String email);

    List<Seller> findByAccountStatus(AccountStatus accountStatus);

}
