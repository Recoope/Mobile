package com.example.recoope_mobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.ApiService;
import com.example.recoope_mobile.Retrofit.RetrofitClient;
import com.example.recoope_mobile.response.ApiDataResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    private static final String LOG_TAG = "ForgotPassword";

    private TextView numberCode;
    private ImageButton btVerifyNumberCode;
    private ApiService apiService;
    private String cnpjOrEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        apiService = RetrofitClient.getClient(this).create(ApiService.class);

        numberCode = findViewById(R.id.numberCode);
        btVerifyNumberCode = findViewById(R.id.btVerifyNumberCode);

        // Extrai o CNPJ ou e-mail do Bundle
        cnpjOrEmail = getIntent().getStringExtra("cnpjOrEmail");

        btVerifyNumberCode.setOnClickListener(r -> {
            String code = numberCode.getText().toString().trim();
            if (code.isEmpty()) {
                Toast.makeText(ForgotPassword.this, "Por favor, insira o código recebido.", Toast.LENGTH_SHORT).show();
                return;
            }
            validateCode(code);
        });
    }

    private void validateCode(String code) {
        // Exemplo de chamada para validar o código com a API
        Call<ApiDataResponse> call = apiService.validatedCodeForgotPassword(cnpjOrEmail, code);
        call.enqueue(new Callback<ApiDataResponse>() {
            @Override
            public void onResponse(Call<ApiDataResponse> call, Response<ApiDataResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Código verificado com sucesso!", Toast.LENGTH_LONG).show();
                    // Prossiga para a próxima etapa, como redefinir senha
                } else {
                    Toast.makeText(ForgotPassword.this, "Código inválido ou expirado. Tente novamente.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiDataResponse> call, Throwable t) {
                Log.e(LOG_TAG, "Erro ao validar o código: " + t.getMessage());
                Toast.makeText(ForgotPassword.this, "Algo deu errado, tente mais tarde!.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
