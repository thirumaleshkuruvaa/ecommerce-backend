package com.ecommerce.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.backend.model.SavedCard;
import com.ecommerce.backend.model.User;

public interface SavedCardRepository extends JpaRepository<SavedCard, Long> {

    List<SavedCard> findByUser(User user);
}