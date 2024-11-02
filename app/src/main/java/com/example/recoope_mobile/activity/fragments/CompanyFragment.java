package com.example.recoope_mobile.activity.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.LoggerClient;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.model.CompanyProfile;
import com.example.recoope_mobile.response.ApiDataResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyFragment extends Fragment {

    private final String LOG_TAG = "CardProfile";

    private String name;
    private String cnpj;
    private String email;
    private String phone;
    private String participatedAuctions;
    private Button exit;

    private TextView textViewName;
    private TextView textViewCnpj;
    private TextView textViewEmail;
    private TextView textViewPhone;

    private TextView textViewParticipatedAuctions;

    private ApiService apiService;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView called");
        LoggerClient.postLog(getContext(), "COMPANY");
        View view = inflater.inflate(R.layout.profile, container, false);

        textViewName = view.findViewById(R.id.cooperativeName);
        textViewCnpj = view.findViewById(R.id.cooperativeCNPJ);
        textViewEmail = view.findViewById(R.id.cooperativeEmail);
        textViewPhone = view.findViewById(R.id.cooperativePhone);
        textViewParticipatedAuctions = view.findViewById(R.id.cooperativeParticipatedAuctions);
        exit = view.findViewById(R.id.exitButton);

        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

        fetchCompany();

        return view;
    }

    private void fetchCompany() {
        String cnpj = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("cnpj", "");
        Call<ApiDataResponse<CompanyProfile>> call = apiService.getCompanyById(cnpj);

        call.enqueue(new Callback<ApiDataResponse<CompanyProfile>>() {
            @Override
            public void onResponse(Call<ApiDataResponse<CompanyProfile>> call, Response<ApiDataResponse<CompanyProfile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiDataResponse<CompanyProfile> apiResponse = response.body();
                    // Pegar informações e colocar no perfil da empresa
                    name = apiResponse.getData().getName();
                    email = apiResponse.getData().getEmail();
                    phone = apiResponse.getData().getPhone();
                    participatedAuctions = apiResponse.getData().getParticipatedAuctions();

                    textViewCnpj.setText(String.format("%s.%s.%s/%s-%s", cnpj.substring(0, 2), cnpj.substring(2, 5), cnpj.substring(5, 8), cnpj.substring(8, 12), cnpj.substring(11, 14)));
                    textViewName.setText(name);
                    textViewEmail.setText(email);
                    textViewPhone.setText(phone);
                    textViewParticipatedAuctions.setText(String.valueOf(participatedAuctions));

                    //Botao de sair
                    exit.setOnClickListener(v -> {
                        getActivity().finish();
                    });

                    Log.d(LOG_TAG, "Company fetched successfully");
                } else {
                    Log.e(LOG_TAG, "Response failed: " + response.message());
                    Toast.makeText(getContext(), "Failed to load auctions.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponse<CompanyProfile>> call, Throwable t) {
                Log.e(LOG_TAG, "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}