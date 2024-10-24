package com.example.recoope_mobile.model;
import com.google.gson.annotations.SerializedName;

public class Payment {

    private Long id;

    @SerializedName("dataEmissao")
    private String emissionDate;

    @SerializedName("horaEmissao")
    private String emissionTime;

    @SerializedName("nomeCooperativa")
    private String cooperativeName;

    @SerializedName("cnpjCooperativa")
    private String cooperativeCnpj;

    @SerializedName("nomeEmpresa")
    private String companyName;

    @SerializedName("cnpjEmpresa")
    private String companyCnpj;

    @SerializedName("valor")
    private Double amount;

    public Payment() {}

    public Payment(Long id, String emissionDate, String emissionTime, String cooperativeName,
                   String cooperativeCnpj, String companyName, String companyCnpj, Double amount) {
        this.id = id;
        this.emissionDate = emissionDate;
        this.emissionTime = emissionTime;
        this.cooperativeName = cooperativeName;
        this.cooperativeCnpj = cooperativeCnpj;
        this.companyName = companyName;
        this.companyCnpj = companyCnpj;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmissionDate() {
        return emissionDate;
    }

    public void setEmissionDate(String emissionDate) {
        this.emissionDate = emissionDate;
    }

    public String getEmissionTime() {
        return emissionTime;
    }

    public void setEmissionTime(String emissionTime) {
        this.emissionTime = emissionTime;
    }

    public String getCooperativeName() {
        return cooperativeName;
    }

    public void setCooperativeName(String cooperativeName) {
        this.cooperativeName = cooperativeName;
    }

    public String getCooperativeCnpj() {
        return cooperativeCnpj;
    }

    public void setCooperativeCnpj(String cooperativeCnpj) {
        this.cooperativeCnpj = cooperativeCnpj;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCnpj() {
        return companyCnpj;
    }

    public void setCompanyCnpj(String companyCnpj) {
        this.companyCnpj = companyCnpj;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", emissionDate=" + emissionDate +
                ", emissionTime=" + emissionTime +
                ", cooperativeName='" + cooperativeName + '\'' +
                ", cooperativeCnpj='" + cooperativeCnpj + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyCnpj='" + companyCnpj + '\'' +
                ", amount=" + amount +
                '}';
    }
}
