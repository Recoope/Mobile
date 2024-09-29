package com.example.recoope_mobile.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {

    @JsonProperty("addressId")
    private int addressId;

    @JsonProperty("city")
    private String city;

    @JsonProperty("street")
    private String street;

    @JsonProperty("number")
    private int number;

    public Address(int addressId, String city, String street, int number) {
        this.addressId = addressId;
        this.city = city;
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
