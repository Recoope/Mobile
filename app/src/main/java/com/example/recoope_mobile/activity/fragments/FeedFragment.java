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
        View view = inflater.inflate(R.layout.feed, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        auctionList = new ArrayList<>();
        auctionAdapter = new AuctionAdapter(auctionList, getContext());
        recyclerView.setAdapter(auctionAdapter);

        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        buttonToggleManager = new ButtonToggleManager(getContext(), R.color.recoope_primary_color, R.color.background_color);

        // Configurar botões de filtro
        setupFilterButtons(view);

        fetchAuctionData();  // Carregar leilões inicialmente
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
                // Limpar e adicionar novos filtros
                activeFilters.clear();
                activeFilters.addAll(filters);

                // Sincronizar os botões do feed com os filtros do diálogo
                syncFiltersBetweenFeedAndDialog(btGlassFilter, btMetalFilter, btPlasticFilter);

                if (validateFilters(closeAt, minWeight, maxWeight)) {
                    applyAdditionalFilters(closeAt, minWeight, maxWeight);
                    btClearFilters.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Filtros inválidos.", Toast.LENGTH_SHORT).show();
                }
            }
        }, activeFilters));


        btClearFilters.setOnClickListener(v -> clearFilters(btClearFilters));
    }

    // Sincronizar estado dos botões de filtro entre o diálogo e o feed
    public void syncFiltersBetweenFeedAndDialog(Button btGlassFilter, Button btMetalFilter, Button btPlasticFilter) {
        // Atualizar estado selecionado para os botões com base nos filtros ativos
        btGlassFilter.setSelected(activeFilters.contains("VIDRO"));
        btMetalFilter.setSelected(activeFilters.contains("METAL"));
        btPlasticFilter.setSelected(activeFilters.contains("PLASTICO"));

        // Aplicar a cor de fundo correta aos botões com base na seleção usando o método correto da ButtonToggleManager
        buttonToggleManager.setButtonState(btGlassFilter, btGlassFilter.isSelected());
        buttonToggleManager.setButtonState(btMetalFilter, btMetalFilter.isSelected());
        buttonToggleManager.setButtonState(btPlasticFilter, btPlasticFilter.isSelected());
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
        applyFilterOrReset();  // Aplicar ou remover filtros conforme o estado
    }

    // Aplicar filtros adicionais (data e peso)
    public void applyAdditionalFilters(String closeAt, String minWeight, String maxWeight) {
        this.closeAt = closeAt;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        applyFilterOrReset();
    }

    // Centralizar a lógica de aplicação ou reset de filtros
    private void applyFilterOrReset() {
        if (activeFilters.isEmpty() && closeAt == null && minWeight == null && maxWeight == null) {
            fetchAuctionData();  // Carregar leilões sem filtro
        } else {
            fetchAuctionDataWithFilter(activeFilters, closeAt, minWeight, maxWeight);  // Carregar leilões filtrados
        }
    }

    // Limpar todos os filtros e recarregar os dados
    public void clearFilters(Button btClearFilters) {
        activeFilters.clear();
        closeAt = null;
        minWeight = null;
        maxWeight = null;
        btClearFilters.setVisibility(View.INVISIBLE);
        fetchAuctionData();  // Recarregar todos os leilões
    }

    // Método para validar se os filtros de data e peso são corretos
    public boolean validateFilters(String closeAt, String minWeight, String maxWeight) {
        return isValidDate(closeAt) && isValidWeight(minWeight) && isValidWeight(maxWeight);
    }

    // Validações
    private boolean isValidDate(String dateStr) {
        // Implementação de validação de data aqui
        return true;  // Exemplo simples
    }

    private boolean isValidWeight(String weightStr) {
        // Implementação de validação de peso aqui
        return true;  // Exemplo simples
    }

    // Métodos para buscar dados com ou sem filtros
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
