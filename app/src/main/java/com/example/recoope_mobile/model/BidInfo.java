package com.example.recoope_mobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class BidInfo {
    @SerializedName("cnpjEmpresa")
    private String companyCnpj;

    @SerializedName("valor")
    private double value;

    public BidInfo(String companyCnpj, double value) {
        this.companyCnpj = companyCnpj;
        this.value = value;
    }

    public String getCompanyCnpj() {
        return companyCnpj;
    }

    public void setCompanyCnpj(String companyCnpj) {
        this.companyCnpj = companyCnpj;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
