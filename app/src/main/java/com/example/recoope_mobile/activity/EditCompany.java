package com.example.recoope_mobile.activity;

import static com.example.recoope_mobile.enums.InvalidFormatUpdate.COMPANY_NOT_FOUND;
import static com.example.recoope_mobile.enums.InvalidFormatUpdate.COMPANY_UPDATED;
import static com.example.recoope_mobile.enums.InvalidFormatUpdate.EMAIL_ALREADY_EXISTS;
import static com.example.recoope_mobile.enums.InvalidFormatUpdate.INVALID_NAME;
import static com.example.recoope_mobile.enums.InvalidFormatUpdate.INVALID_PHONE;
import static com.example.recoope_mobile.enums.InvalidFormatUpdate.PHONE_ALREADY_EXISTS;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.dto.CompanyDto;
import com.example.recoope_mobile.enums.InvalidFormatRegister;
import com.example.recoope_mobile.enums.InvalidFormatUpdate;
import com.example.recoope_mobile.model.Auction;
import com.example.recoope_mobile.response.ApiDataResponse;
import com.example.recoope_mobile.utils.DialogUtils;
import com.example.recoope_mobile.utils.NotificationHelper;
import com.example.recoope_mobile.utils.Token;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCompany extends AppCompatActivity {

    private TextInputEditText companyName, companyEmail, companyPhone, companyPassword, companyPasswordConfirmation;
    private ImageView btnConfirmUpdateProfile, btnUpdatePassword;
    private Button btnDeleteAccount;
    private TextInputLayout companyNameLayoutAtt, companyEmailLayoutAtt, companyPhoneLayoutAtt,companyPasswordLayoutAtt, companyPasswordConfirmationLayoutAtt;
    private ApiService apiService;
    private String cnpj;
    private String refreshToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company);

        SharedPreferences sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);
        cnpj = sharedPreferences.getString("cnpj", "");
        refreshToken = sharedPreferences.getString("refreshToken", "");

        // Inicialização dos campos
        companyName = findViewById(R.id.companyNameAtt);
        companyNameLayoutAtt = findViewById(R.id.companyNameLayoutAtt);
        companyEmail = findViewById(R.id.companyEmailAtt);
        companyEmailLayoutAtt = findViewById(R.id.companyEmailLayoutAtt);
        companyPhone = findViewById(R.id.companyPhoneAtt);
        companyPhoneLayoutAtt = findViewById(R.id.companyPhoneLayoutAtt);
        companyPassword = findViewById(R.id.companyPasswordAtt);
        companyPasswordLayoutAtt = findViewById(R.id.companyPasswordLayoutAtt);
        companyPasswordConfirmation = findViewById(R.id.companyPasswordConfirmationAtt);
        companyPasswordConfirmationLayoutAtt = findViewById(R.id.companyPasswordConfirmationLayoutAtt);
        btnConfirmUpdateProfile = findViewById(R.id.btnConfirmUpdateProfile);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        addTextWatchers();

        Bundle extras = getIntent().getExtras();
        companyName.setText(extras.getString("COMPANY_NAME"));
        companyEmail.setText(extras.getString("COMPANY_EMAIL"));
        companyPhone.setText(extras.getString("COMPANY_PHONE"));

        btnConfirmUpdateProfile.setOnClickListener(r ->{
            updateCompanyData();
        });

        btnUpdatePassword.setOnClickListener(r -> {
            updateCompanyPassword();
        });

        btnDeleteAccount.setOnClickListener(r -> {
            Dialog dialog = new Dialog(this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.item_cancel_popup);

            dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sp = getSharedPreferences("auth", Context.MODE_PRIVATE);
                    String cnpj = sp.getString("cnpj", "");


                    Call call = apiService.deleteCompany(cnpj);
                    call.enqueue(new Callback<ApiDataResponse<Auction>>() {
                        @Override
                        public void onResponse(Call<ApiDataResponse<Auction>> call, Response<ApiDataResponse<Auction>> response) {
                            if (response.code() == 200) {

                            } else {
                                Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiDataResponse<Auction>> call, Throwable t) {
                            Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

            dialog.findViewById(R.id.x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        });

        // Inicialização do ApiService
        apiService = RetrofitClient.getClient(this).create(ApiService.class);
    }

    public void updateCompanyData() {
        String name = companyName.getText().toString().trim();
        String email = companyEmail.getText().toString().trim();
        String phone = companyPhone.getText().toString().trim();

        CompanyDto request = new CompanyDto(name, email, phone);

        if (name.isEmpty() && email.isEmpty() && phone.isEmpty()) {
            setTextFieldError(companyNameLayoutAtt, "Nome vazio.");
            setTextFieldError(companyEmailLayoutAtt, "E-mail vazio.");
            setTextFieldError(companyPhoneLayoutAtt, "Número vazio.");
            DialogUtils.showCustomDialog("Preencha os campos", EditCompany.this);
            return;
        }

        apiService.updateCompany(cnpj, request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();

                        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
                        String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown message";
                        JsonObject data = jsonResponse.has("data") ? jsonResponse.get("data").getAsJsonObject() : new JsonObject();

                        if (response.code() == 200 ) {
                            Log.e("EditCompany", "Atualizado com sucesso");
                            apiService.refreshToken(cnpj, refreshToken).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        if(response.code() == 200){
                                            try {
                                                String responseString2 = response.body().string();

                                                JsonObject jsonResponse2 = JsonParser.parseString(responseString2).getAsJsonObject();
                                                JsonObject data2 = jsonResponse2.has("data") ? jsonResponse2.get("data").getAsJsonObject() : null;
                                                String newToken = data2.has("token") ? data2.get("token").getAsString() : "";


                                                Token.refreshToken(EditCompany.this, newToken);
                                                Log.e("TokenRefresh", "Atualizou token");

                                                Toast.makeText(EditCompany.this, "Dados alterados!", Toast.LENGTH_LONG).show();
                                                finish();

                                            } catch (IOException ioe) {
                                                Log.e("EditCompany", "Error processing response: " + ioe.getMessage(), ioe);
                                                Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_LONG).show();
                                            }

                                        } else if (response.code() == 400) {
                                            Log.e("TokenRefresh", "Deu erro");
                                            Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Log.e("TokenRefresh", "Erro interno");
                                        Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {

                            InvalidFormatUpdate invalidFormatEditCompany = verifyReturn(message);
                            if (invalidFormatEditCompany != null) {
                                matchInvalidFormat(invalidFormatEditCompany);
                            } else {
                                Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IOException e) {
                        Log.e("EditCompany", "Error processing response: " + e.getMessage(), e);
                        Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String responseString = response.errorBody() != null ? response.errorBody().string() : "";
                        Log.d("EditCompany", "Error response string: " + responseString);

                        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
                        String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown message";
                        InvalidFormatUpdate invalidFormatEditCompany = verifyReturn(message);
                        if (invalidFormatEditCompany != null) {
                            matchInvalidFormat(invalidFormatEditCompany);
                        } else {
                            Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e("EditCompany", "Error processing error response: " + e.getMessage(), e);
                        Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateCompanyPassword() {
        String password = companyPassword.getText().toString().trim();
        String confirmPassword = companyPasswordConfirmation.getText().toString().trim();

        Log.e("senha", "senha: " + password);

        if (password.isEmpty() && confirmPassword.isEmpty()) {
            setTextFieldError(companyPasswordLayoutAtt, "Senha vazia.");
            setTextFieldError(companyPasswordConfirmationLayoutAtt, "Senha vazia.");
            DialogUtils.showCustomDialog("Verifique se sua senha não está vazia.", EditCompany.this);
            return;
        }else if (confirmPassword.isEmpty()){
            setTextFieldError(companyPasswordConfirmationLayoutAtt, "Senha vazia.");
            DialogUtils.showCustomDialog("Verifique se sua senha não está vazia.", EditCompany.this);
            return;
        } else if (password.isEmpty()) {
            setTextFieldError(companyPasswordLayoutAtt, "Senha vazia.");
            DialogUtils.showCustomDialog("Verifique se sua senha não está vazia.", EditCompany.this);
            return;
        }

        if (!password.equals(confirmPassword)) {
            setTextFieldError(companyPasswordConfirmationLayoutAtt, "Senha não correspondida.");
            DialogUtils.showCustomDialog("Sua senha de confirmação não está correta.", EditCompany.this);
            return;
        }

        apiService.updateCompanyPassword(cnpj, password).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();

                        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
                        String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown message";
                        JsonObject data = jsonResponse.has("data") ? jsonResponse.get("data").getAsJsonObject() : new JsonObject();

                        if (response.code() == 200 ) {
                            Toast.makeText(EditCompany.this, "Senha atualizada!", Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Log.e("EditCompany", "Error processing response: " + e.getMessage(), e);
                        Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String responseString = response.errorBody() != null ? response.errorBody().string() : "";
                        Log.d("EditCompany", "Error response string: " + responseString);

                        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
                        String message = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown message";
                        InvalidFormatUpdate invalidFormatEditCompany = verifyReturn(message);
                        if (invalidFormatEditCompany != null) {
                            matchInvalidFormat(invalidFormatEditCompany);
                        } else {
                            Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.e("EditCompany", "Error processing error response: " + e.getMessage(), e);
                        Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditCompany.this, "Algo deu errado, volte mais tarde!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private InvalidFormatUpdate verifyReturn(String message) {
        try {
            return InvalidFormatUpdate.fromType(message);
        } catch (IllegalArgumentException e) {
            Log.e("EditCompany", "Invalid format: " + message, e);
            return null;
        }
    }

    private void matchInvalidFormat(InvalidFormatUpdate invalidFormat) {
        switch (invalidFormat) {
            case COMPANY_NOT_FOUND:
                DialogUtils.showCustomDialog(invalidFormat, EditCompany.this);
                break;
            case INVALID_NAME:
                setTextFieldError(companyNameLayoutAtt, "Tamanho inválido.");
                DialogUtils.showCustomDialog(invalidFormat, EditCompany.this);
                break;
            case EMAIL_ALREADY_EXISTS:
                setTextFieldError(companyEmailLayoutAtt, "Email já existente.");
                DialogUtils.showCustomDialog(invalidFormat, EditCompany.this);
                break;
            case INVALID_EMAIL:
                setTextFieldError(companyEmailLayoutAtt, "Email inválido.");
                Toast.makeText(EditCompany.this, "Formato de email inválido.", Toast.LENGTH_SHORT).show();
                DialogUtils.showCustomDialog(invalidFormat, EditCompany.this);
                break;
            case PHONE_ALREADY_EXISTS:
                setTextFieldError(companyPhoneLayoutAtt, "Telefone já existente.");
                DialogUtils.showCustomDialog(invalidFormat, EditCompany.this);
                break;
            case INVALID_PHONE:
                setTextFieldError(companyPhoneLayoutAtt, "Telefone inválido.");
                Toast.makeText(EditCompany.this, "Formato de telefone inválido.", Toast.LENGTH_SHORT).show();
                DialogUtils.showCustomDialog(invalidFormat, EditCompany.this);
                break;
            case COMPANY_UPDATED:
                Toast.makeText(EditCompany.this, "Empresa atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setTextFieldError(TextInputLayout layout, String errorMessage) {
        layout.setError(errorMessage);
        layout.setHintTextColor(ColorStateList.valueOf(Color.RED));
    }

    private void resetTextFieldStyles() {
        resetTextFieldStyle(companyNameLayoutAtt);
        resetTextFieldStyle(companyEmailLayoutAtt);
        resetTextFieldStyle(companyPhoneLayoutAtt);
        resetTextFieldStyle(companyPasswordConfirmationLayoutAtt);
        resetTextFieldStyle(companyPasswordLayoutAtt);
    }

    private void resetTextFieldStyle(TextInputLayout layout) {
        layout.setError(null);
        layout.setHintTextColor(ColorStateList.valueOf(Color.GRAY));
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

        companyName.addTextChangedListener(textWatcher);
        companyEmail.addTextChangedListener(textWatcher);
        companyPassword.addTextChangedListener(textWatcher);
        companyPhone.addTextChangedListener(textWatcher);
        companyPasswordConfirmation.addTextChangedListener(textWatcher);
    }

    public void returnScreen(View view) {
        finish();
    }
}
