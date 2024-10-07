package com.example.recoope_mobile.activity.fragments;

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
import com.example.recoope_mobile.dialog.DialogUtils;
import com.example.recoope_mobile.dialog.FilterDialogCallback;
import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.response.ApiDataResponseAuction;
import com.example.recoope_mobile.utils.ButtonToggleManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment {

    private final String LOG_TAG = "CardFeed";
    private RecyclerView recyclerView;
    private AuctionAdapter auctionAdapter;
    private List<Auction> auctionList;
    private ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
    private ButtonToggleManager buttonToggleManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView called");

        View view = inflater.inflate(R.layout.feed, container, false);
        Log.d(LOG_TAG, "View inflated");

        recyclerView = view.findViewById(R.id.recyclerViewFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d(LOG_TAG, "RecyclerView initialized");

        auctionList = new ArrayList<>();
        auctionAdapter = new AuctionAdapter(auctionList, getContext());
        recyclerView.setAdapter(auctionAdapter);
        Log.d(LOG_TAG, "Adapter set");

        buttonToggleManager = new ButtonToggleManager(getContext(), R.color.recoope_primary_color, R.color.background_color);

        Button btGlassFilter = view.findViewById(R.id.btGlassFilter);
        Button btMetalFilter = view.findViewById(R.id.btMetalFilter);
        Button btPlasticFilter = view.findViewById(R.id.btPlasticFilter);
        ImageButton btOtherFilter = view.findViewById(R.id.btOtherFilter);

        btGlassFilter.setOnClickListener(v -> {
            buttonToggleManager.toggleButton(btGlassFilter);
            applyFilterOrReset(btGlassFilter.isSelected(), "vidro");
        });

        btMetalFilter.setOnClickListener(v -> {
            buttonToggleManager.toggleButton(btMetalFilter);
            applyFilterOrReset(btMetalFilter.isSelected(), "metal");
        });

        btPlasticFilter.setOnClickListener(v -> {
            buttonToggleManager.toggleButton(btPlasticFilter);
            applyFilterOrReset(btPlasticFilter.isSelected(), "plastico");
        });


        btOtherFilter.setOnClickListener(v -> DialogUtils.showFilterDialog(FeedFragment.this, new FilterDialogCallback() {
            @Override
            public void onFilterSelected(String filter) {
                if (filter.isEmpty()) {
                    fetchAuctionData();
                } else {
                    fetchAuctionDataFilter(filter);
                }
            }
        }));

        fetchAuctionData();
        return view;
    }

    private void fetchAuctionData() {
        Call<ApiDataResponseAuction<List<Auction>>> call = apiService.getAllAuctions();

        call.enqueue(new Callback<ApiDataResponseAuction<List<Auction>>>() {
            @Override
            public void onResponse(Call<ApiDataResponseAuction<List<Auction>>> call, Response<ApiDataResponseAuction<List<Auction>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e(LOG_TAG, "Leilões carregados");
                    ApiDataResponseAuction<List<Auction>> apiResponse = response.body();
                    auctionList.clear();
                    if (apiResponse.getData() != null) {
                        auctionList.addAll(apiResponse.getData());
                    }
                    auctionAdapter.notifyDataSetChanged();
                } else {
                    Log.e(LOG_TAG, "Response failed: " + response.message());
                    Toast.makeText(getContext(), "Failed to load auctions.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponseAuction<List<Auction>>> call, Throwable t) {
                Log.e(LOG_TAG, "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyFilterOrReset(boolean isSelected, String filter) {
        if (isSelected) {
            fetchAuctionDataFilter(filter);
        } else {
            fetchAuctionData();
        }
    }

    private void fetchAuctionDataFilter(String filter) {
        Call<ApiDataResponseAuction<List<Auction>>> call = apiService.getFiltredAuction(filter);

        call.enqueue(new Callback<ApiDataResponseAuction<List<Auction>>>() {
            @Override
            public void onResponse(Call<ApiDataResponseAuction<List<Auction>>> call, Response<ApiDataResponseAuction<List<Auction>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiDataResponseAuction<List<Auction>> apiResponse = response.body();
                    auctionList.clear();
                    if (apiResponse.getData() != null) {
                        auctionList.addAll(apiResponse.getData());
                    }
                    auctionAdapter.notifyDataSetChanged();
                } else {
                    Log.e(LOG_TAG, "Response failed: " + response.message());
                    Toast.makeText(getContext(), "Failed to load auctions.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponseAuction<List<Auction>>> call, Throwable t) {
                Log.e(LOG_TAG, "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
