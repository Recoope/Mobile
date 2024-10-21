package com.example.recoope_mobile.activity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recoope_mobile.Firebase;
import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.adapter.CooperativeAdapter;
import com.example.recoope_mobile.model.Cooperative;
import com.example.recoope_mobile.response.ApiDataResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private static final String LOG_TAG = "Search Cooperative";
    private RecyclerView recyclerView;
    private CooperativeAdapter cooperativeAdapter;
    private List<Cooperative> cooperativeList;
    private ApiService apiService;
    private EditText etWord;
    private final Firebase firebase = new Firebase();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);

        Log.d(LOG_TAG, "Activity created");

        // Inicialização do RecyclerView e Adapter
        recyclerView = view.findViewById(R.id.searchRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cooperativeList = new ArrayList<>();

        cooperativeAdapter = new CooperativeAdapter(cooperativeList, inflater, true);  // true indica que estamos no modo pesquisa
        recyclerView.setAdapter(cooperativeAdapter);

        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

        TextView btClearResults = view.findViewById(R.id.btClearResults);
        etWord = view.findViewById(R.id.nameSearching);

        btClearResults.setOnClickListener(r -> clearResults());

        // Adiciona um TextWatcher para capturar mudanças no campo de texto
        etWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Não precisa de implementação
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    // Usuário está digitando, ajustar o RecyclerView e buscar os resultados
                    recyclerView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    getCooperatives(charSequence.toString());  // Chamar o método de pesquisa
                } else {
                    // Usuário não está digitando, exibir o histórico
                    firebase.getCooperativeSearchHistory(new Firebase.OnSearchHistoryFetchedListener() {
                        @Override
                        public void onSuccess(List<Cooperative> cooperatives) {
                            // Limpar lista atual
                            cooperativeList.clear();
                            // Adicionar a lista de cooperativas buscadas no histórico
                            cooperativeList.addAll(cooperatives);
                            // Notificar o adapter para atualizar a lista exibida
                            cooperativeAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            // Lidar com falhas ao buscar histórico
                            Toast.makeText(getContext(), "Failed to fetch history: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                recyclerView.requestLayout(); // Atualizar o layout após modificar o tamanho
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Não precisa de implementação
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etWord.requestFocus();  // Força o foco no EditText
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etWord, InputMethodManager.SHOW_IMPLICIT);  // Exibe o teclado
    }

    public void clearResults() {
        etWord.setText("");
        cooperativeList.clear();
        cooperativeAdapter.notifyDataSetChanged();
    }

    private void getCooperatives(String name) {
        Log.d(LOG_TAG, "Searching cooperatives with term: " + name);
        Call<ApiDataResponse<List<Cooperative>>> call = apiService.getSearchCooperative(name);

        if (isAdded()) {
            call.enqueue(new Callback<ApiDataResponse<List<Cooperative>>>() {
                @Override
                public void onResponse(Call<ApiDataResponse<List<Cooperative>>> call, Response<ApiDataResponse<List<Cooperative>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiDataResponse<List<Cooperative>> apiData = response.body();
                        Log.d(LOG_TAG, "API Response: " + apiData.getMessage());
                        handleCooperativeResponse(apiData);
                    } else {
                        Log.e(LOG_TAG, "Response was not successful or body is null");
                        handleCooperativeResponse(null);
                    }
                }

                @Override
                public void onFailure(Call<ApiDataResponse<List<Cooperative>>> call, Throwable t) {
                    Log.e(LOG_TAG, "API Failure: " + t.getMessage());
                    handleCooperativeFailure(t);
                }
            });
        } else {
            Log.e(LOG_TAG, "Fragment is not attached");
        }
    }

    private void handleCooperativeResponse(ApiDataResponse<List<Cooperative>> response) {
        if (response != null && response.getData() != null && !response.getData().isEmpty()) {
            cooperativeList.clear();
            cooperativeList.addAll(response.getData());
            cooperativeAdapter.notifyDataSetChanged();
        } else {
            Log.e(LOG_TAG, "No cooperatives found");
            cooperativeList.clear();
            cooperativeAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "No results found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCooperativeFailure(Throwable t) {
        Log.e(LOG_TAG, "Error while fetching cooperatives: " + t.getMessage());
        cooperativeList.clear();
        cooperativeAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
    }
}
