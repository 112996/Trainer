package com.example.asus_pc.trainer.until;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.asus_pc.trainer.activities.MainActivity;

public class LogOutBroadCastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityCollector.finishAll();
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
