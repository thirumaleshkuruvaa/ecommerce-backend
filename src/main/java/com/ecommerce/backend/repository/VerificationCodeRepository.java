package com.ecommerce.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.backend.model.*;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    Optional<VerificationCode> findByEmail(String email);

    Optional<VerificationCode> findByOtp(String otp);

    Optional<VerificationCode> findByEmailAndType(String email, String type);

    void deleteByEmailAndType(String email, String type);

    @Transactional
    @Modifying
    @Query("DELETE FROM VerificationCode v WHERE v.email=:email")
    void deleteByEmail(@Param("email") String email);
}