package com.example.recoope_mobile.enums;

public enum InvalidFormatUpdate {

    COMPANY_NOT_FOUND("Empresa não encontrada."),
    INVALID_NAME("O nome da empresa deve conter pelo menos 3 caracteres e no máximo 255 caracteres."),
    EMAIL_ALREADY_EXISTS("Email já existente."),
    INVALID_EMAIL("Email inválido."),
    PHONE_ALREADY_EXISTS("Telefone já existente."),
    INVALID_PHONE("Telefone inválido."),
    COMPANY_UPDATED("Empresa atualizada com sucesso!");
    private final String message;

    InvalidFormatUpdate(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static InvalidFormatUpdate fromType(String message) {
        for (InvalidFormatUpdate response : InvalidFormatUpdate.values()) {
            if (response.getMessage().equalsIgnoreCase(message)) {
                return response;
            }
        }
        throw new IllegalArgumentException("Unrecognized response message: " + message);
    }


}
