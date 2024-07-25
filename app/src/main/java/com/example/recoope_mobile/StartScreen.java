package com.example.recoope_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }

    public void nextScreen(View view){
        Intent intent = new Intent(StartScreen.this, Login.class);
        startActivity(intent);
        finish();
    }

}