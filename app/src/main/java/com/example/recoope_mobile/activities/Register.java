package com.example.recoope_mobile.activities;

import static com.example.recoope_mobile.enums.InvalidFormatRegister.*;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.dialogs.DialogUtils;
import com.example.recoope_mobile.enums.InvalidFormatRegister;
import com.example.recoope_mobile.models.Company;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Register extends AppCompatActivity {

    private static final String LOG_TAG = "Register";

    private ApiService apiService;
    private TextInputLayout companyNameLayout;
    private TextInputEditText companyNameEt;
    private TextInputLayout companyCNPJLayout;
    private TextInputEditText companyCNPJEt;
    private TextInputLayout companyEmailLayout;
    private TextInputEditText companyEmailEt;
    private TextInputLayout companyPhoneLayout;
    private TextInputEditText companyPhoneEt;
    private TextInputLayout companyPasswordLayout;
    private TextInputEditText companyPasswordEt;
    private TextInputLayout companyConfirmationPasswordLayout;
    private TextInputEditText companyConfirmationPasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        companyNameLayout = findViewById(R.id.companyNameLayout);
        companyNameEt = findViewById(R.id.companyName);
        companyCNPJLayout = findViewById(R.id.companyDocumentLayout);
        companyCNPJEt = findViewById(R.id.companyDocument);
        companyEmailLayout = findViewById(R.id.companyEmailLayout);
        companyEmailEt = findViewById(R.id.companyEmail);
        companyPhoneLayout = findViewById(R.id.companyPhoneLayout);
        companyPhoneEt = findViewById(R.id.companyPhone);
        companyPasswordLayout = findViewById(R.id.companyPasswordLayout);
        companyPasswordEt = findViewById(R.id.companyPassword);
        companyConfirmationPasswordLayout = findViewById(R.id.companyPasswordConfirmationLayout);
        companyConfirmationPasswordEt = findViewById(R.id.companyPasswordConfirmation);

        addTextWatchers();
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

        companyNameEt.addTextChangedListener(textWatcher);
        companyCNPJEt.addTextChangedListener(textWatcher);
        companyEmailEt.addTextChangedListener(textWatcher);
        companyPhoneEt.addTextChangedListener(textWatcher);
        companyPasswordEt.addTextChangedListener(textWatcher);
        companyConfirmationPasswordEt.addTextChangedListener(textWatcher);
    }

    private void resetTextFieldStyles() {
        resetTextFieldStyle(companyNameLayout);
        resetTextFieldStyle(companyCNPJLayout);
        resetTextFieldStyle(companyEmailLayout);
        resetTextFieldStyle(companyPhoneLayout);
        resetTextFieldStyle(companyPasswordLayout);
        resetTextFieldStyle(companyConfirmationPasswordLayout);
    }

    private void resetTextFieldStyle(TextInputLayout layout) {
        layout.setError(null);
        layout.setHintTextColor(ColorStateList.valueOf(Color.GRAY));
    }

    private void setTextFieldError(TextInputLayout layout, String errorMessage) {
        layout.setError(errorMessage);
        layout.setHintTextColor(ColorStateList.valueOf(Color.RED));
    }

    public void returnScreen(View view) {
        Intent intent = new Intent(Register.this, StartScreen.class);
        startActivity(intent);
        finish();
    }

    public void nextScreen() {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void fillLogin(Company company) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private InvalidFormatRegister verifyReturn(String message) {
        try {
            return InvalidFormatRegister.fromType(message);
        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, "Invalid format: " + message, e);
            return null;
        }
    }

    private void matchInvalidFormat(InvalidFormatRegister invalidFormat) {
        switch (invalidFormat) {
            case NULL_PARAMETERS:
                setTextFieldError(companyNameLayout, "Campo obrigatório");
                setTextFieldError(companyCNPJLayout, "Campo obrigatório");
                setTextFieldError(companyEmailLayout, "Campo obrigatório");
                setTextFieldError(companyPhoneLayout, "Campo obrigatório");
                setTextFieldError(companyPasswordLayout, "Campo obrigatório");
                setTextFieldError(companyConfirmationPasswordLayout, "Campo obrigatório");
                DialogUtils.showCustomDialog(invalidFormat, Register.this);
                break;
            case PASSWORD_DOES_NOT_MATCH:
                setTextFieldError(companyPasswordLayout, "Senhas não coincidem");
                setTextFieldError(companyConfirmationPasswordLayout, "Senhas não coincidem");
                DialogUtils.showCustomDialog(invalidFormat, Register.this);
                break;
            case INVALID_CNPJ:
                setTextFieldError(companyCNPJLayout, "CNPJ inválido");
                DialogUtils.showCustomDialog(invalidFormat, Register.this);
                break;
            case EXISTING_CNPJ:
                setTextFieldError(companyCNPJLayout, "CNPJ já existente");
                DialogUtils.showCustomDialog(invalidFormat, Register.this);
                break;
            case INVALID_COMPANY_NAME:
                setTextFieldError(companyNameLayout, "Nome da empresa inválido");
                DialogUtils.showCustomDialog(invalidFormat, Register.this);
                break;
            case EXISTING_EMAIL:
                DialogUtils.showCustomDialog(invalidFormat, Register.this);
                break;
            case INVALID_EMAIL:
                setTextFieldError(companyEmailLayout, "Email inválido");
                Toast.makeText(Register.this, EXISTING_EMAIL.getType() + "/" + INVALID_EMAIL.getType(), Toast.LENGTH_SHORT).show();
                DialogUtils.showCustomDialog(invalidFormat, Register.this);
                break;
            case EXISTING_PHONE_NUMBER:
                DialogUtils.showCustomDialog(invalidFormat, Register.this);
                break;
            case INVALID_PHONE_NUMBER:
                setTextFieldError(companyPhoneLayout, "Número de telefone inválido");
                Toast.makeText(Register.this, EXISTING_PHONE_NUMBER.getType() + "/" + INVALID_PHONE_NUMBER.getType(), Toast.LENGTH_SHORT).show();
                DialogUtils.showCustomDialog(invalidFormat, Register.this);
                break;
            case INVALID_PASSWORD:
                setTextFieldError(companyPasswordLayout, "Senha inválida");
                setTextFieldError(companyConfirmationPasswordLayout, "Senha inválida");
                Toast.makeText(Register.this, INVALID_PASSWORD.getType(), Toast.LENGTH_SHORT).show();
                DialogUtils.showCustomDialog(invalidFormat, Register.this);
                break;
        }
    }

    public void register(View view) {
        String companyName = companyNameEt.getText().toString().trim();
        String companyCNPJ = companyCNPJEt.getText().toString().trim();
        String companyEmail = companyEmailEt.getText().toString().trim();
        String companyPhone = companyPhoneEt.getText().toString().trim();
        String companyPassword = companyPasswordEt.getText().toString().trim();
        String companyPasswordConfirmation = companyConfirmationPasswordEt.getText().toString().trim();

        Company company = new Company(companyCNPJ, companyName, companyEmail, companyPassword, companyPhone, companyPasswordConfirmation);

        Log.e(LOG_TAG, company.toString());

        Call<ResponseBody> call = apiService.createCompany(company);

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

                        if ("Empresa cadastrada com sucesso!".equals(message)) {
                            Gson gson = new Gson();
                            Company company = gson.fromJson(data, Company.class);
                            fillLogin(company);
                            nextScreen();
                        } else {
                            InvalidFormatRegister invalidFormatRegister = verifyReturn(message);
                            if (invalidFormatRegister != null) {
                                matchInvalidFormat(invalidFormatRegister);
                            } else {
                                Toast.makeText(Register.this, "Erro desconhecido.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error processing response: " + e.getMessage(), e);
                        Toast.makeText(Register.this, "Error processing response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        String responseString = response.errorBody() != null ? response.errorBody().string() : "";
                        Log.d(LOG_TAG, "Error response string: " + responseString);

                        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
                        String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown message";
                        InvalidFormatRegister invalidFormatRegister = verifyReturn(message);
                        if (invalidFormatRegister != null) {
                            matchInvalidFormat(invalidFormatRegister);
                        } else {
                            Toast.makeText(Register.this, "Erro desconhecido.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error processing error response: " + e.getMessage(), e);
                        Toast.makeText(Register.this, "Error processing error response", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(LOG_TAG, "API call failed: " + t.getMessage(), t);
                Toast.makeText(Register.this, "Falha na chamada da API", Toast.LENGTH_LONG).show();
            }
        });
    }
}
