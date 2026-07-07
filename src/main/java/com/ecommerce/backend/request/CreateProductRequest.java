package com.ecommerce.backend.request;

import java.util.List;
import lombok.Data;

// @Data
public class CreateProductRequest {

    // @NotBlank(message = "Title is required")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMrpPrice() {
        return mrpPrice;
    }

    public void setMrpPrice(Integer mrpPrice) {
        this.mrpPrice = mrpPrice;
    }

    public Integer getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Integer sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    // @NotBlank(message = "Description is required")
    private String description;

    // @NotNull(message = "MRP price is required")
    // @Min(value = 1, message = "MRP price must be greater than 0")
    private Integer mrpPrice;

    // @NotNull(message = "Selling price is required")
    // @Min(value = 1, message = "Selling price must be greater than 0")
    private Integer sellingPrice;

    // @NotNull(message = "Quantity is required")
    // @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    // @NotBlank(message = "Brand is required")
    private String brand;

    // @NotBlank(message = "Color is required")
    private String color;

    // @NotEmpty(message = "At least one image is required")
    private List<String> images;

    // @NotBlank(message = "Category is required")
    private String category;

    // @NotBlank(message = "Category 2 is required")
    private String category2;
    // @NotBlank(message = "Category 3 is required")
    private String category3;

    // @NotEmpty(message = "At least one size is required")
    private List<String> sizes;
}