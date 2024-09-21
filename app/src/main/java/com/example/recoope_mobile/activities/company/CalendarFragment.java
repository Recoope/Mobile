package com.example.recoope_mobile.activities.company;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.recoope_mobile.R;

import java.util.Calendar;

public class CalendarFragment extends Fragment {
    private GridLayout gridLayout;
    private TextView dateTextView, monthTextView, yearTextView;
    private Calendar calendar;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.company_calendar, container, false);

        gridLayout = view.findViewById(R.id.gridLayout);
        monthTextView = view.findViewById(R.id.textMonth);
        yearTextView = view.findViewById(R.id.textYear);
        calendar = Calendar.getInstance();

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

        String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, getResources().getConfiguration().locale);
        int year = calendar.get(Calendar.YEAR);
        monthTextView.setText(Translate.fromMonth(monthName));
        yearTextView.setText(String.valueOf(year));

        for (int i = 0; i < firstDayOfWeek; i++) addDayToGridLayout(0);
        for (int day = 1; day <= daysInMonth; day++) addDayToGridLayout(day);
    }

    private void addDayToGridLayout(int day) {
        String dayText = day != 0 ? String.valueOf(day) : "";

        TextView dayTextView = new TextView(getContext());
        dayTextView.setText(dayText);
        dayTextView.setGravity(Gravity.CENTER);
        dayTextView.setTextSize(16f);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

        dayTextView.setLayoutParams(params);

        gridLayout.addView(dayTextView);
    }

    public enum Translate {
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

        private final String month;
        Translate(String month) { this.month = month; }

        public String getMonth() {
            return month;
        }

        public static String fromMonth(String month) {

            for (Translate respost : Translate.values()) {
                if (respost.toString().equalsIgnoreCase(month)) {
                    return respost.getMonth();
                }
            }
            throw new IllegalArgumentException("Unrecognized response month: " + month);
        }

    }
}