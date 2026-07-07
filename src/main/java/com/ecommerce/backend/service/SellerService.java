package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.domain.AccountStatus;
import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.model.Seller;

public interface SellerService {

    Seller getSellerProfile(String jwt) throws SellerException;

    Seller createSeller(Seller seller) throws SellerException;

    Seller getSellerById(Long id) throws SellerException;

    Seller getSellerByEmail(String email) throws SellerException;

    List<Seller> getAllSellers(AccountStatus accountStatus);

    Seller updateSeller(Long id, Seller seller) throws SellerException;

    void deleteSeller(Long id) throws SellerException;

    // Seller verifyEmail(String email, String otp);

    Seller verifyEmailByOtp(String otp) throws SellerException;

    Seller updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus) throws SellerException;

}
