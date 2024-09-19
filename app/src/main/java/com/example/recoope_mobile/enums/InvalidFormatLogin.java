package com.example.recoope_mobile.enums;

public enum InvalidFormatLogin {

    NO_MATCHING_CNPJ_OR_INCORRECT_PASSWORD("O CNPJ fornecido não possui uma correspondência ou a senha está incorreta."),
    NO_MATCHING_EMAIL_OR_INCORRECT_PASSWORD("O E-mail fornecido não possui uma correspondência ou a senha está incorreta."),
    EMAIL_CNPJ_INVALID("Parâmetro fornecido não é um E-mail ou CNPJ."),
    LOGIN_SUCCESS("Login realizado com sucesso!");


    private final String type;

    InvalidFormatLogin(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static InvalidFormatLogin fromType(String type) {
        for (InvalidFormatLogin respost : InvalidFormatLogin.values()) {
            if (respost.getType().equalsIgnoreCase(type)) {
                return respost;
            }
        }
        throw new IllegalArgumentException("Unrecognized response type: " + type);
    }

}
