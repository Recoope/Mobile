package com.example.recoope_mobile.model;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName(value="id")
    private int productId;

    @SerializedName(value="tipo")
    private String productType;

    @SerializedName(value="valorInicial")
    private double initialValue;

    @SerializedName(value="valorFinal")
    private double finalValue;

    @SerializedName(value="peso")
    private Double weight;

    @SerializedName(value="foto")
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
