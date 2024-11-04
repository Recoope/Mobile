package com.example.recoope_mobile.enums;

public enum InvalidFormatRegister {
    NULL_PARAMETERS("Não devem ser enviados parametros nulos."),
    LOGIN_SUCCESS("Login realizado com sucesso!"),
    PASSWORD_DOES_NOT_MATCH("As senhas não correspondem."),
    INVALID_CNPJ("CNPJ inválido."),
    EXISTING_CNPJ("CNPJ já existente."),
    INVALID_COMPANY_NAME("O nome da empresa deve conter pelo menos 3 caracteres e no máximo 255 caracteres."),
    EXISTING_EMAIL("Email já existente."),
    INVALID_EMAIL("Email inválido."),
    EXISTING_PHONE_NUMBER("Telefone já existente."),
    INVALID_PHONE_NUMBER("Telefone inválido."),
    INVALID_PASSWORD("A senha deve ter pelo menos 8 caracteres, um número e um caractere especial.");

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
