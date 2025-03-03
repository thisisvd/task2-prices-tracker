package com.example.task2_prices_tracker.data.local.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.task2_prices_tracker.domain.Prices;

@Database(entities = {Prices.class}, version = 1, exportSchema = false)
public abstract class PricesDatabase extends RoomDatabase {

    public abstract PricesDao pricesDao();
}
