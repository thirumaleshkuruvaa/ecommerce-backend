package com.ecommerce.backend.service.Impl;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.SellerReport;
import com.ecommerce.backend.repository.SellerReportRepository;
import com.ecommerce.backend.service.SellerReportService;
import com.ecommerce.backend.service.SellerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SellerReportServiceImpl
                implements SellerReportService {

        private final SellerReportRepository sellerReportRepository;
        private final SellerService sellerService;

        public SellerReportServiceImpl(
                        SellerReportRepository sellerReportRepository,
                        SellerService sellerService) {

                this.sellerReportRepository = sellerReportRepository;
                this.sellerService = sellerService;
        }

        private SellerReport createInitialReport(
                        Seller seller) {

                SellerReport report = new SellerReport();

                report.setSeller(seller);

                report.setTotalOrders(0);
                report.setCancelOrders(0);
                report.setTotalTransactions(0);

                report.setTotalSales(0L);
                report.setTotalEarnings(0L);
                report.setTotalRefunds(0L);
                report.setTotalTax(0L);
                report.setNetEarnings(0L);

                return sellerReportRepository.save(report);
        }

        @Override
        public SellerReport getSellerReport(
                        Seller seller) {

                SellerReport report = sellerReportRepository.findBySellerId(
                                seller.getId());

                if (report == null) {

                        report = createInitialReport(seller);
                }

                return report;
        }

        @Override
        public SellerReport updateSellerReport(
                        SellerReport report) {

                return sellerReportRepository.save(report);
        }

        @Override
        public void updateAfterSuccessfulPayment(Order order)
                        throws SellerException {

                Seller seller = sellerService.getSellerById(order.getSellerId());

                SellerReport report = getSellerReport(seller);

                Long amount = Long.valueOf(order.getTotalSellingPrice());

                Long tax = Math.round(amount * 0.18);

                report.setTotalOrders(report.getTotalOrders() + 1);

                report.setTotalTransactions(report.getTotalTransactions() + 1);

                // Revenue
                report.setTotalSales(report.getTotalSales() + amount);

                report.setTotalEarnings(report.getTotalEarnings() + amount);

                report.setTotalTax(report.getTotalTax() + tax);

                report.setNetEarnings(
                                report.getNetEarnings() + (amount - tax));

                sellerReportRepository.save(report);

                log.info("Seller report updated successfully for seller {}",
                                seller.getId());
        }

        @Override
        public void updateAfterCancelledOrder(Order order)
                        throws SellerException {

                Seller seller = sellerService.getSellerById(order.getSellerId());

                SellerReport report = getSellerReport(seller);

                Long refundAmount = Long.valueOf(order.getTotalSellingPrice());

                Long tax = Math.round(refundAmount * 0.18);

                report.setCancelOrders(report.getCancelOrders() + 1);

                report.setTotalRefunds(
                                report.getTotalRefunds() + refundAmount);

                if (report.getTotalOrders() > 0) {
                        report.setTotalOrders(
                                        report.getTotalOrders() - 1);
                }

                if (report.getTotalTransactions() > 0) {
                        report.setTotalTransactions(
                                        report.getTotalTransactions() - 1);
                }

                report.setTotalSales(
                                Math.max(0L,
                                                report.getTotalSales() - refundAmount));

                report.setTotalEarnings(
                                Math.max(0L,
                                                report.getTotalEarnings() - refundAmount));

                report.setTotalTax(
                                Math.max(0L,
                                                report.getTotalTax() - tax));

                report.setNetEarnings(
                                Math.max(0L,
                                                report.getNetEarnings() - (refundAmount - tax)));

                sellerReportRepository.save(report);

                log.info("Seller report reversed successfully for cancelled order {}",
                                order.getId());
        }

}