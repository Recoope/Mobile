package com.example.recoope_mobile.activity.fragments;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import static com.example.recoope_mobile.utils.ValidationUtils.calculateCardWidthDp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.LoggerClient;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.activity.MainActivity;
import com.example.recoope_mobile.activity.SuccessBid;
import com.example.recoope_mobile.model.AuctionDetails;
import com.example.recoope_mobile.model.Bid;
import com.example.recoope_mobile.model.BidInfo;
import com.example.recoope_mobile.model.LoginResponse;
import com.example.recoope_mobile.response.ApiDataResponse;
import com.example.recoope_mobile.utils.DialogUtils;
import com.example.recoope_mobile.utils.NotificationHelper;
import com.example.recoope_mobile.utils.PtBrUtils;
import com.example.recoope_mobile.utils.ValidationUtils;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
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
    private EditText bidPriceInput;
    private Button bidButton;
    private Handler handler = new Handler();
    private ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LoggerClient.postLog(getContext(), "BID");
        View view = inflater.inflate(R.layout.bid, container, false);

        String cnpj = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("cnpj", null);

        // Obtém uma referência à MainActivity
        MainActivity activity = (MainActivity) getActivity();

        // Inicia o ProgressBar ao começar a carregar os dados
        if (activity != null) {
            activity.showLoading();
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidthDp = Math.round(displayMetrics.widthPixels / displayMetrics.density);
        auctionId = getArguments().getInt("AUCTION_ID");

        auctionIdView = view.findViewById(R.id.topBarText);
        backButton = view.findViewById(R.id.backButton);
        cooperativeName = view.findViewById(R.id.txtCooperativeName);
        auctionImage = view.findViewById(R.id.auctionImage);
        auctionEndMsg = view.findViewById(R.id.auctionEndMsg);
        auctionMaterial = view.findViewById(R.id.auctionMaterial);
        auctionWeight = view.findViewById(R.id.auctionWeight);
        auctionPrice = view.findViewById(R.id.auctionPrice);
        bidPriceInput = view.findViewById(R.id.bidPrice);
        bidButton = view.findViewById(R.id.bidButton);
        auctionIdView.setText("Leilão " + ValidationUtils.truncateString(requireContext(), PtBrUtils.formatId(auctionId), calculateCardWidthDp(getContext(), 0.53)));
        backButton.setOnClickListener((v) -> getParentFragmentManager().popBackStack());

        Call<ApiDataResponse<AuctionDetails>> call = apiService.getAuctionDetails(auctionId);
        call.enqueue(new Callback<ApiDataResponse<AuctionDetails>>() {
            @Override
            public void onResponse(Call<ApiDataResponse<AuctionDetails>> call, Response<ApiDataResponse<AuctionDetails>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    activity.hideLoading();
                    AuctionDetails details = response.body().getData();
                    cooperativeName.setText(ValidationUtils.truncateString(requireContext(), details.getCooperative().getName(), calculateCardWidthDp(getContext(), 0.53)));
                    Glide.with(getContext())
                            .load(details.getProduct().getPhoto())
                            .into(auctionImage);
                    startEndCounter(auctionEndMsg, details.getEndDate(), Time.valueOf(details.getTime()));
                    auctionMaterial.setText(ValidationUtils.truncateString(requireContext(), details.getProduct().getProductType(), calculateCardWidthDp(getContext(), 0.53)));
                    auctionWeight.setText(ValidationUtils.truncateString(requireContext(), PtBrUtils.formatWeight(details.getProduct().getWeight()), calculateCardWidthDp(getContext(), 0.53)));
                    if(details.getBestBid() == null){
                        auctionPrice.setText(PtBrUtils.formatReal(0.0));
                    }else{
                        auctionPrice.setText(ValidationUtils.truncateString(
                                requireContext(),
                                PtBrUtils.formatReal(details.getBestBid().getValue()),
                                calculateCardWidthDp(getContext(), 0.50)
                        ));                    }
                } else {
                    Toast.makeText(requireContext(), "Algo deu errado, volte mais tarde!", Toast.LENGTH_LONG).show();
                    Log.e("BID", "Response failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponse<AuctionDetails>> call, Throwable t) {
                Log.e("BID", "Request Failed: " + t.getMessage());
            }
        });

        bidButton.setOnClickListener((v) -> {
            double bidPriceInputValue = 0;

            String bidPriceInputText = bidPriceInput.getText().toString().trim();
            if (!bidPriceInputText.isEmpty()) {
                bidPriceInputValue = Double.parseDouble(bidPriceInputText);
            }

            BidInfo bidInfo = new BidInfo(cnpj, bidPriceInputValue);
            Call bidCall = apiService.bid(auctionId, bidInfo);
            bidCall.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful() && response.code() == 201) {
                        activity.hideLoading();
                        Intent intent = new Intent(getActivity(), SuccessBid.class);
                        intent.putExtra("AUCTION_ID", auctionId);

                        NotificationHelper.sendNotification(requireContext(), "Lance realizado!", "Que ganhe o mais ousado!");
                        Toast.makeText(requireContext(), "Lance realizado!", Toast.LENGTH_LONG).show();
                        startActivity(intent);

                        new Handler().postDelayed(() -> {
                            FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.mainContent, new CalendarFragment());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            ((NavigationBarView) getActivity().findViewById(R.id.navbar)).setSelectedItemId(R.id.calendar_button);
                        }, 50);
                    } else {
                        ResponseBody responseBody = response.errorBody();
                        if (responseBody != null) {
                            try {
                                String responseString = responseBody.string();
                                JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
                                String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown message";

                                if (response.code() == 400) {
                                    DialogUtils.showCustomBidDialog(BidFragment.this, message);
                                }

                            } catch (Exception e) {
                                Log.e("Bid", "Error processing response: " + e.getMessage());
                                Toast.makeText(requireContext(), "Algo deu errado, volte mais tarde!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("Bid", "API call failed: " + t.getMessage());
                }
            });
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
                endCounterView.setText("Encerra em " + PtBrUtils.getRemaingTimeMsgPTBR(endDate, endHour));

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