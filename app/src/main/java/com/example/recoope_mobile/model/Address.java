package com.example.recoope_mobile.model;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName(value="idEndereco")
    private int addressId;

    @SerializedName(value="cidade")
    private String city;

    @SerializedName(value="bairro")
    private String neighborhood;

    @SerializedName(value="rua")
    private String street;

    @SerializedName(value="numero")
    private int number;

    public Address(int addressId, String city, String neighborhood, String street, int number) {
        this.addressId = addressId;
        this.city = city;
        this.neighborhood = neighborhood;
        this.street = street;
        this.number = number;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
