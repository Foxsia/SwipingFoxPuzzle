package com.example.foxpuzzle;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl("https://randomfox.ca")
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .build();

    public static FoxApi getApi() {
        return retrofit.create(FoxApi.class);
    }
}
