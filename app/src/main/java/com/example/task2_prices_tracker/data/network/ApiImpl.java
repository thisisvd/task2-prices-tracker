package com.example.task2_prices_tracker.data.network;

import com.example.task2_prices_tracker.domain.Prices;
import com.example.task2_prices_tracker.domain.User;
import com.example.task2_prices_tracker.domain.UserResponse;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class ApiImpl implements ApiHelper {

    private final Api api;

    @Inject
    public ApiImpl(Api api) {
        this.api = api;
    }

    @Override
    public Call<UserResponse> loginUser(User user) {
        return api.loginUser(user);
    }

    @Override
    public Call<List<Prices>> getPrices(String token) {
        return api.getPrices(token);
    }
}
