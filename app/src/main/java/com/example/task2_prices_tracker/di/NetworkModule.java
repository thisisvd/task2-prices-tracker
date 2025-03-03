package com.example.task2_prices_tracker.di;

import static com.example.task2_prices_tracker.utils.Constants.API_URL;

import android.content.Context;

import com.example.task2_prices_tracker.BuildConfig;
import com.example.task2_prices_tracker.core.NetworkUtils;
import com.example.task2_prices_tracker.data.network.Api;
import com.example.task2_prices_tracker.data.network.ApiHelper;
import com.example.task2_prices_tracker.data.network.ApiImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
        } else {
            return new OkHttpClient.Builder().build();
        }
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(API_URL).client(okHttpClient).build();
    }

    @Provides
    public Api provideApiInterface(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }

    @Provides
    @Singleton
    public ApiHelper provideApiHelper(ApiImpl apiImpl) {
        return apiImpl;
    }

    @Provides
    @Singleton
    public static NetworkUtils provideNetworkUtils(@ApplicationContext Context context) {
        return new NetworkUtils(context);
    }
}