package com.example.recoope_mobile.model;

import com.google.gson.annotations.SerializedName;

public class CompanyProfile {

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

    @SerializedName(value = "leiloesParticipados")
    private String participatedAuctions;

    public CompanyProfile(String cnpj, String name, String email, String password, String phone, String passwordConfirmation, String participatedAuctions) {
        this.cnpj = cnpj;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.passwordConfirmation = passwordConfirmation;
        this.participatedAuctions = participatedAuctions;
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

    public String getParticipatedAuctions() {
        return participatedAuctions;
    }

    public void setParticipatedAuctions(String participatedAuctions) {
        this.participatedAuctions = participatedAuctions;
    }

    @Override
    public String toString() {
        return "CompanyProfile{" +
                "cnpj='" + cnpj + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", passwordConfirmation='" + passwordConfirmation + '\'' +
                ", participatedAuctions='" + participatedAuctions + '\'' +
                '}';
    }
}