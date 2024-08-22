package com.example.recoope_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        Intent intent = new Intent(SplashScreen.this, StartScreen.class);
        startActivity(intent);
        finish();
    }
    
}