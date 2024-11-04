package com.example.recoope_mobile.dto;

import com.google.gson.annotations.SerializedName;

public class CompanyDto {

    @SerializedName(value = "nome")
    private String name;
    @SerializedName(value = "email")
    private String email;
    @SerializedName(value = "telefone")
    private String phone;


    public CompanyDto(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
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

    @Override
    public String toString() {
        return "CompanyProfile{" +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}