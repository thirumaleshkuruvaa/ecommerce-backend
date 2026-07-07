package com.ecommerce.backend.domain;

public enum OrderStatus {
    PENDING, // Order has been placed but not yet processed
    PLACED, // Order has been confirmed and placed by the customer
    CONFIRMED, // Order has been confirmed by the seller and is being prepared for shipment
    SHIPPED, // Order has been shipped to the customer
    DELIVERED, // Order has been delivered to the customer
    CANCELLED, // Order has been cancelled by the customer or seller

}