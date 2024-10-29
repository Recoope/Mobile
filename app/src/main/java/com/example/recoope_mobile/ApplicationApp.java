package com.example.recoope_mobile;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class ApplicationApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Força o tema claro em todo o aplicativo
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

}
