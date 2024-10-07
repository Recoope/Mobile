package com.example.recoope_mobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Cooperative {

    @SerializedName(value="cnpj")
    private String cnpj;

    @SerializedName(value="nome")
    private String name;

    @SerializedName(value="email")
    private String email;

    @SerializedName(value="registro")
    private String registration;

    public Cooperative(String cnpj, String name, String email, String registation) {
        this.cnpj = cnpj;
        this.name = name;
        this.email = email;
        this.registration = registation;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String password) {
        this.registration = registration;
    }
}
