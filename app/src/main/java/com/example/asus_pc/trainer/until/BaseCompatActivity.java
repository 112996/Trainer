package com.example.asus_pc.trainer.until;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BaseCompatActivity extends AppCompatActivity{
    protected LogOutBroadCastReceiver localReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //创建活动时，将其加入管理器中
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //注册广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.asus_pc.trainer.logout");
        localReceiver = new LogOutBroadCastReceiver();
        registerReceiver(localReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //取消注册广播接收器
        unregisterReceiver(localReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //销毁活动时， 将其从管理器中移除
        ActivityCollector.removeActivity(this);
    }
}
