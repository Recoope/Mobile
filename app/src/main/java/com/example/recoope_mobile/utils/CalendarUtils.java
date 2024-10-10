package com.example.recoope_mobile.utils;

import android.app.DatePickerDialog;
import android.content.Context;

import java.util.Calendar;

public class CalendarUtils {

    public static void openDatePickerDialog(Context context, DateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedMonth = selectedMonth + 1;

                    String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth, selectedYear);

                    listener.onDateSelected(selectedDate);
                },
                year, month, day);

        datePickerDialog.show();
    }
}
