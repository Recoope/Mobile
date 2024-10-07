package com.example.recoope_mobile.Retrofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.recoope_mobile.activity.Login;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;

public class RetrofitClient {
        private static final String BASE_URL = "http://10.0.2.2:8080/";

//    private static final String BASE_URL = "ec2-44-194-250-226.compute-1.amazonaws.com/";

    //    private static final String BASE_URL = "https://recoopeapi.onrender.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
                    .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(chain -> {
                        SharedPreferences preferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
                        String token = preferences.getString("token", "");
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", token);
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    })
                    .addInterceptor(chain -> {
                        SharedPreferences preferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
                        String token = preferences.getString("token", null);
                        Response response = chain.proceed(chain.request());

                        if (token != null) {

                            if (response.code() == 401) {
                                Intent intent = new Intent(context, Login.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }

                        return response;
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

