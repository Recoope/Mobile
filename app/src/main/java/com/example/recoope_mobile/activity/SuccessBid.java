package com.example.recoope_mobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.utils.PtBrUtils;

public class SuccessBid extends AppCompatActivity {

    private TextView auctionIdView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_bid);

        auctionIdView = findViewById(R.id.auctionIdView);

        int auctionId = getIntent().getExtras().getInt("AUCTION_ID");
        auctionIdView.setText("Leilão " + PtBrUtils.formatId(auctionId));

        Handler handle = new Handler();
        Runnable finish = (Runnable) this::finish;

        handle.postDelayed(finish, 5000);
        findViewById(R.id.successLayout).setOnClickListener((v) -> {
            handle.removeCallbacks(finish);
            finish();
        });

    }
}