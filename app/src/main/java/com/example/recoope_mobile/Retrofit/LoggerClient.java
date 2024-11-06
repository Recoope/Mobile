package com.example.recoope_mobile.Retrofit;


import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class LoggerClient {
    private static final String BASE_URL = "http://3.209.22.165:8081/";
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
            .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();
    private static IService apiService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(IService.class);

    private static class LogModel {
        public String companyId;
        public String action;
    }

    private interface IService {
        @POST("/api/log")
        Call<Void> postLog(@Body LogModel log);
    }

    public static void postLog(Context context, String action) {
        LogModel log = new LogModel();

        log.companyId = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("cnpj", "");
        log.action = action;

        Call call = apiService.postLog(log);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.i("LOG", "Ação " + log.action + " trackeada com sucesso!");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("LOG", "Falha ao trackear ação: " + t.getMessage());
            }
        });

    }
}

