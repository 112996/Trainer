package com.example.asus_pc.trainer.until;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetwork {
    public static boolean getInfo(Context context) {
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        //是否连接
        if (manager.getActiveNetworkInfo() != null) {
            boolean flag = manager.getActiveNetworkInfo().isAvailable();
            if (flag) {
                NetworkInfo.State state = manager.getActiveNetworkInfo().getState();
                if (state == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
}
