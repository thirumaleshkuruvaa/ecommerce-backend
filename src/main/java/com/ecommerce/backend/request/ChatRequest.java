package com.ecommerce.backend.request;

public class ChatRequest {

    private String messege;

    public ChatRequest() {
    }

    public ChatRequest(String messege) {
        this.messege = messege;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }
}