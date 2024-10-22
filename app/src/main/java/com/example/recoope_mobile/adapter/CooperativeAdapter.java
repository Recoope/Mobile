package com.example.recoope_mobile.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.recoope_mobile.activity.fragments.MainActivity;
import com.example.recoope_mobile.model.Cooperative;

import java.util.List;

public class CooperativeAdapter extends RecyclerView.Adapter<CooperativeAdapter.CooperativeViewHolder> {

    private List<Cooperative> cooperatives;
    private LayoutInflater inflater;
    private final String LOG_TAG = "CooperativeAdapter";
    private Firebase firebase;
    private boolean isSearchMode; // Flag para diferenciar entre pesquisa e histórico

    // Construtor que recebe o LayoutInflater em vez do Context
    public CooperativeAdapter(List<Cooperative> cooperatives, LayoutInflater inflater, boolean isSearchMode) {
        this.cooperatives = cooperatives;
        this.inflater = inflater;
        this.firebase = new Firebase();
        this.isSearchMode = isSearchMode; // Determina se estamos no modo pesquisa ou histórico
    }

    @NonNull
    @Override
    public CooperativeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflando o layout da view
        View view = inflater.inflate(R.layout.item_search_result, parent, false);
        return new CooperativeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CooperativeViewHolder holder, int position) {
        // Pegando a cooperativa na posição especificada
        Cooperative cooperative = cooperatives.get(position);

        // Log de depuração
        if (cooperative != null) {
            Log.d(LOG_TAG, "Cooperative: " + cooperative.toString());

            // Verificando e definindo o nome da cooperativa
            if (cooperative.getName() != null) {
                holder.txtCooperativeName.setText(cooperative.getName());
            } else {
                holder.txtCooperativeName.setText("Cooperative not found");
            }

            // Configurando o click listener para salvar a cooperativa no Firebase, se for modo pesquisa
            holder.itemView.setOnClickListener(v -> {
                if (isSearchMode) {
                    // Se estamos no modo de pesquisa, salvamos no Firebase
                    firebase.saveCooperativeSearchHistory(cooperative);
                    Log.d(LOG_TAG, "Cooperative " + cooperative.getName() + " saved to Firebase history.");
                    Toast.makeText(v.getContext(), "Cooperative saved to search history", Toast.LENGTH_SHORT).show();
                    // Bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("cnpjCooperative", cooperative.getCnpj());
                    //Abrir a tela
                    Fragment cooperativeFragment = new CooperativeFragment();
                    cooperativeFragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainContent, cooperativeFragment); // Certifique-se de usar o ID correto do container
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else {
                    // Se estamos no modo histórico, mostramos os detalhes ou outra ação
                    showHistoryItemDetails(cooperative);
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

    // Método para atualizar a lista de cooperativas
    public void updateData(List<Cooperative> newCooperatives) {
        this.cooperatives.clear();
        this.cooperatives.addAll(newCooperatives);
        notifyDataSetChanged(); // Atualiza a RecyclerView com a nova lista
    }

    // Método para mostrar os detalhes de um item de histórico
    private void showHistoryItemDetails(Cooperative cooperative) {
        // Lógica para exibir detalhes (como uma nova tela ou fragmento com informações detalhadas)
        Toast.makeText(inflater.getContext(), "Details: " + cooperative.getName(), Toast.LENGTH_SHORT).show();
    }

    // ViewHolder interno
    public static class CooperativeViewHolder extends RecyclerView.ViewHolder {
        TextView txtCooperativeName;

        public CooperativeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCooperativeName = itemView.findViewById(R.id.txtCooperativeName);
        }
    }
}
