package com.example.recoope_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.recoope_mobile.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void returnScreen(View view){
        Intent intent = new Intent(Login.this, StartScreen.class);
        startActivity(intent);
        finish();
    }

}