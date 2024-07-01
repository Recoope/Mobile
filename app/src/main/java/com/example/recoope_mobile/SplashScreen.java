package com.example.recoope_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                proximaTela();}
        }, 5000);

    }

    public void proximaTela(){
        Intent intent = new Intent(SplashScreen.this, TelaEntrada.class);
        startActivity(intent);
        finish();
    }
    
}