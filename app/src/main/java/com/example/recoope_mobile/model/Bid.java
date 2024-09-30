package com.example.recoope_mobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bid {

    @JsonProperty("bidId")
    private int bidId;

    @JsonProperty("auctionId")
    private int auctionId;

    @JsonProperty("companyCnpj")
    private String companyCnpj;

    @JsonProperty("value")
    private double value;

    @JsonProperty("bidDate")
    private String bidDate;

    public Bid(int bidId, int auctionId, String companyCnpj, double value, String bidDate) {
        this.bidId = bidId;
        this.auctionId = auctionId;
        this.companyCnpj = companyCnpj;
        this.value = value;
        this.bidDate = bidDate;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
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

    public String getBidDate() {
        return bidDate;
    }

    public void setBidDate(String bidDate) {
        this.bidDate = bidDate;
    }
}
