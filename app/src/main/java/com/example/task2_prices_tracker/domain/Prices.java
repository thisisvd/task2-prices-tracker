package com.example.task2_prices_tracker.domain;

import static com.example.task2_prices_tracker.utils.Constants.DATABASE_TABLE_NAME;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = DATABASE_TABLE_NAME)
public class Prices {

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("change")
    private double change;

    public Prices(int id, String name, double price, double change) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.change = change;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getChange() {
        return change;
    }
}