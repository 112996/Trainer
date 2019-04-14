package com.example.asus_pc.trainer;

import android.app.Application;
import android.content.Context;

import com.example.asus_pc.trainer.until.AppBackFrontHelper;

public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();


    }
}
