package com.ecommerce.backend.response;

import com.ecommerce.backend.domain.USER_ROLE;

public class AuthResponse {
    private String jwt;
    private String messege;
    private USER_ROLE role;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }

    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }

}
