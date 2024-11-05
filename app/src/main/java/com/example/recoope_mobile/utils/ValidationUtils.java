package com.example.recoope_mobile.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

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



    // Método para truncar a string dinamicamente com base no tamanho da tela
    public static String truncateString(Context context, String input, int cardWidthDp) {
        Log.e("truncate", "cardWithDp : "+ cardWidthDp);
        int maxChars = calculateMaxChars(context, cardWidthDp);
        Log.e("truncate", "maxChars : "+ maxChars);
        if (input == null || input.length() <= maxChars) {
            return input; // Retorna a string original se for nula ou não exceder o limite
        }
        return input.substring(0, maxChars) + "..."; // Retorna a string truncada com "..."
    }

    // Método para calcular o número máximo de caracteres com base na largura em dp
    private static int calculateMaxChars(Context context, int cardWidthDp) {
        float averageCharWidthDp = 10; // Largura média de um caractere em dp (ajuste conforme necessário)
        return (int) (cardWidthDp / averageCharWidthDp);
    }

    // Método para converter pixels em dp (com base na densidade da tela)
    public static int convertPxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (px / displayMetrics.density);
    }

    // Método para obter a largura da tela em dp
    public static int getScreenWidthDp(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return convertPxToDp(context, displayMetrics.widthPixels);
    }

    public static int calculateCardWidthDp(Context context, double proportion) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidthDp = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return (int) (screenWidthDp * proportion);  // Proporção da largura da tela para `cardWidthDp`
    }
}




