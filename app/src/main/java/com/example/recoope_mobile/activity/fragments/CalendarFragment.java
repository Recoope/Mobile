package com.example.recoope_mobile.activity.company;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.recoope_mobile.R;

import java.util.Calendar;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    private String todayDayOfWeek, todayMonth;
    private int todayDay, todayYear;
    private GridLayout gridLayout;
    private TextView monthTextView, yearTextView;
    private Calendar calendar;

    @SuppressLint({"MissingInflatedId", "DefaultLocale"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar, container, false);

        gridLayout = view.findViewById(R.id.gridLayout);
        monthTextView = view.findViewById(R.id.textMonth);
        yearTextView = view.findViewById(R.id.textYear);
        calendar = Calendar.getInstance();

        todayDayOfWeek = getDayOfWeekPtBr();
        todayDay = calendar.get(Calendar.DAY_OF_MONTH);
        todayMonth = getMonthPtBr().toLowerCase();
        todayYear = getYear();

        ((TextView) view.findViewById(R.id.textDate)).setText(
                String.format("%s, %d de %s", todayDayOfWeek, todayDay, todayMonth)
        );

        updateCalendar();

        view.findViewById(R.id.prevMonth).setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        view.findViewById(R.id.nextMonth).setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        return view;
    }

    private void updateCalendar() {
        gridLayout.removeAllViews();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        String monthName = getMonthPtBr();
        int year = getYear();
        monthTextView.setText(monthName);
        yearTextView.setText(String.valueOf(year));

        for (int i = 0; i < firstDayOfWeek; i++) addDayToGridLayout(0);
        for (int day = 1; day <= daysInMonth; day++) addDayToGridLayout(day);
    }

    @SuppressLint("ResourceAsColor")
    private void addDayToGridLayout(int day) {
        String dayText = day != 0 ? String.valueOf(day) : "";

        TextView dayTextView = new TextView(getContext());
        dayTextView.setText(dayText);
        dayTextView.setGravity(Gravity.CENTER);
        dayTextView.setTextSize(16f);
        dayTextView.setTypeface(null, Typeface.BOLD);

        // Mudando cor do dia atual.
        if (day == todayDay &&
            getMonthPtBr().equalsIgnoreCase(todayMonth) &&
            getYear() == todayYear) {
            dayTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.recoope_light_blue_color));
        }

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

        dayTextView.setLayoutParams(params);

        gridLayout.addView(dayTextView);
    }

    private String getDayOfWeekPtBr() {
        String dow = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
        return Translate.from(dow);
    }

    private String getMonthPtBr() {
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        return Translate.from(month);
    }

    private int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    private enum Translate {
        SUNDAY("Dom"),
        MONDAY("Dom"),
        TUESDAY("Dom"),
        WEDNESDAY("Dom"),
        THURSDAY("Dom"),
        FRIDAY("Sex"),
        SATURDAY("Sab"),
        JANUARY("Janeiro"),
        FEBRUARY("Fevereiro"),
        MARCH("Março"),
        APRIL("Abril"),
        MAY("Maio"),
        JUNE("Junho"),
        JULY("Julho"),
        AUGUST("Agosto"),
        SEPTEMBER("Setembro"),
        OCTOBER("Outubro"),
        NOVEMBER("Novembro"),
        DECEMBER("Dezembro");

        private final String translation;
        Translate(String translation) { this.translation = translation; }

        public String getTranslation() {
            return translation;
        }

        public static String from(String month) {

            for (Translate respost : Translate.values()) {
                if (respost.toString().equalsIgnoreCase(month)) {
                    return respost.getTranslation();
                }
            }
            throw new IllegalArgumentException("Unrecognized response month: " + month);
        }

    }
}