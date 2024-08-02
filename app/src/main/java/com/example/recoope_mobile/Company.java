package com.example.recoope_mobile;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class Company {

    private String cnpj;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String record;

    public Company(String cnpj, String name, String email, String password, String phone) {
        this.cnpj = cnpj;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.record = currentDate();
    }

    public String currentDate(){
        LocalDate localDate = LocalDate.now();
        String formattedDate = localDate.toString();
        return formattedDate;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRecord() {
        return record;
    }



}
