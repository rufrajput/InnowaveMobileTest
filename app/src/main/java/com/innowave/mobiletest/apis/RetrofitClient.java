package com.innowave.mobiletest.apis;


import com.innowave.mobiletest.utils.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    static RetrofitClient instance;

    public static ApiCall getInstance() {
        if (instance == null)
            instance = new RetrofitClient();
        return instance.getApiClient();
    }

    private OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient.connectTimeout(20, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY));
        return httpClient.build();
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClient())
                .build();
    }

    public ApiCall getApiClient() {
        final Retrofit retrofit = createRetrofit();
        return retrofit.create(ApiCall.class);
    }
}
