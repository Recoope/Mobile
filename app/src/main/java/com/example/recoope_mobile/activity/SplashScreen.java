package com.example.recoope_mobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

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
                nextScreen();}
        }, 5000);

    }

    public void nextScreen(){
        SharedPreferences sp = getSharedPreferences("auth", MODE_PRIVATE);
        Intent intent;
        if(sp.getString("cnpj", null) != null){
            intent = new Intent(SplashScreen.this, MainActivity.class);
        }else {
            intent = new Intent(SplashScreen.this, StartScreen.class);
        }
        startActivity(intent);
        finish();
    }
    
}