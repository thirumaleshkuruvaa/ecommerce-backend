package com.ecommerce.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class SellerReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @OneToOne
    // private Seller seller;

    @OneToOne
    @JoinColumn(name = "seller_id", nullable = false, unique = true)
    private Seller seller;
    private Long totalEarnings = 0L;

    private Long totalSales = 0L;

    private Long totalRefunds = 0L;

    private Long totalTax = 0L;

    private Long netEarnings = 0L;

    private Integer totalOrders = 0;

    private Integer cancelOrders = 0;

    private Integer totalTransactions = 0;

}
