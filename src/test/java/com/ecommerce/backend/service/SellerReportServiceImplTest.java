package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.model.Order;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.SellerReport;
import com.ecommerce.backend.repository.SellerReportRepository;
import com.ecommerce.backend.service.Impl.SellerReportServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SellerReportServiceImplTest {

    @Mock
    private SellerReportRepository sellerReportRepository;

    @Mock
    private SellerService sellerService;

    @InjectMocks
    private SellerReportServiceImpl sellerReportService;

    private Seller seller;
    private SellerReport report;
    private Order order;

    @BeforeEach
    void setUp() {

        seller = new Seller();
        seller.setId(1L);

        report = new SellerReport();
        report.setId(1L);
        report.setSeller(seller);
        report.setTotalOrders(2);
        report.setCancelOrders(0);
        report.setTotalTransactions(2);
        report.setTotalSales(1000L);
        report.setTotalEarnings(1000L);
        report.setTotalRefunds(0L);
        report.setTotalTax(180L);
        report.setNetEarnings(820L);

        order = new Order();
        order.setId(100L);
        order.setSellerId(1L);
        order.setTotalSellingPrice(500);
    }

    @Test
    void testGetSellerReport_WhenExists() {

        when(sellerReportRepository.findBySellerId(1L)).thenReturn(report);

        SellerReport result = sellerReportService.getSellerReport(seller);

        assertNotNull(result);
        assertEquals(report, result);

        verify(sellerReportRepository).findBySellerId(1L);
        verify(sellerReportRepository, never()).save(any());
    }

    @Test
    void testGetSellerReport_WhenNotExists() {

        when(sellerReportRepository.findBySellerId(1L)).thenReturn(null);

        when(sellerReportRepository.save(any(SellerReport.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SellerReport result = sellerReportService.getSellerReport(seller);

        assertNotNull(result);
        assertEquals(0L, result.getTotalSales());
        assertEquals(0, result.getTotalOrders());

        verify(sellerReportRepository).save(any(SellerReport.class));
    }

    @Test
    void testUpdateSellerReport() {

        when(sellerReportRepository.save(report)).thenReturn(report);

        SellerReport result = sellerReportService.updateSellerReport(report);

        assertEquals(report, result);

        verify(sellerReportRepository).save(report);
    }

    @Test
    void testUpdateAfterSuccessfulPayment() throws SellerException {

        when(sellerService.getSellerById(1L)).thenReturn(seller);
        when(sellerReportRepository.findBySellerId(1L)).thenReturn(report);

        sellerReportService.updateAfterSuccessfulPayment(order);

        assertEquals(3, report.getTotalOrders());
        assertEquals(3, report.getTotalTransactions());
        assertEquals(1500L, report.getTotalSales());
        assertEquals(1500L, report.getTotalEarnings());

        Long tax = Math.round(500 * 0.18);
        assertEquals(180L + tax, report.getTotalTax());
        assertEquals(820L + (500 - tax), report.getNetEarnings());

        verify(sellerReportRepository).save(report);
    }

    @Test
    void testUpdateAfterCancelledOrder() throws SellerException {

        report.setTotalOrders(3);
        report.setTotalTransactions(3);
        report.setTotalSales(1500L);
        report.setTotalEarnings(1500L);
        report.setNetEarnings(1230L);
        report.setTotalTax(270L);

        when(sellerService.getSellerById(1L)).thenReturn(seller);
        when(sellerReportRepository.findBySellerId(1L)).thenReturn(report);

        sellerReportService.updateAfterCancelledOrder(order);

        Long tax = Math.round(500 * 0.18);

        assertEquals(2, report.getTotalOrders());
        assertEquals(1, report.getCancelOrders());
        assertEquals(2, report.getTotalTransactions());
        assertEquals(1000L, report.getTotalSales());
        assertEquals(1000L, report.getTotalEarnings());
        assertEquals(500L, report.getTotalRefunds());
        assertEquals(270L - tax, report.getTotalTax());
        assertEquals(1230L - (500 - tax), report.getNetEarnings());

        verify(sellerReportRepository).save(report);
    }

    @Test
    void testUpdateAfterCancelledOrder_WhenValuesBecomeNegative() throws SellerException {

        SellerReport emptyReport = new SellerReport();
        emptyReport.setSeller(seller);

        when(sellerService.getSellerById(1L)).thenReturn(seller);
        when(sellerReportRepository.findBySellerId(1L)).thenReturn(emptyReport);

        sellerReportService.updateAfterCancelledOrder(order);

        assertEquals(0, emptyReport.getTotalOrders());
        assertEquals(0, emptyReport.getTotalTransactions());
        assertEquals(0L, emptyReport.getTotalSales());
        assertEquals(0L, emptyReport.getTotalEarnings());
        assertEquals(0L, emptyReport.getTotalTax());
        assertEquals(0L, emptyReport.getNetEarnings());
        assertEquals(500L, emptyReport.getTotalRefunds());
        assertEquals(1, emptyReport.getCancelOrders());

        verify(sellerReportRepository).save(emptyReport);
    }
}