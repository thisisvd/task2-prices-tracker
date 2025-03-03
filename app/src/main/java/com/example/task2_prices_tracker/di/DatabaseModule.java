package com.example.task2_prices_tracker.di;

import static com.example.task2_prices_tracker.utils.Constants.DATABASE_NAME;

import android.content.Context;

import androidx.room.Room;

import com.example.task2_prices_tracker.core.NetworkUtils;
import com.example.task2_prices_tracker.data.MainRepository;
import com.example.task2_prices_tracker.data.local.EncryptedSharedPreference;
import com.example.task2_prices_tracker.data.local.room.PricesDao;
import com.example.task2_prices_tracker.data.local.room.PricesDatabase;
import com.example.task2_prices_tracker.data.network.ApiImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public static PricesDatabase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, PricesDatabase.class, DATABASE_NAME).build();
    }

    @Provides
    @Singleton
    public static PricesDao providesPrices(PricesDatabase database) {
        return database.pricesDao();
    }

    @Provides
    @Singleton
    public EncryptedSharedPreference provideEncryptedSharedPreference(@ApplicationContext Context context) {
        return new EncryptedSharedPreference(context);
    }

    @Provides
    @Singleton
    public MainRepository provideMainRepository(ApiImpl apiImpl, EncryptedSharedPreference encryptedSharedPreference, PricesDao pricesDao, NetworkUtils networkUtils) {
        return new MainRepository(apiImpl, encryptedSharedPreference, pricesDao, networkUtils);
    }
}
