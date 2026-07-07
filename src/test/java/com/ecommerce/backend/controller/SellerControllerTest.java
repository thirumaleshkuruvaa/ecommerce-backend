package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.config.JwtProvider;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.repository.VerificationCodeRepository;
import com.ecommerce.backend.service.AuthService;
import com.ecommerce.backend.service.EmailService;
import com.ecommerce.backend.service.SellerReportService;
import com.ecommerce.backend.service.SellerService;

class SellerControllerTest {

    @Test
    void shouldGetAllSellers() {
        SellerService sellerService = mock(SellerService.class);
        VerificationCodeRepository verificationCodeRepository = mock(VerificationCodeRepository.class);
        AuthService authService = mock(AuthService.class);
        EmailService emailService = mock(EmailService.class);
        SellerReportService sellerReportService = mock(SellerReportService.class);
        JwtProvider jwtProvider = mock(JwtProvider.class);
        SellerController controller = new SellerController(sellerService, verificationCodeRepository, authService,
                emailService, sellerReportService, jwtProvider);

        Seller seller = new Seller();
        seller.setId(6L);

        when(sellerService.getAllSellers(null)).thenReturn(List.of(seller));

        ResponseEntity<List<Seller>> response = controller.getAllSellers(null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }
}
