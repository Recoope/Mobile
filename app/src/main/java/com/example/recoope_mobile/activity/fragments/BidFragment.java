package com.example.recoope_mobile.activity.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
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
import com.example.recoope_mobile.model.AuctionDetails;
import com.example.recoope_mobile.response.ApiDataResponseAuction;
import com.example.recoope_mobile.utils.PtBrUtils;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidFragment extends Fragment {

    private int auctionId;

    private ImageView backButton;
    private TextView auctionIdView;
    private TextView cooperativeName;
    private ImageView auctionImage;
    private TextView auctionEndMsg;
    private TextView auctionMaterial;
    private TextView auctionWeight;
    private TextView auctionPrice;

    private Handler handler = new Handler();
    private ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bid, container, false);

        auctionId = getArguments().getInt("AUCTION_ID");

        auctionIdView = view.findViewById(R.id.topBarText);
        backButton = view.findViewById(R.id.backButton);
        cooperativeName = view.findViewById(R.id.cooperativeName);
        auctionImage = view.findViewById(R.id.auctionImage);
        auctionEndMsg = view.findViewById(R.id.auctionEndMsg);
        auctionMaterial = view.findViewById(R.id.auctionMaterial);
        auctionWeight = view.findViewById(R.id.auctionWeight);
        auctionPrice = view.findViewById(R.id.auctionPrice);

        auctionIdView.setText("Leilão " + PtBrUtils.formatId(auctionId));
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
                    startEndCounter(auctionEndMsg, details.getEndDate(), Time.valueOf(details.getTime()));
                    auctionMaterial.setText(details.getProduct().getProductType());
                    auctionWeight.setText(PtBrUtils.formatWeight(details.getProduct().getWeight()));
                    auctionPrice.setText(PtBrUtils.formatReal(details.getProduct().getInitialValue()));
                } else {
                    Log.e("BID", "Response failed: " + response.message());
                    Toast.makeText(getContext(), "Failed to load auctions.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponseAuction<AuctionDetails>> call, Throwable t) {
                Log.e("BID", "Request Failed: " + t.getMessage());
            }
        });

        return view;
    }

    private void startEndCounter(TextView endCounterView, Date endDate, Time endHour) {

        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(endDate);
        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(endHour);

        dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));

        Date remainingTime = dateCal.getTime();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                endCounterView.setText("Encerra em " + PtBrUtils.getRemaingTimeMsgPTBR(remainingTime));

                if (remainingTime.getTime() >= 0) {
                    handler.postDelayed(this, 0);
                }
            }
        }, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}