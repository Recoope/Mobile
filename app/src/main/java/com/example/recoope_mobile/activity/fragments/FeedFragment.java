package com.example.recoope_mobile.activity.fragments;

import static com.example.recoope_mobile.utils.ValidationUtils.isValidDate;
import static com.example.recoope_mobile.utils.ValidationUtils.isValidWeight;

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
import android.widget.Toast;
import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.adapter.AuctionAdapter;
import com.example.recoope_mobile.utils.DialogUtils;
import com.example.recoope_mobile.utils.FilterDialogCallback;
import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.response.ApiDataResponseAuction;
import com.example.recoope_mobile.utils.ButtonToggleManager;
import com.example.recoope_mobile.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Calendar;

public class FeedFragment extends Fragment {

    private static final String LOG_TAG = "CardFeed";
    private RecyclerView recyclerView;
    private AuctionAdapter auctionAdapter;
    private List<Auction> auctionList;
    private ApiService apiService;
    private ButtonToggleManager buttonToggleManager;
    private ArrayList<String> activeFilters = new ArrayList<>();
    private String closeAt = null;
    private String minWeight = null;
    private String maxWeight = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feed, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        auctionList = new ArrayList<>();
        auctionAdapter = new AuctionAdapter(auctionList, getContext());
        recyclerView.setAdapter(auctionAdapter);

        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        buttonToggleManager = new ButtonToggleManager(getActivity(), R.color.recoope_primary_color, R.color.background_color);

        setupFilterButtons(view);

        fetchAuctionData();
        return view;
    }

    private void setupFilterButtons(View view) {
        Button btGlassFilter = view.findViewById(R.id.btGlassFilter);
        Button btMetalFilter = view.findViewById(R.id.btMetalFilter);
        Button btPlasticFilter = view.findViewById(R.id.btPlasticFilter);
        ImageButton btOtherFilter = view.findViewById(R.id.btOtherFilter);
        Button btClearFilters = view.findViewById(R.id.btClearFilters);

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

        btOtherFilter.setOnClickListener(v -> DialogUtils.showFilterDialog(FeedFragment.this, new FilterDialogCallback() {
            @Override
            public void onFilterSelected(List<String> filters, String closeAt, String minWeight, String maxWeight) {
                activeFilters.clear();
                activeFilters.addAll(filters);
                syncFiltersBetweenFeedAndDialog(btGlassFilter, btMetalFilter, btPlasticFilter);

                if (validateFilters(closeAt, minWeight, maxWeight)) {
                    applyAdditionalFilters(closeAt, minWeight, maxWeight);
                    btClearFilters.setVisibility(View.VISIBLE);
                } else {
                    DialogUtils.showCustomFeedDialog(FeedFragment.this);
                }
            }
        }, activeFilters));

        btClearFilters.setOnClickListener(v -> clearFilters(btClearFilters));
    }

    public void syncFiltersBetweenFeedAndDialog(Button btGlassFilter, Button btMetalFilter, Button btPlasticFilter) {
        btGlassFilter.setSelected(activeFilters.contains("VIDRO"));
        btMetalFilter.setSelected(activeFilters.contains("METAL"));
        btPlasticFilter.setSelected(activeFilters.contains("PLASTICO"));

        buttonToggleManager.setButtonState(btGlassFilter, btGlassFilter.isSelected());
        buttonToggleManager.setButtonState(btMetalFilter, btMetalFilter.isSelected());
        buttonToggleManager.setButtonState(btPlasticFilter, btPlasticFilter.isSelected());
    }

    private void updateFilterList(boolean isSelected, String filter) {
        if (isSelected) {
            if (!activeFilters.contains(filter)) {
                activeFilters.add(filter);
            }
        } else {
            activeFilters.remove(filter);
        }
        syncFiltersBetweenFeedAndDialog(
                getView().findViewById(R.id.btGlassFilter),
                getView().findViewById(R.id.btMetalFilter),
                getView().findViewById(R.id.btPlasticFilter)
        );
        applyFilterOrReset();
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

    public void clearFilters(Button btClearFilters) {
        activeFilters.clear();
        closeAt = null;
        minWeight = null;
        maxWeight = null;
        btClearFilters.setVisibility(View.INVISIBLE);
        syncFiltersBetweenFeedAndDialog(
                getView().findViewById(R.id.btGlassFilter),
                getView().findViewById(R.id.btMetalFilter),
                getView().findViewById(R.id.btPlasticFilter)
        );
        fetchAuctionData();
    }

    public boolean validateFilters(String closeAt, String minWeight, String maxWeight) {
        return isValidDate(closeAt) && isValidWeight(minWeight) && isValidWeight(maxWeight);
    }


    private void fetchAuctionData() {
        Call<ApiDataResponseAuction<List<Auction>>> call = apiService.getAllAuctions();
        call.enqueue(new Callback<ApiDataResponseAuction<List<Auction>>>() {
            @Override
            public void onResponse(Call<ApiDataResponseAuction<List<Auction>>> call, Response<ApiDataResponseAuction<List<Auction>>> response) {
                handleAuctionResponse(response);
            }

            @Override
            public void onFailure(Call<ApiDataResponseAuction<List<Auction>>> call, Throwable t) {
                handleAuctionFailure(t);
            }
        });
    }

    private void fetchAuctionDataWithFilter(List<String> filters, String closeAt, String weightMin, String weightMax) {
        Call<ApiDataResponseAuction<List<Auction>>> call = apiService.getFilteredAuctions(filters, closeAt, weightMin, weightMax);
        call.enqueue(new Callback<ApiDataResponseAuction<List<Auction>>>() {
            @Override
            public void onResponse(Call<ApiDataResponseAuction<List<Auction>>> call, Response<ApiDataResponseAuction<List<Auction>>> response) {
                handleAuctionResponse(response);
            }

            @Override
            public void onFailure(Call<ApiDataResponseAuction<List<Auction>>> call, Throwable t) {
                handleAuctionFailure(t);
            }
        });
    }

    private void handleAuctionResponse(Response<ApiDataResponseAuction<List<Auction>>> response) {
        if (response.isSuccessful() && response.body() != null) {
            auctionList.clear();
            auctionList.addAll(response.body().getData());
            auctionAdapter.notifyDataSetChanged();
        } else {
            auctionList.clear();
            auctionAdapter.notifyDataSetChanged();
        }
    }

    private void handleAuctionFailure(Throwable t) {
        auctionList.clear();
        auctionAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
    }
}
