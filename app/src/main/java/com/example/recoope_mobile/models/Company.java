package com.example.recoope_mobile.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Company {

    @JsonProperty(value = "cnpjEmpresa")
    private String cnpj;
    @JsonProperty(value = "nomeEmpresa")
    private String name;
    @JsonProperty(value = "emailEmpresa")
    private String email;
    @JsonProperty(value = "senhaEmpresa")

    private String password;
    @JsonProperty(value = "telefoneEmpresa")

    private String phone;
    @JsonProperty(value = "registroEmpresa")

    private String record;

    @JsonProperty(value = "confirmacaoSenha")
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

    public String getRecord() {
        return record;
    }
}
