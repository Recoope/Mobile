package com.example.recoope_mobile.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName(value="token")
    private String token;

    @SerializedName(value="cnpj")
    private String cnpj;

    public String getToken() {
        return token;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public LoginResponse(String token, String cnpj) {
        this.token = token;
        this.cnpj = cnpj;
    }
}
