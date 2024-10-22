package com.example.recoope_mobile;

import android.content.Context;
import android.util.Log;

import com.example.recoope_mobile.model.Cooperative;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Firebase {

    private static final String COLLECTION_NAME = "searchHistory";
    private final Context context;
    private FirebaseFirestore db;

    private String cnpj;

    public Firebase(Context context) {
        db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public void saveCooperativeSearchHistory(Cooperative cooperative) {
        if (cooperative == null) {
            Log.e("Firebase", "Cooperativa é null, não é possível salvar no Firebase");
            return;
        }

        String cnpj = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("cnpj", "");

        if (cnpj.isEmpty()) {
            Log.e("Firebase", "CNPJ da empresa não encontrado");
            return;
        }

        Map<String, Object> cooperativeData = new HashMap<>();
        cooperativeData.put("cnpj", cooperative.getCnpj());
        cooperativeData.put("name", cooperative.getName());
        cooperativeData.put("email", cooperative.getEmail());
        cooperativeData.put("status", cooperative.getStatus());

        DocumentReference docRef = db.collection(COLLECTION_NAME).document(cnpj);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().exists()) {
                    Map<String, Object> initialData = new HashMap<>();
                    initialData.put("cooperatives", new ArrayList<>());
                    docRef.set(initialData)
                            .addOnSuccessListener(aVoid -> Log.d("Firebase", "Documento criado"))
                            .addOnFailureListener(e -> Log.e("Firebase", "Falha ao criar documento", e));
                }

                docRef.update("cooperatives", FieldValue.arrayUnion(cooperativeData))
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firebase", "Cooperativa adicionada ao histórico com sucesso");
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firebase", "Erro ao salvar histórico de cooperativas", e);
                        });
            } else {
                Log.e("Firebase", "Erro ao acessar documento da empresa", task.getException());
            }
        });
    }

    public void getCooperativeSearchHistory(OnSearchHistoryFetchedListener listener) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Cooperative> cooperatives = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                List<Map<String, Object>> cooperativeMaps = (List<Map<String, Object>>) document.get("cooperatives");
                                if (cooperativeMaps != null) {
                                    for (Map<String, Object> cooperativeMap : cooperativeMaps) {
                                        Cooperative cooperative = new Cooperative();
                                        cooperative.setCnpj((String) cooperativeMap.get("cnpj"));
                                        cooperative.setName((String) cooperativeMap.get("name"));
                                        cooperative.setEmail((String) cooperativeMap.get("email"));
                                        cooperative.setStatus((String) cooperativeMap.get("status"));

                                        Log.e("Firebase", cooperative.toString());
                                        cooperatives.add(cooperative);
                                    }
                                }
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


    public interface OnSearchHistoryFetchedListener {
        void onSuccess(List<Cooperative> cooperatives);
        void onFailure(String errorMessage);
    }

    public void deleteAllDocuments(OnDeleteDocumentsListener listener) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection(COLLECTION_NAME).document(document.getId()).delete();
                        }
                        listener.onSuccess();
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }
                });
    }

    public interface OnDeleteDocumentsListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public void deleteCooperativeFromHistory(Cooperative cooperative, OnDeleteDocumentsListener listener) {
        if (cooperative == null || cooperative.getCnpj() == null) {
            listener.onFailure("Cooperative or CNPJ is null");
            return;
        }

        String cnpj = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("cnpj", "");

        if (cnpj.isEmpty()) {
            listener.onFailure("CNPJ da empresa não encontrado");
            return;
        }

        DocumentReference docRef = db.collection(COLLECTION_NAME).document(cnpj);

        Map<String, Object> cooperativeData = new HashMap<>();
        cooperativeData.put("cnpj", cooperative.getCnpj());
        cooperativeData.put("name", cooperative.getName());
        cooperativeData.put("email", cooperative.getEmail());
        cooperativeData.put("status", cooperative.getStatus());

        docRef.update("cooperatives", FieldValue.arrayRemove(cooperativeData))
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Cooperativa removida do histórico com sucesso");
                    listener.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Erro ao remover cooperativa do histórico", e);
                    listener.onFailure(e.getMessage());
                });
    }



}
