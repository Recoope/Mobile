package com.example.recoope_mobile.activity.fragments;
import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.LoggerClient;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.activity.MainActivity;
import com.example.recoope_mobile.adapter.ParticipateAuctionAdapter;
import com.example.recoope_mobile.model.ParticipatedAuction;
import com.example.recoope_mobile.response.ApiDataResponse;
import com.example.recoope_mobile.utils.StatusUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.text.SimpleDateFormat;
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
    private List<Date> expiringDates;
    ParticipateAuctionAdapter expiringAuctionAdapter;
    ParticipateAuctionAdapter auctionParticipationsAdapter;
    RecyclerView inProgressRecycler;
    private TextView pendingText;
    private ImageView messageStatus;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LoggerClient.postLog(getContext(), "CALENDAR");
        View view = inflater.inflate(R.layout.calendar, container, false);

        expiringDates = new ArrayList<>();
        inProgressRecycler = view.findViewById(R.id.calendarAuctions);
        inProgressRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        pendingText = view.findViewById(R.id.pendingText);
        pendingText.setVisibility(View.INVISIBLE);

        messageStatus = view.findViewById(R.id.messageStatusCalendar);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidthDp = Math.round(displayMetrics.widthPixels / displayMetrics.density);

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

        activity = (MainActivity) getActivity();

        // Criando calendario.
        updateCalendar();

        view.findViewById(R.id.prevMonth).setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        view.findViewById(R.id.nextMonth).setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        // Preenchendo leilões em andamento.
        Call c = apiService.getParticipations(cnpj);
        c.enqueue(new Callback<ApiDataResponse<List<ParticipatedAuction>>>() {
            @Override
            public void onResponse(Call<ApiDataResponse<List<ParticipatedAuction>>> call, Response<ApiDataResponse<List<ParticipatedAuction>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    activity.hideLoading();
                    StatusUtils.hideStatusImage(messageStatus);
                    ApiDataResponse<List<ParticipatedAuction>> apiResponse = response.body();
                    if (apiResponse.getData() != null && !apiResponse.getData().isEmpty()) {
                        Log.d("AuctionData", "Data fetched: " + apiResponse.getData().size());
                        auctionParticipationsAdapter = new ParticipateAuctionAdapter(apiResponse.getData(), getContext(), screenWidthDp);
                        pendingText.setVisibility(View.VISIBLE);
                        inProgressRecycler.setAdapter(auctionParticipationsAdapter);
                    }
                }else{
                    if(response.code() == 500){
                        StatusUtils.showStatusImage(messageStatus, StatusUtils.STATUS_SERVER_ERROR);
                    }else {
                        StatusUtils.hideStatusImage(messageStatus);
                        StatusUtils.showStatusImage(messageStatus, StatusUtils.STATUS_NO_DATA);
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiDataResponse<List<ParticipatedAuction>>> call, Throwable t) {
                StatusUtils.hideStatusImage(messageStatus);
                Log.e("API_ERROR", "Failed to load data: " + t.getMessage(), t);
                StatusUtils.showStatusImage(messageStatus, StatusUtils.STATUS_SERVER_ERROR);
            }
        });
        // Colocando data do dia.
        ((TextView) view.findViewById(R.id.textDate)).setText(
                String.format("%s, %d de %s", todayDayOfWeek, todayDay, todayMonth)
        );
        // Resgatando datas de expiração.
        Call<ApiDataResponse<List<Date>>> call = apiService.getExpiringDates(cnpj);
        call.enqueue(new Callback<ApiDataResponse<List<Date>>>() {
            @Override
            public void onResponse(Call<ApiDataResponse<List<Date>>> call, Response<ApiDataResponse<List<Date>>> response) {
                activity.hideLoading();
                expiringDates.clear();
                ApiDataResponse<List<Date>> data = response.body();
                if (data != null) expiringDates.addAll(data.getData());
                updateCalendar();
            }
            @Override
            public void onFailure(Call<ApiDataResponse<List<Date>>> call, Throwable t) {}
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
            boolean isExpiringDay = false;
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
                    isExpiringDay = true;
                    break;
                }
            }
            // Mudando a cor do dia atual.
            if (day == todayDay &&
                    getMonthPtBr().equalsIgnoreCase(todayMonth) &&
                    getYear() == todayYear && !isExpiringDay) {
                dayTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.recoope_light_blue_color));
            }
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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidthDp = Math.round(displayMetrics.widthPixels / displayMetrics.density);
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
        activity = (MainActivity) getActivity();
        Call call = apiService.getParticipationsByExpiringDate(cnpj, dataFormated);
        call.enqueue(new Callback<ApiDataResponse<List<ParticipatedAuction>>>() {
            @Override
            public void onResponse(Call<ApiDataResponse<List<ParticipatedAuction>>> call, Response<ApiDataResponse<List<ParticipatedAuction>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    activity.hideLoading();
                    ApiDataResponse<List<ParticipatedAuction>> apiResponse = response.body();
                    if (apiResponse.getData() != null && !apiResponse.getData().isEmpty()) {
                        Log.d("AuctionData", "Data fetched: " + apiResponse.getData().size());
                        expiringAuctionAdapter = new ParticipateAuctionAdapter(apiResponse.getData(), getContext(), screenWidthDp);
                        recycler.setAdapter(expiringAuctionAdapter);
                    } else {
                        Log.d("AuctionData", "No auctions found for this date.");
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiDataResponse<List<ParticipatedAuction>>> call, Throwable t) {
//                 Log.e(LOG_TAG, "API call failed: " + t.getMessage());
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