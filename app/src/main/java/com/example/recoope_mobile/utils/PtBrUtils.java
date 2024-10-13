package com.example.recoope_mobile.utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PtBrUtils {

    public static String getRemaingTimeMsgPTBR(Date remainingTime) {

        Date now = new Date();
        long difMillis = remainingTime.getTime() - now.getTime();

        long seconds = difMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days + " dias.";
        } else {
            long remainingHours = hours % 24;
            long remainingMinutes = minutes % 60;
            long remainingSeconds = seconds % 60;

            return String.format("%02dh %02dm %02ds", remainingHours, remainingMinutes, remainingSeconds);
        }
    }

    public static String formatReal(double real) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatter.format(real);
    }

    public static String formatWeight(double weight) {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        return formatter.format(weight) + " kg";
    }

    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public static String formatId(int id) {
        return String.format("#%04d", id);
    }
}
