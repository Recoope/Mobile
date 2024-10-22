package com.example.recoope_mobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.recoope_mobile.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE);
                String token = sharedPreferences.getString("token", null);
                String cnpj = sharedPreferences.getString("token", null);

                if (token == null && cnpj == null) loginScreen();
                else startScreen();
            }
        }, 5000);

    }

    public void loginScreen(){
        Intent intent = new Intent(SplashScreen.this, StartScreen.class);
        startActivity(intent);
        finish();
    }

    public void startScreen(){
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}