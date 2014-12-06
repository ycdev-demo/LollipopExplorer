package me.ycdev.android.demo.lollipopdemo;

import android.app.Application;

import me.ycdev.android.demo.lollipopdemo.utils.AppLogger;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        AppLogger.i(TAG, "app start...");
    }
}
