package com.example.recoope_mobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.response.ApiDataResponse;
import com.example.recoope_mobile.utils.DialogUtils;
import com.example.recoope_mobile.enums.InvalidFormatLogin;
import com.example.recoope_mobile.model.LoginParams;
import com.example.recoope_mobile.model.LoginResponse;
import com.example.recoope_mobile.utils.NotificationHelper;
import com.example.recoope_mobile.utils.Token;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    private final String LOG_TAG = "Login";

    private ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

    private TextInputEditText documentLoginEt;
    private TextInputLayout documentLoginLayout;
    private EditText passwordLoginEt;
    private TextInputLayout passwordLoginLayout;
    private TextView btForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getIntent().addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        documentLoginEt = findViewById(R.id.documentLogin);
        documentLoginLayout = findViewById(R.id.documentLoginLayout);
        passwordLoginEt = findViewById(R.id.passwordLogin);
        passwordLoginLayout = findViewById(R.id.passwordLoginLayout);

        btForgotPassword = findViewById(R.id.btForgotPassword);

        Intent intent = getIntent();
        String cnpj = intent.getStringExtra("cnpj");
        String password = intent.getStringExtra("password");

        documentLoginEt.setText(cnpj);
        passwordLoginEt.setText(password);

        ImageButton btnLogin = findViewById(R.id.btnLogin);

        btForgotPassword.setOnClickListener(r -> {
            String cnpjOrEmail = documentLoginEt.getText().toString().trim();

            if (cnpjOrEmail.isEmpty()) {
                setTextFieldError(documentLoginLayout, "Por favor, insira o CNPJ ou E-mail.");
                return;
            }

            Call<ApiDataResponse> call = apiService.generateCodeForgotPassword(cnpjOrEmail);
            call.enqueue(new Callback<ApiDataResponse>() {
                @Override
                public void onResponse(Call<ApiDataResponse> call, Response<ApiDataResponse> response) {
                    if (response.isSuccessful()) {
                        // Código gerado com sucesso
                        Toast.makeText(Login.this, "Código de recuperação enviado! Verifique seu e-mail.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this, ForgotPassword.class);
                        intent.putExtra("cnpjOrEmail", cnpjOrEmail);
                        startActivity(intent);
                    } else {
                        // Caso de erro ao gerar o código
                        Toast.makeText(Login.this, "Algo deu errado, volte mais tarde!.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiDataResponse> call, Throwable t) {
                    Log.e(LOG_TAG, "Falha na chamada API: " + t.getMessage());
                    Toast.makeText(Login.this, "Algo deu errado, volte mais tarde!.", Toast.LENGTH_LONG).show();
                }
            });
        });

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

                        if (response.code() == 200) {
                            Gson gson = new Gson();
                            LoginResponse loginResponse = gson.fromJson(data, LoginResponse.class);

                            // Capturando o valor do "context"
                            String contextValue = data.has("context") ? data.get("context").getAsString() : "Unknown context";
                            Log.i(LOG_TAG, "Context: " + contextValue);

                            if(contextValue.equals("EMPRESA")){
                                Token.saveToken(Login.this, loginParams.getCnpjOrEmail(), loginResponse.getToken(), loginResponse.getRefreshToken());
                                nextScreenMain();
                            }else{
                                nextScreenWebView();
                            }

                        }

                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error processing response: " + e.getMessage());
                        Toast.makeText(Login.this, "Algo deu errado, volte mais tarde!.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        String responseString = response.errorBody() != null ? response.errorBody().string() : "";
                        Log.d(LOG_TAG, "Error response string: " + responseString);

                        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
                        String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown message";
                        InvalidFormatLogin invalidFormatLogin = verifyReturn(message);
                        if (invalidFormatLogin != null) {
                            matchInvalidFormat(invalidFormatLogin);
                        } else {
                            Toast.makeText(Login.this, "Error.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error processing error response: " + e.getMessage(), e);
                        Toast.makeText(Login.this, "Algo deu errado, volte mais tarde!.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(LOG_TAG, "API call failed: " + t.getMessage());
                Toast.makeText(Login.this, "Algo deu errado, volte mais tarde!.", Toast.LENGTH_LONG).show();
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

    public void nextScreenMain() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
        NotificationHelper.sendNotification(Login.this, "Ufa... Que bom te ver por aqui!", "Entre diariamente, sempre há novidades!");
    }

    public void nextScreenWebView() {
        Intent intent = new Intent(Login.this, WebArea.class);
        startActivity(intent);
        finish();
        NotificationHelper.sendNotification(Login.this, "Ufa... Que bom te ver por aqui!", "Entre diariamente, sempre há novidades!");
    }

}
