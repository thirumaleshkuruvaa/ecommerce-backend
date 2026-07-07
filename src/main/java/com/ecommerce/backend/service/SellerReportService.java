package com.ecommerce.backend.service;

import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.SellerReport;

public interface SellerReportService {

    SellerReport getSellerReport(Seller seller);

    SellerReport updateSellerReport(SellerReport report);

    void updateAfterSuccessfulPayment(Order order) throws SellerException;

    void updateAfterCancelledOrder(Order order) throws SellerException;

}