package com.example.recoope_mobile.activity.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.adapter.PaymentAdapter;
import com.example.recoope_mobile.model.Payment;
import com.example.recoope_mobile.model.Payment;
import com.example.recoope_mobile.response.ApiDataResponse;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payments, container, false);

        recyclerView = view.findViewById(R.id.paymentRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        paymentList = new ArrayList<>();

        paymentAdapter = new PaymentAdapter(paymentList, getContext());
        recyclerView.setAdapter(paymentAdapter);

        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

        getPayments();

        return view;
    }
    
    public void getPayments(){
        String cnpj = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("cnpj", "");
        Call<ApiDataResponse<List<Payment>>> call = apiService.getPayment(cnpj);
        call.enqueue(new Callback<ApiDataResponse<List<Payment>>>() {
            @Override
            public void onResponse(Call<ApiDataResponse<List<Payment>>> call, Response<ApiDataResponse<List<Payment>>> response) {
                handlePaymentResponse(response);
            }

            @Override
            public void onFailure(Call<ApiDataResponse<List<Payment>>> call, Throwable t) {
                handlePaymentFailure(t);
            }
        });
    }

    private void handlePaymentResponse(Response<ApiDataResponse<List<Payment>>> response) {
        if (response.isSuccessful() && response.body() != null) {
            paymentList.clear();
            paymentList.addAll(response.body().getData());
            paymentAdapter.notifyDataSetChanged();
        } else {
            paymentList.clear();
            paymentAdapter.notifyDataSetChanged();
        }
    }

    private void handlePaymentFailure(Throwable t) {
        paymentList.clear();
        paymentAdapter.notifyDataSetChanged();
        Log.e(LOG_TAG, t.getMessage());
    }
    
    
}