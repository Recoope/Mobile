package com.example.recoope_mobile.activity.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.LoggerClient;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.activity.MainActivity;
import com.example.recoope_mobile.adapter.PaymentAdapter;
import com.example.recoope_mobile.model.Payment;
import com.example.recoope_mobile.response.ApiDataResponse;
import com.example.recoope_mobile.utils.StatusUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentsFragment extends Fragment {
    private static final String LOG_TAG = "Search Payment";
    private RecyclerView recyclerView;
    private PaymentAdapter paymentAdapter;
    private List<Payment> paymentList;
    private ApiService apiService;
    private ImageView messageStatus;
    private MainActivity activity;
    private Button btnOrderByDate;
    private boolean dateDesc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LoggerClient.postLog(getContext(), "PAYMENTS");
        View view = inflater.inflate(R.layout.payments, container, false);

        recyclerView = view.findViewById(R.id.paymentRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        paymentList = new ArrayList<>();

        btnOrderByDate = view.findViewById(R.id.orderByDate);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidthDp = Math.round(displayMetrics.widthPixels / displayMetrics.density);

        paymentAdapter = new PaymentAdapter(paymentList, getContext(), screenWidthDp);
        recyclerView.setAdapter(paymentAdapter);

        activity = (MainActivity) requireActivity();

        messageStatus = view.findViewById(R.id.messageStatusPayments);

        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

        dateDesc = false;

        btnOrderByDate.setOnClickListener(r -> {
            if(dateDesc){
                btnOrderByDate.setText("Mais recentes");
                dateDesc = false;

            }else{
                btnOrderByDate.setText("Mais antigos");
                dateDesc = true;
            }
            getPayments(dateDesc);
        });

        getPayments(dateDesc);

        return view;
    }
    
    public void getPayments(boolean dateDesc){
        String cnpj = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("cnpj", "");
        Call<ApiDataResponse<List<Payment>>> call = apiService.getPayment(cnpj, dateDesc);
        call.enqueue(new Callback<ApiDataResponse<List<Payment>>>() {
            @Override
            public void onResponse(Call<ApiDataResponse<List<Payment>>> call, Response<ApiDataResponse<List<Payment>>> response) {
                if (isAdded()) {
                    handlePaymentResponse(response);
                    activity.hideLoading();
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponse<List<Payment>>> call, Throwable t) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Algo deu errado, volte mais tarde!", Toast.LENGTH_LONG).show();
                    handlePaymentFailure(t);
                }
            }
        });
    }


    private void handlePaymentResponse(Response<ApiDataResponse<List<Payment>>> response) {
        if (response.isSuccessful() && response.body() != null) {
            StatusUtils.hideStatusImage(messageStatus);
            paymentList.clear();
            paymentList.addAll(response.body().getData());
            paymentAdapter.notifyDataSetChanged();

        } else {
            if(response.code() == 500){
                Toast.makeText(requireContext(), "Algo deu errado, volte mais tarde!", Toast.LENGTH_LONG).show();
                activity.hideLoading();
                StatusUtils.showStatusImage(messageStatus, StatusUtils.STATUS_SERVER_ERROR);
            }else {
                StatusUtils.hideStatusImage(messageStatus);
                paymentList.clear();
                StatusUtils.showStatusImage(messageStatus, StatusUtils.STATUS_NO_DATA);
                paymentAdapter.notifyDataSetChanged();
            }
        }
    }

    private void handlePaymentFailure(Throwable t) {
        StatusUtils.hideStatusImage(messageStatus);
        Log.e("API_ERROR", "Failed to load data: " + t.getMessage(), t);
        paymentList.clear();
        activity.hideLoading();
        StatusUtils.showStatusImage(messageStatus, StatusUtils.STATUS_SERVER_ERROR);
        paymentAdapter.notifyDataSetChanged();
    }
    
    
}