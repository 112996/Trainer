package com.example.asus_pc.trainer.until;

import android.app.Activity;
import android.content.Intent;

import com.example.asus_pc.trainer.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    private ActivityCollector() {
    }
    public static ArrayList<Activity> mActivityList = new ArrayList<Activity>();


    public static void addActivity(Activity act) {
        if (!mActivityList.contains(act)){
            mActivityList.add(act);
        }
    }

    public static void removeActivity(Activity act) {
        mActivityList.remove(act);
    }

    public static void finishAll() {
        for (Activity activity : mActivityList){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
