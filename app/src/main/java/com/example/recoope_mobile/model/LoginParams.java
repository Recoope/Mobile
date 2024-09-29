package com.example.recoope_mobile.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class LoginParams {

    @SerializedName(value = "cnpjOuEmail")
    private String cnpjOrEmail;

    @SerializedName(value = "senha")
    private String password;

    public LoginParams(String cnpjOrEmail, String password) {
        this.cnpjOrEmail = cnpjOrEmail;
        this.password = password;
    }

    public String getCnpjOrEmail() {
        return cnpjOrEmail;
    }

    public void setCnpjOrEmail(String cnpjOrEmail) {
        this.cnpjOrEmail = cnpjOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
 