package com.example.asus_pc.trainer.Me_Activty;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.activities.LoginActivity;
import com.jaeger.library.StatusBarUtil;

import cn.bmob.v3.Bmob;

public class Train_LogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train__log);
        StatusBarUtil.setTransparent(Train_LogActivity.this);
        Bmob.initialize(this, "5f0a55cbd319099d5f48f6b952cb17fc");
    }
}
