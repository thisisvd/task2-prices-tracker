package com.example.task2_prices_tracker.data.local.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.task2_prices_tracker.domain.Prices;

import java.util.List;

@Dao
public interface PricesDao {

    @Insert
    void insertPrices(Prices prices);

    @Query("SELECT * FROM prices_table")
    List<Prices> getAllPrices();

    @Query("SELECT * FROM prices_table " + "WHERE name LIKE '%' || :searchQuery || '%' " + "OR price LIKE '%' || :searchQuery || '%' " + "OR change LIKE '%' || :searchQuery || '%'")
    List<Prices> searchInPrices(String searchQuery);

    @Query("DELETE FROM prices_table")
    void deleteAllPrices();
}