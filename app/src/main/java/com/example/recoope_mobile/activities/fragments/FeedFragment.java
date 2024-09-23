package com.example.recoope_mobile.activities.fragments;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

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
import com.example.recoope_mobile.activities.Login;
import com.example.recoope_mobile.adapter.AuctionAdapter;
import com.example.recoope_mobile.enums.InvalidFormatLogin;
import com.example.recoope_mobile.models.Address;
import com.example.recoope_mobile.models.Auction;
import com.example.recoope_mobile.models.Company;
import com.example.recoope_mobile.models.Product;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment {

    private final String LOG_TAG = "CardFeed";
    private RecyclerView recyclerView;
    private AuctionAdapter auctionAdapter;
    private List<Auction> auctionList;
    private ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize auction list
        auctionList = new ArrayList<>();
        fetchAuctionData(); // Call API to get auctions

        // Set up adapter with auction list
        auctionAdapter = new AuctionAdapter(auctionList, getContext());
        recyclerView.setAdapter(auctionAdapter);

        return view;
    }

    private void fetchAuctionData() {
        Call<ResponseBody> call = apiService.getAllAuction();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
                        JsonArray data = jsonResponse.getAsJsonArray("data");

                        // Loop through each auction in the array and map to Auction object
                        for (int i = 0; i < data.size(); i++) {
                            JsonObject auctionJson = data.get(i).getAsJsonObject();

                            // Create Product object from JSON
                            JsonObject productJson = auctionJson.getAsJsonObject("produto");
                            Product product = new Product(
                                    productJson.get("idProduto").getAsInt(),
                                    productJson.get("tipoProduto").getAsString(),
                                    productJson.get("valorInicialProduto").getAsDouble(),
                                    productJson.get("peso").getAsDouble(),
                                    productJson.has("fotoLeilao") ? productJson.get("fotoLeilao").getAsString() : null
                            );

                            // Create Address object from JSON
                            JsonObject addressJson = auctionJson.getAsJsonObject("endereco");
                            Address address = new Address(
                                    addressJson.get("idEndereco").getAsInt(),
                                    addressJson.get("cidade").getAsString(),
                                    addressJson.get("rua").getAsString(),
                                    addressJson.get("numero").getAsInt()
                            );

                            // Create Auction object
                            Auction auction = new Auction(
                                    auctionJson.get("idLeilao").getAsInt(),
                                    auctionJson.get("dataInicioLeilao").getAsString(),
                                    auctionJson.get("dataFimLeilao").getAsString(),
                                    auctionJson.get("detalhesLeilao").getAsString(),
                                    auctionJson.get("horaLeilao").getAsString(),
                                    product,
                                    address,
                                    null // You can add cooperativa when it's available
                            );

                            // Add auction to list
                            auctionList.add(auction);
                        }

                        // Notify adapter that data has changed
                        auctionAdapter.notifyDataSetChanged();

                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error processing response: " + e.getMessage());
                    }
                } else {
                    Log.e(LOG_TAG, "Response failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(LOG_TAG, "API call failed: " + t.getMessage());
            }
        });
    }
}