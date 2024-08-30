package com.example.recoope_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.enums.InvalidFormatRegister;
import com.example.recoope_mobile.models.Company;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private static final String LOG_TAG = "Register";

    private ApiService apiService;
    private EditText companyNameEt;
    private EditText companyCNPJEt;
    private EditText companyEmailEt;
    private EditText companyPhoneEt;
    private EditText companyPasswordEt;
    private EditText companyConfirmationPasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        companyNameEt = findViewById(R.id.companyName);
        companyCNPJEt = findViewById(R.id.companyDocument);
        companyEmailEt = findViewById(R.id.companyEmail);
        companyPhoneEt = findViewById(R.id.companyPhone);
        companyPasswordEt = findViewById(R.id.companyPassword);
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
        companyNameEt.setTextColor(Color.BLACK);
        companyCNPJEt.setTextColor(Color.BLACK);
        companyEmailEt.setTextColor(Color.BLACK);
        companyPhoneEt.setTextColor(Color.BLACK);
        companyPasswordEt.setTextColor(Color.BLACK);
        companyConfirmationPasswordEt.setTextColor(Color.BLACK);

        companyNameEt.setHintTextColor(Color.GRAY);
        companyCNPJEt.setHintTextColor(Color.GRAY);
        companyEmailEt.setHintTextColor(Color.GRAY);
        companyPhoneEt.setHintTextColor(Color.GRAY);
        companyPasswordEt.setHintTextColor(Color.GRAY);
        companyConfirmationPasswordEt.setHintTextColor(Color.GRAY);
    }

    private void setTextFieldError(EditText editText, String hint) {
        editText.setTextColor(Color.RED);
        editText.setHintTextColor(Color.RED);
        editText.setHint(hint);
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
        intent.putExtra("cnpj", company.getCnpj());
        intent.putExtra("password", company.getPassword());
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
            case NAME_MIN:
            case NAME_MAX:
                setTextFieldError(companyNameEt, invalidFormat.getType());
                break;
            case CNPJ:
                setTextFieldError(companyCNPJEt, invalidFormat.getType());
                break;
            case EMAIL:
                setTextFieldError(companyEmailEt, invalidFormat.getType());
                break;
            case PHONE:
                setTextFieldError(companyPhoneEt, invalidFormat.getType());
                break;
            case PASSWORD:
                setTextFieldError(companyPasswordEt, invalidFormat.getType());
                break;
            default:
                break;
        }
    }

    public void register(View view) {
        String companyName = companyNameEt.getText().toString();
        String companyCNPJ = companyCNPJEt.getText().toString();
        String companyEmail = companyEmailEt.getText().toString();
        String companyPhone = companyPhoneEt.getText().toString();
        String companyPassword = companyPasswordEt.getText().toString();
        String companyPasswordConfirmation = companyConfirmationPasswordEt.getText().toString();

        if (!companyPassword.equals(companyPasswordConfirmation)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        Company company = new Company(companyCNPJ, companyName, companyEmail, companyPassword, companyPhone, companyPasswordConfirmation);

        Call<ResponseBody> call = apiService.createCompany(company);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
                        String message = jsonResponse.get("message").getAsString();
                        InvalidFormatRegister invalidFormat = verifyReturn(message);

                        if (invalidFormat != null) {
                            matchInvalidFormat(invalidFormat);
                        } else {
                            fillLogin(company);
                            nextScreen();
                        }

                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Error processing response: " + e.getMessage(), e);
                        Toast.makeText(Register.this, "Error processing response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(LOG_TAG, "Error creating company: " + t.getMessage(), t);
                Toast.makeText(Register.this, "Error creating company", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleErrorResponse(Response<ResponseBody> response) {
        try {
            String responseString = response.errorBody().string();
            JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
            String message = jsonResponse.get("message").getAsString();
            Log.e(LOG_TAG, "Error response: " + message);
            Toast.makeText(Register.this, "Error: " + message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error processing error response: " + e.getMessage(), e);
            Toast.makeText(Register.this, "Error processing error response", Toast.LENGTH_LONG).show();
        }
    }
}
