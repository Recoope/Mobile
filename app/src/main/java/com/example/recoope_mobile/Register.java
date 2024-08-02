package com.example.recoope_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.time.Instant;
import java.util.Date;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void returnScreen(View view){
        Intent intent = new Intent(Register.this, StartScreen.class);
        startActivity(intent);
        finish();
    }

    public void register(View view){
        EditText companyName = findViewById(R.id.companyName);
        EditText companyCNPJ = findViewById(R.id.companyDocument);
        EditText companyEmail = findViewById(R.id.companyEmail);
        EditText companyPhone = findViewById(R.id.companyPhone);
        EditText companyPassword = findViewById(R.id.companyPassword);

        Database db = new Database();

        db.saveCompany(new Company(companyCNPJ.getText().toString(), companyName.getText().toString(), companyEmail.getText().toString(), companyPassword.getText().toString(), companyPhone.getText().toString()));
    }

}