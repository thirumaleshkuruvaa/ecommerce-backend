package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.domain.AccountStatus;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.service.SellerService;

class AdminControllerTest {

    @Test
    void shouldUpdateSellerStatus() throws Exception {
        SellerService sellerService = mock(SellerService.class);
        AdminController controller = new AdminController(sellerService);

        Seller seller = new Seller();
        seller.setId(10L);
        seller.setAccountStatus(AccountStatus.ACTIVE);

        when(sellerService.updateSellerAccountStatus(10L, AccountStatus.ACTIVE)).thenReturn(seller);

        ResponseEntity<Seller> response = controller.updateSellerStatus(10L, AccountStatus.ACTIVE);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(seller, response.getBody());
    }
}
