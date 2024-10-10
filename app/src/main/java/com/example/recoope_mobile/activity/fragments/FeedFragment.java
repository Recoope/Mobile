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
import com.example.recoope_mobile.utils.DialogUtils;
import com.example.recoope_mobile.utils.FilterDialogCallback;
import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.response.ApiDataResponseAuction;
import com.example.recoope_mobile.utils.ButtonToggleManager;
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
    private String closeAt = null;  // Data de fechamento
    private String minWeight = null;  // Peso mínimo
    private String maxWeight = null;  // Peso máximo

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

        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        buttonToggleManager = new ButtonToggleManager(getContext(), R.color.recoope_primary_color, R.color.background_color);

        Button btGlassFilter = view.findViewById(R.id.btGlassFilter);
        Button btMetalFilter = view.findViewById(R.id.btMetalFilter);
        Button btPlasticFilter = view.findViewById(R.id.btPlasticFilter);
        ImageButton btOtherFilter = view.findViewById(R.id.btOtherFilter);
        Button btClearFilters = view.findViewById(R.id.btClearFilters);

        btGlassFilter.setOnClickListener(v -> {
            buttonToggleManager.toggleButton(btGlassFilter);
            updateFilterList(btGlassFilter.isSelected(), "VIDRO");
            Log.d(LOG_TAG, "Filters selected: " + activeFilters);
        });

        btMetalFilter.setOnClickListener(v -> {
            buttonToggleManager.toggleButton(btMetalFilter);
            updateFilterList(btMetalFilter.isSelected(), "METAL");
            Log.d(LOG_TAG, "Filters selected: " + activeFilters);
        });

        btPlasticFilter.setOnClickListener(v -> {
            buttonToggleManager.toggleButton(btPlasticFilter);
            updateFilterList(btPlasticFilter.isSelected(), "PLASTICO");
            Log.d(LOG_TAG, "Filters selected: " + activeFilters);
        });

        btOtherFilter.setOnClickListener(v -> DialogUtils.showFilterDialog(FeedFragment.this, new FilterDialogCallback() {
            @Override
            public void onFilterSelected(List<String> filters, String closeAt, String minWeight, String maxWeight) {
                Log.d(LOG_TAG, "Filters selected: " + filters);
                activeFilters.clear();
                activeFilters.addAll(filters);
                if (validateFilters(closeAt, minWeight, maxWeight)) {
                    Log.e(LOG_TAG, "Fecha:" + closeAt);
                    Log.e(LOG_TAG, "Peso min:" + minWeight);
                    Log.e(LOG_TAG, "Peso max:" + maxWeight);
                    applyAdditionalFilters(closeAt, minWeight, maxWeight);
                    btClearFilters.setVisibility(View.VISIBLE);
                } else {
                    DialogUtils.showCustomFeedDialog(FeedFragment.this);
                }
            }
        }));

        btClearFilters.setOnClickListener(v -> {
            clearFilters();
            btClearFilters.setVisibility(View.INVISIBLE);
        });

        fetchAuctionData();
        return view;
    }

    public void clearFilters(){
        auctionList.clear();
        closeAt = "";
        minWeight = "";
        maxWeight = "";
        fetchAuctionData();
    }


    // Método para carregar leilões sem filtro
    private void fetchAuctionData() {
        Call<ApiDataResponseAuction<List<Auction>>> call = apiService.getAllAuctions();

        call.enqueue(new Callback<ApiDataResponseAuction<List<Auction>>>() {
            @Override
            public void onResponse(Call<ApiDataResponseAuction<List<Auction>>> call, Response<ApiDataResponseAuction<List<Auction>>> response) {
                Log.d(LOG_TAG, "onResponse called with status code: " + response.code());

                // Verifique o código de status HTTP
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(LOG_TAG, "Response is successful, processing auctions.");

                    auctionList.clear();  // Limpar lista antes de adicionar novos itens
                    Log.d(LOG_TAG, "Auction list cleared.");

                    if (response.body().getData() != null) {
                        auctionList.addAll(response.body().getData());
                        Log.d(LOG_TAG, "Auctions added: " + response.body().getData().size());
                    } else {
                        Log.d(LOG_TAG, "No auction data found in response body.");
                    }

                    auctionAdapter.notifyDataSetChanged();  // Atualizar RecyclerView
                } else {
                    Log.e(LOG_TAG, "Response failed: " + response.message() + " (Code: " + response.code() + ")");

                    // Se o código da resposta for 404, ou outro erro específico
                    if (response.code() == 404) {
                        Log.e(LOG_TAG, "Error 404: No auctions found.");
                        Toast.makeText(getContext(), "No auctions available (404).", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(LOG_TAG, "Unexpected error: " + response.code());
                        Toast.makeText(getContext(), "Failed to load auctions (Error: " + response.code() + ").", Toast.LENGTH_SHORT).show();
                    }

                    // Sempre limpar a lista de leilões ao ocorrer um erro
                    auctionList.clear();
                    Log.d(LOG_TAG, "Auction list cleared after failed response.");
                    auctionAdapter.notifyDataSetChanged();  // Atualizar RecyclerView para exibir o feed vazio
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponseAuction<List<Auction>>> call, Throwable t) {
                Log.e(LOG_TAG, "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch data: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                // Limpar lista e atualizar adapter em caso de falha
                auctionList.clear();
                Log.d(LOG_TAG, "Auction list cleared after API failure.");
                auctionAdapter.notifyDataSetChanged();  // Atualizar RecyclerView
            }
        });
    }

    // Atualizar lista de filtros com base no estado do botão
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

    // Validar os filtros de data e peso
    public boolean validateFilters(String closeAt, String minWeight, String maxWeight) {
        // Validar data de fechamento
        if (closeAt != null && !closeAt.isEmpty()) {
            if (!isValidDate(closeAt)) {
                Toast.makeText(getContext(), "Data de fechamento inválida. Não pode ser anterior à data atual.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        // Validar peso mínimo
        if (minWeight != null && !minWeight.isEmpty()) {
            try {
                int minWeightValue = Integer.parseInt(minWeight);
                if (minWeightValue < 1) {
                    Toast.makeText(getContext(), "Peso mínimo deve ser maior que 0.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Peso mínimo inválido.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        // Validar peso máximo
        if (maxWeight != null && !maxWeight.isEmpty()) {
            try {
                int maxWeightValue = Integer.parseInt(maxWeight);
                if (maxWeightValue > Integer.MAX_VALUE || maxWeightValue < 1) {
                    Toast.makeText(getContext(), "Peso máximo inválido. Deve ser maior que 0 e menor que " + Integer.MAX_VALUE, Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Peso máximo inválido.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    // Método para validar se a data é maior ou igual à data atual
    private boolean isValidDate(String dateStr) {
        try {
            String[] dateParts = dateStr.split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            Calendar inputDate = Calendar.getInstance();
            inputDate.set(year, month - 1, day);

            Calendar currentDate = Calendar.getInstance();
            return !inputDate.before(currentDate);
        } catch (Exception e) {
            return false;
        }
    }

    // Método para aplicar os filtros adicionais
    public void applyAdditionalFilters(String closeAt, String minWeight, String maxWeight) {
        this.closeAt = closeAt;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        applyFilterOrReset();
    }

    // Método para aplicar ou resetar os filtros
    private void applyFilterOrReset() {
        if (activeFilters.isEmpty() && closeAt == null && minWeight == null && maxWeight == null) {
            fetchAuctionData();  // Carregar todos os leilões sem filtros
        } else {
            fetchAuctionDataWithFilter(activeFilters, closeAt, minWeight, maxWeight);
        }
    }

    // Método para aplicar filtros baseados nos parâmetros selecionados
    private void fetchAuctionDataWithFilter(List<String> filters, String closeAt, String weightMin, String weightMax) {
        Call<ApiDataResponseAuction<List<Auction>>> call = apiService.getFilteredAuctions(filters, closeAt, weightMin, weightMax);

        call.enqueue(new Callback<ApiDataResponseAuction<List<Auction>>>() {
            @Override
            public void onResponse(Call<ApiDataResponseAuction<List<Auction>>> call, Response<ApiDataResponseAuction<List<Auction>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(LOG_TAG, "Filtered auctions loaded successfully");

                    auctionList.clear();

                    if (response.body().getData() != null) {
                        auctionList.addAll(response.body().getData());
                    }

                    auctionAdapter.notifyDataSetChanged();
                } else {
                    Log.e(LOG_TAG, "Response failed: " + response.message());
                    Toast.makeText(getContext(), "Failed to load filtered auctions.", Toast.LENGTH_SHORT).show();

                    // Limpa a lista de leilões em caso de erro
                    auctionList.clear();
                    auctionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponseAuction<List<Auction>>> call, Throwable t) {
                Log.e(LOG_TAG, "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Failed to fetch filtered data.", Toast.LENGTH_SHORT).show();

                auctionList.clear();
                auctionAdapter.notifyDataSetChanged();
            }
        });
    }

}
