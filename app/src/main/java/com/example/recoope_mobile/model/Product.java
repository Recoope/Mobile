package com.example.recoope_mobile.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

    @JsonProperty("productId")
    private int productId;

    @JsonProperty("productType")
    private String productType;

    @JsonProperty("initialValue")
    private double initialValue;

    @JsonProperty("finalValue")
    private double finalValue;

    @JsonProperty("weight")
    private double weight;

    @JsonProperty("photo")
    private String photo;

    public Product(int productId, String productType, double initialValue, double finalValue, double weight, String photo) {
        this.productId = productId;
        this.productType = productType;
        this.initialValue = initialValue;
        this.finalValue = finalValue;
        this.weight = weight;
        this.photo = photo;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(double initialValue) {
        this.initialValue = initialValue;
    }

    public double getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(double finalValue) {
        this.finalValue = finalValue;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
