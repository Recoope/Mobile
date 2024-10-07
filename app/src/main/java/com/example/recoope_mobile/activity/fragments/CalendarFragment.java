package com.example.recoope_mobile.activity.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.adapter.ExpiringAuctionAdapter;
import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.response.ApiDataResponseAuction;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint({"MissingInflatedId", "DefaultLocale", "ResourceAsColor"})
public class CalendarFragment extends Fragment {
    private String cnpj;
    private String todayDayOfWeek, todayMonth;
    private int todayDay, todayYear;
    private GridLayout gridLayout;
    private TextView monthTextView, yearTextView;
    private Calendar calendar;
    private ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
    private final List<Date> expiringDates = new ArrayList<>();
    ExpiringAuctionAdapter expiringAuctionAdapter;
    RecyclerView inProgressRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar, container, false);

        inProgressRecycler = view.findViewById(R.id.calendarAuctions);
        gridLayout = view.findViewById(R.id.gridLayout);
        monthTextView = view.findViewById(R.id.textMonth);
        yearTextView = view.findViewById(R.id.textYear);
        calendar = Calendar.getInstance();

        todayDayOfWeek = getDayOfWeekPtBr();
        todayDay = calendar.get(Calendar.DAY_OF_MONTH);
        todayMonth = getMonthPtBr().toLowerCase();
        todayYear = getYear();

        cnpj = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("cnpj", "");

        // Preenchendo leilões em andamento.
        Call c = apiService.getParticipations(cnpj);
        c.enqueue(new Callback<ApiDataResponseAuction<List<Auction>>>() {
            @Override
            public void onResponse(Call<ApiDataResponseAuction<List<Auction>>> call, Response<ApiDataResponseAuction<List<Auction>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiDataResponseAuction<List<Auction>> apiResponse = response.body();

                    if (apiResponse.getData() != null && !apiResponse.getData().isEmpty()) {
                        Log.d("AuctionData", "Data fetched: " + apiResponse.getData().size());
                        expiringAuctionAdapter = new ExpiringAuctionAdapter(apiResponse.getData(), getContext());
                        inProgressRecycler.setAdapter(expiringAuctionAdapter);
                    } else {
                        Log.d("AuctionData", "No auctions found for this date.");
                        Toast.makeText(getContext(), "No auctions found.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponseAuction<List<Auction>>> call, Throwable t) {
                // Log.e(LOG_TAG, "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch data.", Toast.LENGTH_SHORT).show();
            }
        });

        // Colocando data do dia.
        ((TextView) view.findViewById(R.id.textDate)).setText(
                String.format("%s, %d de %s", todayDayOfWeek, todayDay, todayMonth)
        );

        // Resgatando datas de expiração.
        Call<ApiDataResponseAuction<List<Date>>> call = apiService.getExpiringDates(cnpj);

        call.enqueue(new Callback<ApiDataResponseAuction<List<Date>>>() {
            @Override
            public void onResponse(Call<ApiDataResponseAuction<List<Date>>> call, Response<ApiDataResponseAuction<List<Date>>> response) {
                expiringDates.clear();
                ApiDataResponseAuction<List<Date>> data = response.body();
                if (data != null) expiringDates.addAll(data.getData());
                updateCalendar();
            }

            @Override
            public void onFailure(Call<ApiDataResponseAuction<List<Date>>> call, Throwable t) {}
        });

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

        // Limitar calendario, para começar em janeiro de 2024.
        if (getYear() == 2023 && getMonthPtBr().equals("Dezembro")) {
            calendar.add(Calendar.MONTH, 1);
            return;
        }

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

    private void addDayToGridLayout(int day) {
        String dayText = day != 0 ? String.valueOf(day) : "";

        TextView dayTextView = new TextView(getContext());
        dayTextView.setText(dayText);
        dayTextView.setGravity(Gravity.CENTER);
        dayTextView.setTextSize(16f);
        dayTextView.setPadding(0, 0, 0, 0);
        dayTextView.setTypeface(null, Typeface.BOLD);

        // Verificar se o dia está na lista expiringDates e mudar o background para verde
        if (day != 0) {
            Calendar currentCalendar = (Calendar) calendar.clone();
            currentCalendar.set(Calendar.DAY_OF_MONTH, day);

            for (Date expiringDate : expiringDates) {
                Calendar expiringCalendar = Calendar.getInstance();
                expiringCalendar.setTime(expiringDate);

                if (currentCalendar.get(Calendar.YEAR) == expiringCalendar.get(Calendar.YEAR) &&
                        currentCalendar.get(Calendar.MONTH) == expiringCalendar.get(Calendar.MONTH) &&
                        currentCalendar.get(Calendar.DAY_OF_MONTH) == expiringCalendar.get(Calendar.DAY_OF_MONTH)) {

                    dayTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    dayTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_rounded_calendar));
                    dayTextView.setOnClickListener((view) -> openAuctionsExpiriredOn(getDayOfWeekPtBr(), dayText));
                    break;
                }
            }
        }

        // Mudando a cor do dia atual.
        if (day == todayDay &&
                getMonthPtBr().equalsIgnoreCase(todayMonth) &&
                getYear() == todayYear) {
            dayTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.recoope_light_blue_color));
        }

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = GridLayout.LayoutParams.WRAP_CONTENT;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

        dayTextView.setLayoutParams(params);

        gridLayout.addView(dayTextView);
    }

    private void openAuctionsExpiriredOn(String dw, String day) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View bidsDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bid, null);

        bottomSheetDialog.setContentView(bidsDialog);

        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog bottomDialog = (BottomSheetDialog) dialog;
            View bottomSheet = bottomDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                bottomSheet.getLayoutParams().height = 500 * getResources().getDisplayMetrics().densityDpi / 160;
                bottomSheet.requestLayout();
            }
        });

        ((TextView) bidsDialog.findViewById(R.id.yearText)).setText(String.valueOf(getYear()));
        ((TextView) bidsDialog.findViewById(R.id.dayText)).setText(
                String.format("%s, %s de %s", dw, day, getMonthPtBr())
        );

        RecyclerView recycler = bidsDialog.findViewById(R.id.bidRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dataFormated = dateFormat.format(new Date(getYear() - 1900, getMonth(), Integer.parseInt(day)));

        Call call = apiService.getParticipationsByExpiringDate(cnpj, dataFormated);
        call.enqueue(new Callback<ApiDataResponseAuction<List<Auction>>>() {
            @Override
            public void onResponse(Call<ApiDataResponseAuction<List<Auction>>> call, Response<ApiDataResponseAuction<List<Auction>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiDataResponseAuction<List<Auction>> apiResponse = response.body();

                    if (apiResponse.getData() != null && !apiResponse.getData().isEmpty()) {
                        Log.d("AuctionData", "Data fetched: " + apiResponse.getData().size());
                        expiringAuctionAdapter = new ExpiringAuctionAdapter(apiResponse.getData(), getContext());
                        recycler.setAdapter(expiringAuctionAdapter);
                    } else {
                        Log.d("AuctionData", "No auctions found for this date.");
                        Toast.makeText(getContext(), "No auctions found.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponseAuction<List<Auction>>> call, Throwable t) {
                // Log.e(LOG_TAG, "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch data.", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.show();
    }

    private String getDayOfWeekPtBr() {
        String dow = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
        return Translate.from(dow);
    }

    private String getMonthPtBr() {
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        return Translate.from(month);
    }

    private int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    private int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    private enum Translate {
        SUNDAY("Dom"),
        MONDAY("Seg"),
        TUESDAY("Ter"),
        WEDNESDAY("Qua"),
        THURSDAY("Qui"),
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