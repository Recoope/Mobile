package com.example.recoope_mobile.enums;

public enum InvalidFormatRegister {
    NAME_MIN("O nome da empresa deve conter pelo menos 3 caracteres."),
    NAME_MAX("O nome da empresa deve conter no máximo 255 caracteres."),
    CNPJ("CNPJ inválido."),
    EMAIL("Email já existente."),
    PHONE("Telefone inválido."),
    PASSWORD("As senhas não correspondem.");


    private final String type;

    InvalidFormatRegister(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static InvalidFormatRegister fromType(String type) {
        for (InvalidFormatRegister respost : InvalidFormatRegister.values()) {
            if (respost.getType().equalsIgnoreCase(type)) {
                return respost;
            }
        }
        throw new IllegalArgumentException("Unrecognized response type: " + type);
    }


}
