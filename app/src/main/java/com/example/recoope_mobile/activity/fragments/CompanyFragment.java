package com.example.recoope_mobile.activity.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;
import com.example.recoope_mobile.Firebase;
import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.activity.EditCompany;
import com.example.recoope_mobile.activity.MainActivity;
import com.example.recoope_mobile.model.CompanyProfile;
import com.example.recoope_mobile.response.ApiDataResponse;
import com.example.recoope_mobile.utils.ValidationUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyFragment extends Fragment {

    private final String LOG_TAG = "CardProfile";

    private String name;
    private String cnpj;
    private String email;
    private String phone;
    private String participatedAuctions;
    private Button exit;

    private TextView textViewName;
    private TextView textViewCnpj;
    private TextView textViewEmail;
    private TextView textViewPhone;
    private ImageView imgCompany;
    private ImageView btEditPhotoCompany;
    private TextView textViewParticipatedAuctions;
    private ApiService apiService;
    private TextView btnUpdateProfile;

    // Gerenciador para a seleção de imagens
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private MainActivity activity;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.profile, container, false);

        textViewName = view.findViewById(R.id.cooperativeName);
        textViewCnpj = view.findViewById(R.id.cooperativeCNPJ);
        textViewEmail = view.findViewById(R.id.cooperativeEmail);
        textViewPhone = view.findViewById(R.id.cooperativePhone);
        textViewParticipatedAuctions = view.findViewById(R.id.cooperativeParticipatedAuctions);
        exit = view.findViewById(R.id.exitButton);
        imgCompany = view.findViewById(R.id.imgCompany);
        btEditPhotoCompany = view.findViewById(R.id.btEditPhotoCompany);
        activity = (MainActivity) getActivity();
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);

        apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        Firebase firebase = new Firebase(getContext());

        btnUpdateProfile.setOnClickListener(r -> {
            Intent intent = new Intent(getActivity(), EditCompany.class);
            startActivity(intent);
        });

        // Inicializar o gerenciador de seleção de imagens
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            // Exibir o loading antes de iniciar o processo de upload e carregamento da imagem
                            activity.showLoading();

                            // Salvar a imagem no Firebase
                            firebase.saveProfileImage(selectedImageUri,
                                    uri -> {
                                        // Carregar a imagem salva no ImageView usando Glide
                                        Glide.with(this)
                                                .load(uri)
                                                .listener(new com.bumptech.glide.request.RequestListener<Drawable>() {
                                                    @Override
                                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                        // Ocultar o loading em caso de falha no carregamento da imagem
                                                        activity.hideLoading();
                                                        Toast.makeText(getContext(), "Erro ao carregar a imagem", Toast.LENGTH_SHORT).show();
                                                        return false; // Permite que o Glide lide com o erro
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                        // Ocultar o loading quando a imagem for carregada com sucesso
                                                        activity.hideLoading();
                                                        return false; // Permite que o Glide lide com a exibição da imagem
                                                    }
                                                })
                                                .into(imgCompany);

                                        Toast.makeText(getContext(), "Imagem de perfil atualizada com sucesso", Toast.LENGTH_SHORT).show();
                                    },
                                    e -> {
                                        // Ocultar o loading em caso de erro ao salvar no Firebase
                                        activity.hideLoading();
                                        Toast.makeText(getContext(), "Erro ao atualizar a imagem de perfil", Toast.LENGTH_SHORT).show();
                                    }
                            );
                        }
                    }
                }
        );


        // Inicializar o gerenciador de permissões
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Log.d(LOG_TAG, "Permissão de leitura concedida, abrindo galeria");
                        openGallery();
                    } else {
                        activity.hideLoading();
                        Toast.makeText(getContext(), "Permissão negada para acessar a galeria", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        btEditPhotoCompany.setOnClickListener(v -> {
            activity.showLoading();
            Log.d(LOG_TAG, "Botão de editar foto clicado");
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                Log.d(LOG_TAG, "Permissão de leitura não concedida, solicitando permissão");
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                Log.d(LOG_TAG, "Permissão de leitura concedida, abrindo galeria");
                openGallery();
            }
        });

        // Carregar a imagem do Firebase
        firebase.getProfileImageUrl()
                .addOnSuccessListener(imageUrl -> {
                    loadCompanyImage(imageUrl);
                    activity.hideLoading();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Erro ao salvar a imagem de perfil", e);
                    Toast.makeText(getContext(), "Erro ao salvar a imagem. Tente novamente.", Toast.LENGTH_SHORT).show();
                });

        fetchCompany();

        return view;
    }

    // Método para abrir a galeria
    private void openGallery() {
        Log.d(LOG_TAG, "Tentando abrir a galeria");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    // Método para carregar a imagem da empresa
    private void loadCompanyImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .listener(new com.bumptech.glide.request.RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        activity.hideLoading();
                        return false;
                    }
                })
                .into(imgCompany);
    }


    private void fetchCompany() {
        String cnpj = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("cnpj", "");
        Call<ApiDataResponse<CompanyProfile>> call = apiService.getCompanyById(cnpj);

        activity = (MainActivity) getActivity();

        call.enqueue(new Callback<ApiDataResponse<CompanyProfile>>() {
            @Override
            public void onResponse(Call<ApiDataResponse<CompanyProfile>> call, Response<ApiDataResponse<CompanyProfile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        activity.hideLoading();
                        ApiDataResponse<CompanyProfile> apiResponse = response.body();
                        name = apiResponse.getData().getName();
                        email = apiResponse.getData().getEmail();
                        phone = apiResponse.getData().getPhone();
                        participatedAuctions = apiResponse.getData().getParticipatedAuctions();

                        textViewCnpj.setText(String.format("%s.%s.%s/%s-%s", cnpj.substring(0, 2), cnpj.substring(2, 5), cnpj.substring(5, 8), cnpj.substring(8, 12), cnpj.substring(11, 14)));
                        textViewName.setText(ValidationUtils.truncateString(name, 30));
                        textViewEmail.setText(ValidationUtils.truncateString(email, 35));
                        textViewPhone.setText(phone);
                        textViewParticipatedAuctions.setText(ValidationUtils.truncateString(String.valueOf(participatedAuctions), 15));

                        // Botão de sair
                        exit.setOnClickListener(v -> {
                            getActivity().finish();
                        });

                        Log.d(LOG_TAG, "Company fetched successfully");
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Erro ao processar resposta da API", e);
                    }
                } else {
                    Log.e(LOG_TAG, "Response failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponse<CompanyProfile>> call, Throwable t) {
                Log.e(LOG_TAG, "API call failed: " + t.getMessage());
            }
        });
    }
}
