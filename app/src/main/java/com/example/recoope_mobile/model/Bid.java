package com.example.recoope_mobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Bid {

    @SerializedName("bidId")
    private int bidId;

    @SerializedName("auctionId")
    private int auctionId;

    @SerializedName("companyCnpj")
    private String companyCnpj;

    @SerializedName("value")
    private double value;

    @SerializedName("bidDate")
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
