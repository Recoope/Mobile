package com.example.recoope_mobile.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class Token {

    public static void saveToken(Context context, String cnpj, String token, String refreshToken) {
        SharedPreferences preferences = context.getSharedPreferences("auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cnpj", cnpj);
        editor.putString("token", token);
        editor.putString("refreshToken", refreshToken);
        editor.apply();
    }


}
