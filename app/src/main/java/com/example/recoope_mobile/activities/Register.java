package com.example.recoope_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.models.Company;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private final String LOG_TAG = "Register";

    private ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void returnScreen(View view){
        Intent intent = new Intent(Register.this, StartScreen.class);
        startActivity(intent);
        finish();
    }

    public void register(View view){
        EditText companyNameEt = findViewById(R.id.companyName);
        EditText companyCNPJEt = findViewById(R.id.companyDocument);
        EditText companyEmailEt = findViewById(R.id.companyEmail);
        EditText companyPhoneEt = findViewById(R.id.companyPhone);
        EditText companyPasswordEt = findViewById(R.id.companyPassword);
        EditText companyConfirmationPasswordEt = findViewById(R.id.companyPasswordConfirmation);

        String companyName = companyNameEt.getText().toString();
        String companyCNPJ = companyCNPJEt.getText().toString();
        String companyEmail = companyEmailEt.getText().toString();
        String companyPhone = companyPhoneEt.getText().toString();
        String companyPassword = companyPasswordEt.getText().toString();
        String companyPasswordConfirmation = companyPasswordEt.getText().toString();

        boolean invalidFormat = false;

        if(companyCNPJ.length() != 14){
            companyCNPJEt.setError("CNPJ com Formato inválido.");
            invalidFormat = true;
        }

        if(!(companyName.length() > 0) && !(companyName.length() <= 100)){
            companyNameEt.setError("Nome com tamanho inválido.");
            invalidFormat = true;
        }

        if(!(companyEmail.length() > 3) && !(companyEmail.length() < 100)){
            companyEmailEt.setError("E-mail tamanho inválido.");
            invalidFormat = true;
        }

        if(!(companyEmail.contains("@")) && !(companyEmail.contains("."))){
            companyEmailEt.setError("E-mail com formato inválido.");
            invalidFormat = true;
        }

        if(!(companyPassword.length() > 6) && !(companyPassword.length() <= 100)){
            companyPasswordEt.setError("Senha com tamanho inválida.");
            invalidFormat = true;
        }

        if(!(companyPasswordConfirmation.length() > 6) && !(companyPasswordConfirmation.length() <= 100) && companyPassword == companyPasswordConfirmation){
            companyPasswordEt.setError("Senha com tamanho inválida.");
            invalidFormat = true;
        }

        if(!(companyPhone.length() > 2) && !(companyPhone.length() <= 10)){
            companyPhoneEt.setError("Senha com tamanho inválida.");
            invalidFormat = true;
        }

        if(!invalidFormat){
            Call<Company> call = apiService.createCompany(new Company(companyCNPJ, companyName, companyEmail, companyPassword, companyPasswordConfirmation, companyPhone));

            call.enqueue(new Callback<Company>() {
                @Override
                public void onResponse(Call<Company> call, Response<Company> response) {
                    Log.d(LOG_TAG, "Reached this place");
                    if (response.code() >= 200 && response.code() < 300) {
                        Company company = response.body();
                        Log.d(LOG_TAG, "Company created successfully: " + company.getName());
                        // Do something with the company object
                    } else {
                        Log.d(LOG_TAG, "Error creating company: " + response.code());
                        Log.e(LOG_TAG, String.valueOf(response.body()));
                        // Handle error response
                    }
                }

                @Override
                public void onFailure(Call<Company> call, Throwable t) {
                    Log.e(LOG_TAG, "Error creating company: " + t.getMessage());
                    Log.e(LOG_TAG, t.getMessage());

                }
            });

        }


    }

}