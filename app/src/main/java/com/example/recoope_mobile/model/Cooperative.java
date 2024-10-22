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

    @SerializedName(value="status")
    private String status;

    public Cooperative(String cnpj, String name, String email, String status) {
        this.cnpj = cnpj;
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public Cooperative(){}

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Cooperative{" +
                "cnpj='" + cnpj + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

