package com.example.recoope_mobile.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import org.intellij.lang.annotations.Identifier;

import java.time.LocalDate;

public class Company {
    @SerializedName(value = "cnpj")
    private String cnpj;
    @SerializedName(value = "nome")
    private String name;
    @SerializedName(value = "email")
    private String email;
    @SerializedName(value = "senha")

    private String password;
    @SerializedName(value = "telefone")

    private String phone;

    @SerializedName(value = "confirmacaoSenha")
    private String passwordConfirmation;

    public Company(String cnpj, String name, String email, String password, String phone, String passwordConfirmation) {
        this.cnpj = cnpj;
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.phone = phone;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    @Override
    public String toString() {
        return "Company{" +
                "cnpj='" + cnpj + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", passwordConfirmation='" + passwordConfirmation + '\'' +
                '}';
    }
}
