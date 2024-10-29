package com.example.recoope_mobile.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recoope_mobile.Firebase;
import com.example.recoope_mobile.R;
import com.example.recoope_mobile.activity.fragments.CooperativeFragment;
import com.example.recoope_mobile.model.Cooperative;

import java.util.List;

public class CooperativeAdapter extends RecyclerView.Adapter<CooperativeAdapter.CooperativeViewHolder> {

    private List<Cooperative> cooperatives;
    private LayoutInflater inflater;
    private final String LOG_TAG = "CooperativeAdapter";
    private Firebase firebase;
    private boolean isSearchMode;

    public CooperativeAdapter(List<Cooperative> cooperatives, LayoutInflater inflater, boolean isSearchMode) {
        this.cooperatives = cooperatives;
        this.inflater = inflater;
        this.firebase = new Firebase(inflater.getContext());
        this.isSearchMode = isSearchMode;
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

            holder.txtCooperativeName.setText(cooperative.getName() != null ? cooperative.getName() : "Cooperative not found");


            holder.btDeleteHistory.setOnClickListener(v -> {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    firebase.deleteCooperativeFromHistory(cooperative, new Firebase.OnDeleteDocumentsListener() {
                        @Override
                        public void onSuccess() {
                            cooperatives.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                            Toast.makeText(inflater.getContext(), "Cooperative deleted from history", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(inflater.getContext(), "Failed to delete cooperative: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            holder.itemView.setOnClickListener(v -> {
                if (isSearchMode) {
                    firebase.saveCooperativeSearchHistory(cooperative);
                    Log.d(LOG_TAG, "Cooperative " + cooperative.getName() + " saved to Firebase history.");
                    Toast.makeText(v.getContext(), "Cooperative saved to search history", Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainContent, new CooperativeFragment());
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