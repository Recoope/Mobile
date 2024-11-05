package com.example.recoope_mobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.recoope_mobile.R;

public class WebArea extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBarWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_area);

        webView = findViewById(R.id.webView);
        progressBarWeb = findViewById(R.id.progressBarWeb);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBarWeb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBarWeb.setVisibility(View.INVISIBLE);
            }
        });


        openWebView();

    }

    int contVoltar;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        contVoltar++;
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            contVoltar=0;
            return true;
        } else if (webView.canGoBack() == false && contVoltar == 1) {
            Toast.makeText(this, "Clique novamente para sair", Toast.LENGTH_SHORT).show();
        }  else if (webView.canGoBack() == false && contVoltar == 2) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public void openWebView(){
        webView.loadUrl("http://3.209.22.165/home.html");
    }


}