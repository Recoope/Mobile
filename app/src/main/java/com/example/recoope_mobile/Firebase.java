package com.example.recoope_mobile;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.recoope_mobile.model.Cooperative;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Firebase {

    private static final String COLLECTION_NAME_SEARCH = "searchHistory";
    private static final String COLLECTION_NAME_IMAGE = "imgProfileCooperative";
    private final Context context;
    private FirebaseFirestore db;

    private String cnpj;

    public Firebase(Context context) {
        db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public Task<Void> saveCooperativeSearchHistory(Cooperative cooperative) {
        if (cooperative == null) {
            Log.e("Firebase", "Cooperativa é null, não é possível salvar no Firebase");
            return Tasks.forException(new Exception("Cooperativa é null"));
        }

        String cnpj = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("cnpj", "");

        if (cnpj.isEmpty()) {
            Log.e("Firebase", "CNPJ da empresa não encontrado");
            return Tasks.forException(new Exception("CNPJ da empresa não encontrado"));
        }

        Map<String, Object> cooperativeData = new HashMap<>();
        cooperativeData.put("cnpj", cooperative.getCnpj());
        cooperativeData.put("name", cooperative.getName());
        cooperativeData.put("email", cooperative.getEmail());
        cooperativeData.put("status", cooperative.getStatus());

        DocumentReference docRef = db.collection(COLLECTION_NAME_SEARCH).document(cnpj);

        return docRef.get().continueWithTask(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().exists()) {
                    Map<String, Object> initialData = new HashMap<>();
                    initialData.put("cooperatives", new ArrayList<>());
                    return docRef.set(initialData)
                            .continueWith(aVoid -> {
                                Log.d("Firebase", "Documento criado");
                                return null;
                            });
                }

                return docRef.update("cooperatives", FieldValue.arrayUnion(cooperativeData))
                        .continueWith(aVoid -> {
                            Log.d("Firebase", "Cooperativa adicionada ao histórico com sucesso");
                            return null;
                        });
            } else {
                Log.e("Firebase", "Erro ao acessar documento da empresa", task.getException());
                return Tasks.forException(task.getException());
            }
        });
    }


    public Task<List<Cooperative>> getCooperativeSearchHistory() {
        TaskCompletionSource<List<Cooperative>> taskCompletionSource = new TaskCompletionSource<>();

        db.collection(COLLECTION_NAME_SEARCH)
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
                            taskCompletionSource.setResult(cooperatives);
                        } else {
                            taskCompletionSource.setException(new Exception("No records found"));
                        }
                    } else {
                        taskCompletionSource.setException(task.getException());
                    }
                });

        return taskCompletionSource.getTask();
    }


    public void deleteAllDocuments(OnDeleteDocumentsListener listener) {
        db.collection(COLLECTION_NAME_SEARCH)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection(COLLECTION_NAME_SEARCH).document(document.getId()).delete();
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

        DocumentReference docRef = db.collection(COLLECTION_NAME_SEARCH).document(cnpj);

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
    public void saveProfileImage(Uri imageUri, OnSuccessListener<Uri> successListener, OnFailureListener failureListener) {
        String cnpj = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("cnpj", "");

        if (cnpj.isEmpty()) {
            Log.e("Firebase", "CNPJ da empresa não encontrado");
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("profile_images/" + cnpj + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Log para indicar que o upload foi bem-sucedido
                    Log.d("Firebase", "Upload da imagem de perfil concluído com sucesso.");

                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Chame o listener de sucesso passado como parâmetro
                        if (successListener != null) {
                            successListener.onSuccess(uri);
                        }
                        // Após o upload, obtenha o URL de download e salve no Firestore
                        saveImageUrlInFirestore(uri.toString(), cnpj);
                    }).addOnFailureListener(e -> {
                        Log.e("Firebase", "Erro ao obter a URL de download da imagem", e);
                        if (failureListener != null) {
                            failureListener.onFailure(e);
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Erro ao fazer upload da imagem", e);
                    if (failureListener != null) {
                        failureListener.onFailure(e);
                    }
                });
    }


    // Função auxiliar para salvar o URL da imagem no Firestore
    private void saveImageUrlInFirestore(String imageUrl, String cnpj) {
        DocumentReference docRef = db.collection(COLLECTION_NAME_IMAGE).document(cnpj);

        Map<String, Object> imageData = new HashMap<>();
        imageData.put("profileImageUrl", imageUrl);

        docRef.set(imageData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Imagem de perfil salva com sucesso no Firestore"))
                .addOnFailureListener(e -> Log.e("Firebase", "Erro ao salvar a URL da imagem no Firestore", e));
    }

    public Task<String> getProfileImageUrl() {
        String cnpj = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("cnpj", "");

        if (cnpj.isEmpty()) {
            Log.e("Firebase", "CNPJ da empresa não encontrado");
            return Tasks.forException(new Exception("CNPJ não encontrado"));
        }

        DocumentReference docRef = db.collection(COLLECTION_NAME_IMAGE).document(cnpj);

        return docRef.get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                String imageUrl = task.getResult().getString("profileImageUrl");

                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Log.d("Firebase", "URL da imagem de perfil obtida com sucesso: " + imageUrl);
                    return imageUrl;
                } else {
                    throw new Exception("URL da imagem de perfil não encontrada");
                }
            } else {
                throw new Exception("Erro ao buscar o documento do Firestore");
            }
        }).addOnFailureListener(e -> Log.e("Firebase", "Erro ao buscar a imagem de perfil", e));
    }




}
