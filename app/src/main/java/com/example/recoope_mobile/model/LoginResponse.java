package com.example.recoope_mobile.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName(value="token")
    private String token;

    @SerializedName(value="cnpj")
    private String cnpj;

    @SerializedName(value="refreshToken")
    private String refreshToken;

    public String getToken() {
        return token;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public String getCnpj() {
        return cnpj;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public void setRefreshToken(String token) {
        this.refreshToken = refreshToken;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public LoginResponse(String token, String cnpj) {
        this.token = token;
        this.cnpj = cnpj;
    }
}
