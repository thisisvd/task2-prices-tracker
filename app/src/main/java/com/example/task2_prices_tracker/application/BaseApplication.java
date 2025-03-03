package com.example.task2_prices_tracker.application;

import android.app.Application;

import com.example.task2_prices_tracker.BuildConfig;

import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;

@HiltAndroidApp
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // planted timber for debug build
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
