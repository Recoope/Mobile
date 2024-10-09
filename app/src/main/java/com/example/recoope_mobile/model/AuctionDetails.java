package com.example.recoope_mobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class AuctionDetails {

    @SerializedName(value="id")
    private int auctionId;

    @SerializedName(value="dataInicio")
    private String startDate;

    @SerializedName(value="dataFim")
    private String endDate;

    @SerializedName(value="detalhes")
    private String details;

    @SerializedName(value="hora")
    private String time;

    @SerializedName(value = "tempoRestante")
    private String remainingTime;

    @SerializedName(value = "maiorLance")
    private Bid bestBid;

    @SerializedName(value="endereco")
    private Address address;

    @SerializedName(value="produto")
    private Product product;

    @SerializedName(value="cooperativa")
    private Cooperative cooperative;

    public AuctionDetails(int auctionId, String startDate, String endDate, String details, String time, String isFinished, int addressId, int productId, String cooperativeCnpj, String remainingTime, Bid bestBid, Product product, Cooperative cooperative, Address address) {
        this.auctionId = auctionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.details = details;
        this.time = time;
        this.remainingTime = remainingTime;
        this.bestBid = bestBid;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
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

    public String getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Bid getBestBid() {
        return bestBid;
    }

    public void setBestBid(Bid bestBid) {
        this.bestBid = bestBid;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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