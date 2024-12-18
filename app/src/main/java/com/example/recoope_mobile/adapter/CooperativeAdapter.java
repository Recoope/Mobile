package com.example.recoope_mobile.adapter;

import static com.example.recoope_mobile.utils.ValidationUtils.calculateCardWidthDp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recoope_mobile.Firebase;
import com.example.recoope_mobile.R;
import com.example.recoope_mobile.activity.fragments.CooperativeFragment;
import com.example.recoope_mobile.model.Cooperative;
import com.example.recoope_mobile.utils.ValidationUtils;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class CooperativeAdapter extends RecyclerView.Adapter<CooperativeAdapter.CooperativeViewHolder> {

    private List<Cooperative> cooperatives;
    private LayoutInflater inflater;
    private final String LOG_TAG = "CooperativeAdapter";
    private Firebase firebase;
    private boolean isSearchMode;
    private Context context;
    private int screenWidthDp; // Adicionado para armazenar a largura da tela

    public CooperativeAdapter(List<Cooperative> cooperatives, LayoutInflater inflater, boolean isSearchMode, Context context) {
        this.cooperatives = cooperatives;
        this.inflater = inflater;
        this.firebase = new Firebase(inflater.getContext());
        this.isSearchMode = isSearchMode;
        this.context = context;
    }

    @NonNull
    @Override
    public CooperativeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_search_result, parent, false);
        return new CooperativeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CooperativeViewHolder holder, int position) {
        Cooperative cooperative = cooperatives.get(position);

        if (cooperative != null) {
            Log.d(LOG_TAG, "Cooperative: " + cooperative.toString());

            // Usando a largura da tela e o contexto
            String truncatedName = ValidationUtils.truncateString(inflater.getContext(), cooperative.getName(), calculateCardWidthDp(context, 0.65));
            holder.txtCooperativeName.setText(truncatedName != null ? truncatedName : "Cooperative not found");

            holder.btDeleteHistory.setOnClickListener(v -> {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    firebase.deleteCooperativeFromHistory(cooperative, new Firebase.OnDeleteDocumentsListener() {
                        @Override
                        public void onSuccess() {
                            cooperatives.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.e(LOG_TAG, "Failed to delete cooperative: " + errorMessage);
                        }
                    });
                }
            });

            holder.itemView.setOnClickListener(v -> {
                if (isSearchMode) {
                    Task<Void> voidTask = firebase.saveCooperativeSearchHistory(cooperative)
                            .addOnSuccessListener(aVoid -> Log.e(LOG_TAG, "Histórico salvo!"))
                            .addOnFailureListener(e -> Log.e(LOG_TAG, e.getMessage()));
                    Log.d(LOG_TAG, "Cooperative " + cooperative.getName() + " saved to Firebase history.");

                    Bundle bundle = new Bundle();
                    bundle.putString("cnpjCooperative", cooperative.getCnpj());
                    Fragment cooperativeFragment = new CooperativeFragment();
                    cooperativeFragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainContent, cooperativeFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        } else {
            Log.e(LOG_TAG, "Cooperative at position " + position + " is null");
        }
    }

    @Override
    public int getItemCount() {
        return cooperatives.size();
    }

    public static class CooperativeViewHolder extends RecyclerView.ViewHolder {
        TextView txtCooperativeName;
        ImageButton btDeleteHistory;

        public CooperativeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCooperativeName = itemView.findViewById(R.id.txtCooperativeName);
            btDeleteHistory = itemView.findViewById(R.id.btDeleteHistory);
        }
    }
}
