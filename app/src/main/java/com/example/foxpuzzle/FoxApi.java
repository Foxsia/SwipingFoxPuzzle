package com.example.foxpuzzle;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FoxApi {
    @GET("floof/")
    Call<FoxResponse> getRandomFox();
}
