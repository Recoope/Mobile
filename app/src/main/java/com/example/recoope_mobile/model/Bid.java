package com.example.recoope_mobile.model;

import com.google.gson.annotations.SerializedName;

public class Bid {

    @SerializedName("id")
    private int bidId;

    @SerializedName("empresa")
    private Company auction;

    @SerializedName("valor")
    private double value;

    @SerializedName("data")
    private String bidDate;

    public Bid(int bidId, Company auction, double value, String bidDate) {
        this.bidId = bidId;
        this.auction = auction;
        this.value = value;
        this.bidDate = bidDate;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public Company getAuction() {
        return auction;
    }

    public void setAuction(Company auction) {
        this.auction = auction;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getBidDate() {
        return bidDate;
    }

    public void setBidDate(String bidDate) {
        this.bidDate = bidDate;
    }
}
