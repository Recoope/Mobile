package com.example.recoope_mobile.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName(value="token")
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }
}
