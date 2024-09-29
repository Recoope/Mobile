package com.example.recoope_mobile.model;

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

    @JsonProperty("address")
    private Address adress;

    @JsonProperty("product")
    private Product product;

    @JsonProperty("cooperative")
    private Cooperative cooperative;

    public Auction(int auctionId, String startDate, String endDate, String details, String time, String isFinished, int addressId, int productId, String cooperativeCnpj, Product product, Cooperative cooperative, Address address) {
        this.auctionId = auctionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.details = details;
        this.time = time;
        this.isFinished = isFinished;
        this.adress = address;
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

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }

    public Address getAdress() {
        return adress;
    }

    public void setAdress(Address adress) {
        this.adress = adress;
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
}
