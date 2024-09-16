package com.example.recoope_mobile.activities;

import static com.example.recoope_mobile.enums.InvalidFormatLogin.EMAIL_CNPJ_INVALID;
import static com.example.recoope_mobile.enums.InvalidFormatLogin.NO_MATCHING_CNPJ_OR_INCORRECT_PASSWORD;
import static com.example.recoope_mobile.enums.InvalidFormatLogin.NO_MATCHING_EMAIL_OR_INCORRECT_PASSWORD;
import static com.example.recoope_mobile.enums.InvalidFormatRegister.EXISTING_EMAIL;
import static com.example.recoope_mobile.enums.InvalidFormatRegister.EXISTING_PHONE_NUMBER;
import static com.example.recoope_mobile.enums.InvalidFormatRegister.INVALID_EMAIL;
import static com.example.recoope_mobile.enums.InvalidFormatRegister.INVALID_PASSWORD;
import static com.example.recoope_mobile.enums.InvalidFormatRegister.INVALID_PHONE_NUMBER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.dialogs.DialogUtils;
import com.example.recoope_mobile.enums.InvalidFormatLogin;
import com.example.recoope_mobile.enums.InvalidFormatRegister;
import com.example.recoope_mobile.models.Company;
import com.example.recoope_mobile.models.LoginParams;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

    private TextInputEditText documentLoginEt;
    private TextInputLayout documentLoginLayout;
    private EditText passwordLoginEt;
    private TextInputLayout passwordLoginLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        documentLoginEt = findViewById(R.id.documentLogin);
        documentLoginLayout = findViewById(R.id.documentLoginLayout);
        passwordLoginEt = findViewById(R.id.passwordLogin);
        passwordLoginLayout = findViewById(R.id.passwordLoginLayout);

        ImageButton btnLogin = findViewById(R.id.btnLogin);

        addTextWatchers();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cnpj = documentLoginEt.getText().toString().trim();
                String password = passwordLoginEt.getText().toString().trim();

                authenticationLogin(cnpj, password);

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
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        Log.d(LOG_TAG, "Response string: " + responseString);

                        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
                        String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown message";
                        JsonObject data = jsonResponse.has("data") ? jsonResponse.get("data").getAsJsonObject() : new JsonObject();

                        Log.i(LOG_TAG, "Message: " + message);
                        Log.i(LOG_TAG, "Data: " + data);

                        if ("Login realizado com sucesso!!".equals(message)) {
                            Gson gson = new Gson();
                            Company company = gson.fromJson(data, Company.class);
                            Log.i(LOG_TAG, "Pronto para ir para o feed!!!!!");
                        } else {
                            InvalidFormatLogin invalidFormatLogin = verifyReturn(message);
                            if (invalidFormatLogin != null) {
                                matchInvalidFormat(invalidFormatLogin);
                            } else {
                                Toast.makeText(Login.this, "Erro desconhecido.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error processing response: " + e.getMessage());
                    }
                } else {
                    try {
                        String responseString = response.body().string();
                        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
                        String message = jsonResponse.get("message").getAsString();
                        String executedAt = jsonResponse.get("executedAt").getAsString();
                        JsonObject data = jsonResponse.get("data").getAsJsonObject();
                        Gson gson = new Gson();
                        Company company = gson.fromJson(data, Company.class);
                        Log.e(LOG_TAG, "Data: " + company);
                        Log.e(LOG_TAG, "Message: " + message);
                        Log.e(LOG_TAG, "Executed at: " + executedAt);

                    } catch (Exception e) {
                    Log.e(LOG_TAG, "Error processing response: " + e.getMessage());
                }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(LOG_TAG, "API call failed: " + t.getMessage());
            }
        });

    }

    private void resetTextFieldStyle(TextInputLayout layout) {
        layout.setError(null);
        layout.setHintTextColor(ColorStateList.valueOf(Color.GRAY));
    }

    private void setTextFieldError(TextInputLayout layout, String errorMessage) {
        layout.setError(errorMessage);
        layout.setHintTextColor(ColorStateList.valueOf(Color.RED));
    }

    private void resetTextFieldStyles() {
        resetTextFieldStyle(documentLoginLayout);
        resetTextFieldStyle(passwordLoginLayout);
    }

    private void addTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                resetTextFieldStyles();
            }
        };

        documentLoginEt.addTextChangedListener(textWatcher);
        passwordLoginEt.addTextChangedListener(textWatcher);
    }

    public void returnScreen(View view){
        Intent intent = new Intent(Login.this, StartScreen.class);
        startActivity(intent);
        finish();
    }

    private InvalidFormatLogin verifyReturn(String message) {
        try {
            return InvalidFormatLogin.fromType(message);
        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, "Invalid format: " + message, e);
            return null;
        }
    }

    private void matchInvalidFormat(InvalidFormatLogin invalidFormat) {
        switch (invalidFormat) {
            case NO_MATCHING_CNPJ_OR_INCORRECT_PASSWORD:
                setTextFieldError(documentLoginLayout, "CNPJ ou senha incorretos");
                setTextFieldError(passwordLoginLayout, "CNPJ ou senha incorretos");
                DialogUtils.showCustomDialog(invalidFormat, Login.this);
                break;
            case NO_MATCHING_EMAIL_OR_INCORRECT_PASSWORD:
                setTextFieldError(documentLoginLayout, "E-mail ou senha incorretos");
                setTextFieldError(passwordLoginLayout, "E-mail ou senha incorretos");
                DialogUtils.showCustomDialog(invalidFormat, Login.this);
                break;
            case EMAIL_CNPJ_INVALID:
                setTextFieldError(documentLoginLayout, "E-mail ou CNPJ incorretos");
                setTextFieldError(documentLoginLayout, "E-mail ou CNPJ incorretos");
                DialogUtils.showCustomDialog(invalidFormat, Login.this);
                break;
        }
    }
}




