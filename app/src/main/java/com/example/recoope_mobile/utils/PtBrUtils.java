package com.example.recoope_mobile.utils;

import java.sql.Time;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PtBrUtils {

    public static String getRemaingTimeMsgPTBR(Date endDate, Time endHour) {

        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(endDate);
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(endHour);

        dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));

        Date remainingTime = dateCal.getTime();

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
