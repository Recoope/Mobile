package com.example.recoope_mobile.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cooperative {

    @JsonProperty("cnpj_cooperativa")
    private String cnpj;

    @JsonProperty("nome_cooperativa")
    private String name;

    @JsonProperty("email_cooperativa")
    private String email;

    @JsonProperty("senha_cooperativa")
    private String passoword;

}
