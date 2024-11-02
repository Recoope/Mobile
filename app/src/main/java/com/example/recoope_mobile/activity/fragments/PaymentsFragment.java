package com.example.recoope_mobile.activity.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.Retrofit.LoggerClient;

public class PaymentsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LoggerClient.postLog(getContext(), "PAYMENTS");
        return inflater.inflate(R.layout.payments, container, false);
    }
}