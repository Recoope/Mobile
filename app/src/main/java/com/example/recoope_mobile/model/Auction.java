package com.example.recoope_mobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Auction {

    @SerializedName(value="id")
    private int auctionId;

    @SerializedName(value="dataInicio")
    private Date startDate;

    @SerializedName(value="dataFim")
    private Date endDate;

    @SerializedName(value="detalhes")
    private String details;

    @SerializedName(value="hora")
    private String time;

    @SerializedName(value="endereco")
    private Address address;

    @SerializedName(value="produto")
    private Product product;

    @SerializedName(value="cooperativa")
    private Cooperative cooperative;

    public Auction(int auctionId, Date startDate, Date endDate, String details, String time, String isFinished, int addressId, int productId, String cooperativeCnpj, Product product, Cooperative cooperative, Address address) {
        this.auctionId = auctionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.details = details;
        this.time = time;
        this.address = address;
        this.product = product;
        this.cooperative = cooperative;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public Address getAdress() {
        return address;
    }

    public void setAdress(Address adress) {
        this.address = adress;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cooperative getCooperative() {
        return cooperative;
    }

    public void setCooperative(Cooperative cooperative) {
        this.cooperative = cooperative;
    }

    @Override
    public String toString() {
        return "Auction{" +
                "auctionId=" + auctionId +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", details='" + details + '\'' +
                ", time='" + time + '\'' +
                ", adress=" + address +
                ", product=" + product +
                ", cooperative=" + cooperative +
                '}';
    }
}
