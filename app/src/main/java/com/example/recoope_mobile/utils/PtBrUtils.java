package com.example.recoope_mobile.utils;

import android.annotation.SuppressLint;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.recoope_mobile.R;

import java.sql.Time;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(real).replace("\u00A0", "");
    }

    public static String formatWeight(double weight) {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        return formatter.format(weight) + " kg";
    }

    public static Date parseDate(String dateString) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            return originalFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String formatDate(Date date) {
        if(date == null){
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public static String formatId(int id) {
        return String.format("#%04d", id);
    }

    @SuppressLint("ResourceAsColor")
    public static void formatAuctionStatus(int status, TextView statusTv) {
        switch (status) {
            case 0:
                statusTv.setText("Leilão vencido!");
                statusTv.setTextColor(ContextCompat.getColor(statusTv.getContext(), R.color.recoope_primary_color));
                break;
            case 1:
                statusTv.setText("Maior lance é seu!");
                statusTv.setTextColor(ContextCompat.getColor(statusTv.getContext(), R.color.recoope_light_blue_color));
                break;
            case 2:
                statusTv.setText("Leilão encerrado");
                statusTv.setTextColor(ContextCompat.getColor(statusTv.getContext(), R.color.recoope_red_color));
                break;
            case 3:
                statusTv.setText("Lance superado!");
                statusTv.setTextColor(ContextCompat.getColor(statusTv.getContext(), R.color.recoope_yellow_color));
                break;
            case 4:
                statusTv.setText("Status não disponível");
                break;
        }
    }
}
