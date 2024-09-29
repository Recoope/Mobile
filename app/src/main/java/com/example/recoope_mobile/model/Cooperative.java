package com.example.recoope_mobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cooperative {

    @JsonProperty("cnpj_cooperativa")
    private String cnpj;

    @JsonProperty("nome_cooperativa")
    private String name;

    @JsonProperty("email_cooperativa")
    private String email;

    @JsonProperty("senha_cooperativa")
    private String password;

    public Cooperative(String cnpj, String name, String email, String password) {
        this.cnpj = cnpj;
        this.name = name;
        this.email = email;
        this.password = password;
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
}
