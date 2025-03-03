package com.example.task2_prices_tracker.data.network;

import com.example.task2_prices_tracker.domain.Prices;
import com.example.task2_prices_tracker.domain.User;
import com.example.task2_prices_tracker.domain.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;

public interface ApiHelper {

    Call<UserResponse> loginUser(@Body User user);

    Call<List<Prices>> getPrices(String token);
}