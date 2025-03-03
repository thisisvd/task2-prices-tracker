package com.example.task2_prices_tracker.data.network;

import com.example.task2_prices_tracker.domain.Prices;
import com.example.task2_prices_tracker.domain.User;
import com.example.task2_prices_tracker.domain.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {

    @POST("login")
    Call<UserResponse> loginUser(@Body User user);

    @POST("prices")
    Call<List<Prices>> getPrices(@Header("Authorization") String token);
}