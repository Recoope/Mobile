package com.example.recoope_mobile.activity.fragments;

import static com.example.recoope_mobile.utils.ValidationUtils.isValidDate;
import static com.example.recoope_mobile.utils.ValidationUtils.isValidWeight;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.activity.MainActivity;
import com.example.recoope_mobile.adapter.AuctionAdapter;
import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.model.Cooperative;
import com.example.recoope_mobile.response.ApiDataResponse;
import com.example.recoope_mobile.utils.ButtonToggleManager;
import com.example.recoope_mobile.utils.DialogUtils;
import com.example.recoope_mobile.utils.FilterDialogCallback;
import com.example.recoope_mobile.utils.StatusUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
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
    private MainActivity activity;
    private RecyclerView auctionRecyclerCoop;
    private AuctionAdapter auctionAdapter;
    private List<Auction> auctionList;
    private ButtonToggleManager buttonToggleManager;
    private ArrayList<String> activeFilters = new ArrayList<>();
    private String closeAt = null;
    private String minWeight = null;
    private String maxWeight = null;
    private Button btGlassFilter;
    private Button btMetalFilter;
    private Button btPlasticFilter;
    private ImageView messageStatus;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.cooperative_profile, container, false);

        textViewName = view.findViewById(R.id.txtCooperativeName);
        textViewEmail = view.findViewById(R.id.cooperativeEmail);
        textViewPhone = view.findViewById(R.id.cooperativePhone);

        btGlassFilter = view.findViewById(R.id.btGlassFilterCoop);
        btMetalFilter = view.findViewById(R.id.btMetalFilterCoop);
        btPlasticFilter = view.findViewById(R.id.btPlasticFilterCoop);

        activity = (MainActivity) getActivity();

        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

        fetchCooperative();

        messageStatus = view.findViewById(R.id.messageStatusCoopProfile);

        auctionRecyclerCoop = view.findViewById(R.id.auctionRecyclerCoop);
        auctionRecyclerCoop.setLayoutManager(new LinearLayoutManager(getContext()));

        auctionList = new ArrayList<>();
        auctionAdapter = new AuctionAdapter(auctionList, getContext());
        auctionRecyclerCoop.setAdapter(auctionAdapter);

        buttonToggleManager = new ButtonToggleManager(getActivity(), R.color.recoope_primary_color, R.color.white);

        setupFilterButtons(view);
        fetchCooperative();
        fetchAuctionData();

        return view;
    }

    private void fetchCooperative() {
        activity.showLoading();
        Bundle bundle = getArguments();

        String cnpj = bundle.getString("cnpjCooperative");

        activity = (MainActivity) getActivity();

        Call<ApiDataResponse<Cooperative>> call = apiService.getIdCooperative(cnpj);

        call.enqueue(new Callback<ApiDataResponse<Cooperative>>() {
            @Override
            public void onResponse(Call<ApiDataResponse<Cooperative>> call, Response<ApiDataResponse<Cooperative>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    activity.hideLoading();
                    StatusUtils.hideStatusImage(messageStatus);
                    ApiDataResponse<Cooperative> apiResponse = response.body();
                    // Pegar informações e colocar no perfil da empresa
                    name = apiResponse.getData().getName();
                    email = apiResponse.getData().getEmail();

                    textViewName.setText(name);
                    textViewEmail.setText(email);

                    Log.d(LOG_TAG, "Company fetched successfully");
                } else {
                    if(response.code() == 500){
                        StatusUtils.showStatusImage(messageStatus, StatusUtils.STATUS_SERVER_ERROR);
                    }else {
                        StatusUtils.hideStatusImage(messageStatus);
                        StatusUtils.showStatusImage(messageStatus, StatusUtils.STATUS_NO_DATA);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponse<Cooperative>> call, Throwable t) {
                Log.e(LOG_TAG, "API call failed: " + t.getMessage());
            }
        });
    }


    private void setupFilterButtons(View view) {
        btGlassFilter = view.findViewById(R.id.btGlassFilterCoop);
        btMetalFilter = view.findViewById(R.id.btMetalFilterCoop);
        btPlasticFilter = view.findViewById(R.id.btPlasticFilterCoop);
        ImageButton btOtherFilter = view.findViewById(R.id.btOtherFilterCoop);
        Button btClearFilters = view.findViewById(R.id.btClearFiltersCoop);

        btGlassFilter.setOnClickListener(v -> {
            buttonToggleManager.toggleButton(btGlassFilter);
            updateFilterList(btGlassFilter.isSelected(), "VIDRO");
        });

        btMetalFilter.setOnClickListener(v -> {
            buttonToggleManager.toggleButton(btMetalFilter);
            updateFilterList(btMetalFilter.isSelected(), "METAL");
        });

        btPlasticFilter.setOnClickListener(v -> {
            buttonToggleManager.toggleButton(btPlasticFilter);
            updateFilterList(btPlasticFilter.isSelected(), "PLASTICO");
        });

        btOtherFilter.setOnClickListener(v -> DialogUtils.showFilterDialog(this, new FilterDialogCallback() {
            @Override
            public void onFilterSelected(List<String> filters, String closeAt, String minWeight, String maxWeight) {
                activeFilters.clear();
                activeFilters.addAll(filters);
                syncFiltersBetweenFeedAndDialog(btGlassFilter, btMetalFilter, btPlasticFilter);
                if (validateFilters(closeAt, minWeight, maxWeight)) {
                    applyAdditionalFilters(closeAt, minWeight, maxWeight);
                    btClearFilters.setVisibility(View.VISIBLE);
                } else {
                    DialogUtils.showCustomFeedDialog(CooperativeFragment.this);
                }
            }
        }, activeFilters));

        btClearFilters.setOnClickListener(v -> clearFilters(btClearFilters));
    }

    private void updateFilterList(boolean isSelected, String filter) {
        if (isSelected) {
            if (!activeFilters.contains(filter)) {
                activeFilters.add(filter);
            }
        } else {
            activeFilters.remove(filter);
        }
        applyFilterOrReset();
    }

    public boolean validateFilters(String closeAt, String minWeight, String maxWeight) {
        return isValidDate(closeAt) && isValidWeight(minWeight) && isValidWeight(maxWeight);
    }

    public void applyAdditionalFilters(String closeAt, String minWeight, String maxWeight) {
        this.closeAt = closeAt;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        Log.d(LOG_TAG, "CloseAt: " + closeAt + ", MinWeight: " + minWeight + ", MaxWeight: " + maxWeight);
        applyFilterOrReset();
    }

    private void applyFilterOrReset() {
        if (activeFilters.isEmpty() && closeAt == null && minWeight == null && maxWeight == null) {
            fetchAuctionData();
        } else {
            fetchAuctionDataWithFilter(activeFilters, closeAt, minWeight, maxWeight);
        }
    }

    private void fetchAuctionData() {
        Call<ApiDataResponse<List<Auction>>> call = apiService.getAllAuctions();
        call.enqueue(new Callback<ApiDataResponse<List<Auction>>>() {
            @Override
            public void onResponse(Call<ApiDataResponse<List<Auction>>> call, Response<ApiDataResponse<List<Auction>>> response) {
                activity.hideLoading();
                handleAuctionResponse(response);
            }

            @Override
            public void onFailure(Call<ApiDataResponse<List<Auction>>> call, Throwable t) {
                handleAuctionFailure(t);
            }
        });
    }

    private void fetchAuctionDataWithFilter(List<String> filters, String closeAt, String weightMin, String weightMax) {
        Call<ApiDataResponse<List<Auction>>> call = apiService.getFilteredAuctions(filters, closeAt, weightMin, weightMax);
        call.enqueue(new Callback<ApiDataResponse<List<Auction>>>() {
            @Override
            public void onResponse(Call<ApiDataResponse<List<Auction>>> call, Response<ApiDataResponse<List<Auction>>> response) {
                activity.hideLoading();
                handleAuctionResponse(response);
            }

            @Override
            public void onFailure(Call<ApiDataResponse<List<Auction>>> call, Throwable t) {
                handleAuctionFailure(t);
            }
        });
    }

    private void handleAuctionResponse(Response<ApiDataResponse<List<Auction>>> response) {
        activity.hideLoading();
        if (response.isSuccessful() && response.body() != null) {
            StatusUtils.hideStatusImage(messageStatus);
            auctionList.clear();
            auctionList.addAll(response.body().getData());
            auctionAdapter.notifyDataSetChanged();
        } else {
            if(response.code() == 500){
                StatusUtils.showStatusImage(messageStatus, StatusUtils.STATUS_SERVER_ERROR);
            }else {
                StatusUtils.hideStatusImage(messageStatus);
                auctionList.clear();
                StatusUtils.showStatusImage(messageStatus, StatusUtils.STATUS_NO_DATA);
                auctionAdapter.notifyDataSetChanged();
            }
        }
    }

    private void handleAuctionFailure(Throwable t) {
        StatusUtils.hideStatusImage(messageStatus);
        Log.e("API_ERROR", "Failed to load data: " + t.getMessage(), t);
        auctionList.clear();
        StatusUtils.showStatusImage(messageStatus, StatusUtils.STATUS_SERVER_ERROR);
        auctionAdapter.notifyDataSetChanged();
    }

    public void clearFilters(Button btClearFilters) {
        activeFilters.clear();
        closeAt = null;
        minWeight = null;
        maxWeight = null;
        btClearFilters.setVisibility(View.INVISIBLE);
        syncFiltersBetweenFeedAndDialog(btGlassFilter, btMetalFilter, btPlasticFilter);
        fetchAuctionData();
    }

    public void syncFiltersBetweenFeedAndDialog(Button btGlassFilter, Button btMetalFilter, Button btPlasticFilter) {
        btGlassFilter.setSelected(activeFilters.contains("VIDRO"));
        btMetalFilter.setSelected(activeFilters.contains("METAL"));
        btPlasticFilter.setSelected(activeFilters.contains("PLASTICO"));

        buttonToggleManager.setButtonState(btGlassFilter, btGlassFilter.isSelected());
        buttonToggleManager.setButtonState(btMetalFilter, btMetalFilter.isSelected());
        buttonToggleManager.setButtonState(btPlasticFilter, btPlasticFilter.isSelected());
    }

}