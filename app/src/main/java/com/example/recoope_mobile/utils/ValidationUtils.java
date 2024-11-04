package com.example.recoope_mobile.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidationUtils {

    // Verifica se a data é maior ou igual à data atual
    public static boolean isValidDate(String dateStr) {
        if(dateStr.isEmpty()){
            return true;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Ajuste o formato da data conforme necessário
        try {
            LocalDate chosenDate = LocalDate.parse(dateStr, formatter);
            LocalDate currentDate = LocalDate.now();
            return !chosenDate.isBefore(currentDate);  // Verifica se a data não é antes de hoje
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido!");
            return false;  // Retorna falso se a data estiver em um formato inválido
        }
    }

    // Verifica se o peso é um número positivo
    public static boolean isValidWeight(String weightStr) {
        if(weightStr.isEmpty()){
            return true;
        }
        try {
            double weight = Double.parseDouble(weightStr);
            return weight > 0;  // Peso precisa ser maior que zero
        } catch (NumberFormatException e) {
            System.out.println("Peso inválido!");
            return false;  // Retorna falso se o valor não for um número
        }
    }

    public static String truncateString(String input, int maxChars) {
        if (input == null || input.length() <= maxChars) {
            return input; // Retorna a string original se for nula ou não exceder o limite
        }
        return input.substring(0, maxChars) + "..."; // Retorna a string truncada com "..."
    }



}
