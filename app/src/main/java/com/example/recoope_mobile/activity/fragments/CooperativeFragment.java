package com.example.recoope_mobile.activity.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.model.Cooperative;
import com.example.recoope_mobile.response.ApiDataResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CooperativeFragment extends Fragment {

    private final String LOG_TAG = "Cooperative Profile";

    private String name;
    private String email;
    private String phone;

    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewPhone;
    private ApiService apiService;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.cooperative_profile, container, false);

        textViewName = view.findViewById(R.id.txtCooperativeName);
        textViewEmail = view.findViewById(R.id.cooperativeEmail);
        textViewPhone = view.findViewById(R.id.cooperativePhone);

        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

        fetchCooperative();

        return view;
    }

    private void fetchCooperative() {
        Bundle bundle = getArguments();

        String cnpj = bundle.getString("cnpjCooperative");

        Call<ApiDataResponse<Cooperative>> call = apiService.getIdCooperative(cnpj);

        call.enqueue(new Callback<ApiDataResponse<Cooperative>>() {
            @Override
            public void onResponse(Call<ApiDataResponse<Cooperative>> call, Response<ApiDataResponse<Cooperative>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiDataResponse<Cooperative> apiResponse = response.body();
                    // Pegar informações e colocar no perfil da empresa
                    name = apiResponse.getData().getName();
                    email = apiResponse.getData().getEmail();

                    textViewName.setText(name);
                    textViewEmail.setText(email);

                    Log.d(LOG_TAG, "Company fetched successfully");
                } else {
                    Log.e(LOG_TAG, "Response failed: " + response.message());
                    Toast.makeText(getContext(), "Failed to load auctions.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponse<Cooperative>> call, Throwable t) {
                Log.e(LOG_TAG, "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}