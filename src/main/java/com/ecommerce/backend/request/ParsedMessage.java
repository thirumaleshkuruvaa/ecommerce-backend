package com.ecommerce.backend.request;

public class ParsedMessage {

    private String keyword;

    private Integer minPrice;

    private Integer maxPrice;
    private Integer minDiscount;
    private ChatIntent intent = ChatIntent.PRODUCT_SEARCH;
public ChatIntent getIntent() {
    return intent;
}

public void setIntent(ChatIntent intent) {
    this.intent = intent;
}
    public Integer getMinDiscount() {
        return minDiscount;
    }

    public void setMinDiscount(Integer minDiscount) {
        this.minDiscount = minDiscount;
    }

    private boolean recommendation;

    public ParsedMessage() {
    }

    public ParsedMessage(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public boolean isRecommendation() {
        return recommendation;
    }

    public void setRecommendation(boolean recommendation) {
        this.recommendation = recommendation;
    }
}