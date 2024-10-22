package com.example.recoope_mobile;

import android.util.Log;

import com.example.recoope_mobile.model.Cooperative;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Firebase {

    private static final String COLLECTION_NAME = "searchHistory";
    private FirebaseFirestore db;

    public Firebase() {
        // Inicializa o Firestore
        db = FirebaseFirestore.getInstance();
    }

    // Método para salvar cooperativas pesquisadas no Firestore
    public void saveCooperativeSearchHistory(Cooperative cooperative) {
        if (cooperative == null) {
            Log.e("Firebase", "Cooperativa é null, não é possível salvar no Firebase");
            return;
        }

        // Criar um mapa com os dados da cooperativa
        Map<String, Object> cooperativeData = new HashMap<>();
        cooperativeData.put("cnpj", cooperative.getCnpj());
        cooperativeData.put("nome", cooperative.getName());
        cooperativeData.put("email", cooperative.getEmail());
        cooperativeData.put("status", cooperative.getStatus());

        // Adicionar ao Firestore
        db.collection(COLLECTION_NAME)
                .add(cooperativeData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firebase", "Histórico de pesquisa salvo com ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Erro ao salvar o histórico: ", e);
                });
    }

    // Método para buscar históricos de cooperativas no Firestore
    public void getCooperativeSearchHistory(OnSearchHistoryFetchedListener listener) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Cooperative> cooperatives = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Criar cooperativa com os dados retornados
                                Cooperative cooperative = document.toObject(Cooperative.class);
                                Log.e("Firebase", cooperative.toString());
                                cooperatives.add(cooperative);
                            }
                            listener.onSuccess(cooperatives);
                        } else {
                            listener.onFailure("No records found");
                        }
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }
                });
    }

    // Interface para passar os resultados da busca
    public interface OnSearchHistoryFetchedListener {
        void onSuccess(List<Cooperative> cooperatives);
        void onFailure(String errorMessage);
    }

}
