package com.example.recoope_mobile.activity.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.model.AuctionDetails;
import com.example.recoope_mobile.response.ApiDataResponseAuction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidFragment extends Fragment {

    private int auctionId;

    private ImageView backButton;
    private TextView cooperativeName;
    private ImageView auctionImage;
    private TextView auctionEndMsg;
    private TextView auctionMaterial;
    private TextView auctionWeight;
    private TextView auctionPrice;

    private ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bid, container, false);

        auctionId = getArguments().getInt("AUCTION_ID");

        backButton = view.findViewById(R.id.backButton);
        cooperativeName = view.findViewById(R.id.cooperativeName);
        auctionImage = view.findViewById(R.id.auctionImage);
        auctionEndMsg = view.findViewById(R.id.auctionEndMsg);
        auctionMaterial = view.findViewById(R.id.auctionMaterial);
        auctionWeight = view.findViewById(R.id.auctionWeight);
        auctionPrice = view.findViewById(R.id.auctionPrice);

        backButton.setOnClickListener((v) -> getParentFragmentManager().popBackStack());

        Call<ApiDataResponseAuction<AuctionDetails>> call = apiService.getAuctionDetails(auctionId);
        call.enqueue(new Callback<ApiDataResponseAuction<AuctionDetails>>() {
            @Override
            public void onResponse(Call<ApiDataResponseAuction<AuctionDetails>> call, Response<ApiDataResponseAuction<AuctionDetails>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuctionDetails details = response.body().getData();
                    cooperativeName.setText(details.getCooperative().getName());
                    Glide.with(getContext())
                            .load(details.getProduct().getPhoto())
                            .into(auctionImage);
                    auctionEndMsg.setText("Restam " + details.getRemainingTime());
                    auctionMaterial.setText(details.getProduct().getProductType());
                    auctionWeight.setText(String.valueOf(details.getProduct().getWeight()));
                    auctionPrice.setText(String.valueOf(details.getProduct().getInitialValue()));
                } else {
                    Log.e("BID", "Response failed: " + response.message());
                    Toast.makeText(getContext(), "Failed to load auctions.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponseAuction<AuctionDetails>> call, Throwable t) {

            }
        });

        return view;
    }
}