package com.example.recoope_mobile.model;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName(value="idProduto")
    private int productId;

    @SerializedName(value="tipoProduto")
    private String productType;

    @SerializedName(value="valorInicialProduto")
    private double initialValue;

    @SerializedName(value="valorFinalProduto")
    private double finalValue;

    @SerializedName(value="peso")
    private Double weight;

    @SerializedName(value="fotoLeilao")
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
