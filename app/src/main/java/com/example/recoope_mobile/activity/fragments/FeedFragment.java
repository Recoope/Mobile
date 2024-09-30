package com.example.recoope_mobile.activity.fragments;

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
import com.example.recoope_mobile.adapter.AuctionAdapter;
import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.response.ApiDataResponseAuction;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView called");

        View view = null;
        try {
            view = inflater.inflate(R.layout.feed, container, false);
            Log.d(LOG_TAG, "View inflated");

            recyclerView = view.findViewById(R.id.recyclerViewFeed);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            Log.d(LOG_TAG, "RecyclerView initialized");

            auctionList = new ArrayList<>();
            auctionAdapter = new AuctionAdapter(auctionList, getContext());
            recyclerView.setAdapter(auctionAdapter);
            Log.d(LOG_TAG, "Adapter set");

            fetchAuctionData();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception in onCreateView: " + e.getMessage());
            e.printStackTrace();
        }

        return view;
    }


    private void fetchAuctionData() {
        Call<ApiDataResponseAuction<List<Auction>>> call = apiService.getAllAuctions();

        call.enqueue(new Callback<ApiDataResponseAuction<List<Auction>>>() {
            @Override
            public void onResponse(Call<ApiDataResponseAuction<List<Auction>>> call, Response<ApiDataResponseAuction<List<Auction>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiDataResponseAuction<List<Auction>> apiResponse = response.body();
                    // Limpar a lista existente e adicionar os novos leilões da resposta
                    auctionList.clear();
                    if (apiResponse.getData() != null) {
                        auctionList.addAll(apiResponse.getData());
                    }
                    // Notificar o adaptador que os dados mudaram
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
