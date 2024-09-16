package com.example.recoope_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.models.Company;
import com.example.recoope_mobile.models.LoginParams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    private final String LOG_TAG = "Login";

    private ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

    private EditText documentLogin;

    private EditText passwordLogin;
    private Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        documentLogin = findViewById(R.id.documentLogin);
        passwordLogin = findViewById(R.id.passwordLogin);

        ImageButton btnLogin = findViewById(R.id.btnLogin);

        Intent intent = getIntent();
        String cnpj = intent.getStringExtra("cnpj");
        String password = intent.getStringExtra("password");
        Log.e(LOG_TAG,"Intent CNPJ: " +  cnpj);
        Log.e(LOG_TAG,"Intent CNPJ: " +  password);

        if(cnpj != null && password != null){
            fillLogin(cnpj, password);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(Login.this, Main.class);
                startActivity(intent1);

                String cnpj = documentLogin.getText().toString().trim();
                String password = passwordLogin.getText().toString().trim();

                boolean invalidFormat = false;

                if (documentLogin.length() != 14) {
                    documentLogin.setError("CNPJ com Formato inválido.");
                    invalidFormat = true;
                }

                if (!(passwordLogin.length() > 6) && !(passwordLogin.length() <= 100)) {
                    passwordLogin.setError("Senha com tamanho inválida.");
                    invalidFormat = true;
                }

                if(!invalidFormat) {
                    authenticationLogin(cnpj, password);
                }
            }
        });
    }

    public void authenticationLogin(String cnpj, String password){
        if (cnpj == null || password == null || cnpj.isEmpty() || password.isEmpty()) {
            Log.e(LOG_TAG, "CNPJ or password not provided.");
            return;
        }

        LoginParams loginParams = new LoginParams(cnpj, password);

        Call<ResponseBody> call = apiService.authenticationCompany(loginParams);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseString = response.body().string();
                    JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
                    String message = jsonResponse.get("message").getAsString();
                    String executedAt = jsonResponse.get("executedAt").getAsString();
                    JsonObject data = jsonResponse.get("data").getAsJsonObject();
                    Gson gson = new Gson();
                    Company company = gson.fromJson(data, Company.class);

                    if (response.isSuccessful()) {
                        Intent mainIntent = new Intent(Login.this, Main.class);
                        startActivity(mainIntent);
                        finish();

                        Log.i(LOG_TAG, "Data: " + company);
                        Log.i(LOG_TAG, "Message: " + message);
                        Log.i(LOG_TAG, "Executed at: " + executedAt);
                    } else {
                        Log.e(LOG_TAG, "Message: " + message);
                        Log.e(LOG_TAG, "Executed at: " + executedAt);
                    }

                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error processing response: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(LOG_TAG, "API call failed: " + t.getMessage());
            }
        });

    }

    public void returnScreen(View view){
        Intent intent = new Intent(Login.this, StartScreen.class);
        startActivity(intent);
        finish();
    }

    public void fillLogin(String cnpj, String password){
        documentLogin.setText(cnpj);
        passwordLogin.setText(password);
    }



}