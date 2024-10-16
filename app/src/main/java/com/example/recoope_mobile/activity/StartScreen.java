package com.example.recoope_mobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.recoope_mobile.R;

import java.util.TimeZone;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        setContentView(R.layout.activity_start_screen);
    }

    public void nextScreenLogin(View view){
        Intent intent = new Intent(StartScreen.this, Login.class);
        startActivity(intent);
        finish();
    }

    public void nextScreenRegister(View view){
        Intent intent = new Intent(StartScreen.this, Register.class);
        startActivity(intent);
        finish();
    }

}