package com.ecommerce.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import lombok.*;

@Entity
// @Getter
// @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Integer sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Integer getMrpPrice() {
        return mrpPrice;
    }

    public void setMrpPrice(Integer mrpPrice) {
        this.mrpPrice = mrpPrice;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    private Product product;

    @ElementCollection
    @CollectionTable(name = "cart_item_sizes", joinColumns = @JoinColumn(name = "cart_item_id"))
    @Column(name = "size")
    private List<String> sizes = new ArrayList<>();

    @Min(1)
    private int quantity = 1;

    private Integer sellingPrice;
    private Integer mrpPrice;

    private Long userId; // optional (not needed if cart relation exists)
}