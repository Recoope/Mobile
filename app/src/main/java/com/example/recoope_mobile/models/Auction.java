package com.example.recoope_mobile.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Auction {

    @JsonProperty("auctionId")
    private int auctionId;

    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("details")
    private String details;

    @JsonProperty("time")
    private String time;

    @JsonProperty("isFinished")
    private String isFinished;

    @JsonProperty("addressId")
    private int addressId;

    @JsonProperty("productId")
    private int productId;

    @JsonProperty("cooperativeCnpj")
    private String cooperativeCnpj;

    public Auction(int auctionId, String startDate, String endDate, String details, String time, String isFinished, int addressId, int productId, String cooperativeCnpj) {
        this.auctionId = auctionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.details = details;
        this.time = time;
        this.isFinished = isFinished;
        this.addressId = addressId;
        this.productId = productId;
        this.cooperativeCnpj = cooperativeCnpj;
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

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCooperativeCnpj() {
        return cooperativeCnpj;
    }

    public void setCooperativeCnpj(String cooperativeCnpj) {
        this.cooperativeCnpj = cooperativeCnpj;
    }
}
