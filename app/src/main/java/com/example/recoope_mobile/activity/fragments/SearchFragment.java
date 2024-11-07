package com.example.recoope_mobile.activity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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
import com.example.recoope_mobile.Retrofit.LoggerClient;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.activity.MainActivity;
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
    private TextView txtRecentSearch;
    private final Firebase firebase = new Firebase(getContext());
    private MainActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LoggerClient.postLog(getContext(), "SEARCH");
        View view = inflater.inflate(R.layout.search, container, false);

        Log.d(LOG_TAG, "Activity created");

        recyclerView = view.findViewById(R.id.searchRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cooperativeList = new ArrayList<>();

        cooperativeAdapter = new CooperativeAdapter(cooperativeList, inflater, true, getContext());  // true indica que estamos no modo pesquisa
        recyclerView.setAdapter(cooperativeAdapter);

        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

        TextView btClearResults = view.findViewById(R.id.btClearResults);
        etWord = view.findViewById(R.id.nameSearching);
        txtRecentSearch = view.findViewById(R.id.txtRecentSearch);

        activity = (MainActivity) requireActivity();

        btClearResults.setOnClickListener(r -> clearResults());

        firebase.getCooperativeSearchHistory()
                .addOnSuccessListener(cooperatives -> {
                    cooperativeList.clear();
                    cooperativeList.addAll(cooperatives);
                    cooperativeAdapter.notifyDataSetChanged();
                    activity.hideLoading();

                })
                .addOnFailureListener(e -> {
                    Log.e("SearchFragment", "Error fetching cooperatives: " + e.getMessage());
                });

        etWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    btClearResults.setVisibility(View.GONE);
                    txtRecentSearch.setVisibility(View.GONE);
                    getCooperatives(charSequence.toString());
                } else {
                    btClearResults.setVisibility(View.VISIBLE);
                    firebase.getCooperativeSearchHistory()
                            .addOnSuccessListener(cooperatives -> {
                                cooperativeList.clear();
                                cooperativeList.addAll(cooperatives);
                                cooperativeAdapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("SearchFragment", "Error fetching cooperatives: " + e.getMessage());
                            });
                }
                recyclerView.requestLayout();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etWord.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etWord, InputMethodManager.SHOW_IMPLICIT);
    }

    public void clearResults() {
        etWord.setText("");

        firebase.deleteAllDocuments(new Firebase.OnDeleteDocumentsListener() {
            @Override
            public void onSuccess() {
                cooperativeList.clear();
                cooperativeAdapter.notifyDataSetChanged();


                fetchHistoryFromFirebase();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(), "Algo deu errado, volte mais tarde!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchHistoryFromFirebase() {
        firebase.getCooperativeSearchHistory()
                .addOnSuccessListener(cooperatives -> {
                    cooperativeList.clear();
                    cooperativeList.addAll(cooperatives);
                    cooperativeAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("SearchFragment", "Error fetching cooperatives: " + e.getMessage());
                });
    }



    private void getCooperatives(String name) {
        Log.d(LOG_TAG, "Searching cooperatives with term: " + name);
        activity = (MainActivity) getActivity();
        Call<ApiDataResponse<List<Cooperative>>> call = apiService.getSearchCooperative(name);

        if (isAdded()) {
            call.enqueue(new Callback<ApiDataResponse<List<Cooperative>>>() {
                @Override
                public void onResponse(Call<ApiDataResponse<List<Cooperative>>> call, Response<ApiDataResponse<List<Cooperative>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        activity.hideLoading();
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
                    Toast.makeText(requireContext(), "Algo deu errado, volte mais tarde!", Toast.LENGTH_LONG).show();
                    handleCooperativeFailure(t);
                }
            });
        } else {
            Toast.makeText(requireContext(), "Algo deu errado, volte mais tarde!", Toast.LENGTH_LONG).show();
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
        }
    }




    private void handleCooperativeFailure(Throwable t) {
        Log.e(LOG_TAG, "Error while fetching cooperatives: " + t.getMessage());
        cooperativeList.clear();
        cooperativeAdapter.notifyDataSetChanged();
    }
}
