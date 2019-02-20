package com.example.asus_pc.trainer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.asus_pc.trainer.Me_Activty.BMIActivity;
import com.example.asus_pc.trainer.R;
import com.jaeger.library.StatusBarUtil;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class EnterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        StatusBarUtil.setTransparent(EnterActivity.this);
        Bmob.initialize(this, "5f0a55cbd319099d5f48f6b952cb17fc");

        ifUserFromSP();
    }

    private void ifUserFromSP() {
        SharedPreferences s = getSharedPreferences("UserMsg", MODE_PRIVATE);
        String user_id = s.getString("user_id", "");
        String user_pass = s.getString("user_pass", "");
        if (user_id.isEmpty() || user_pass.isEmpty()) {
            startActivity(new Intent(EnterActivity.this, MainActivity.class));
            finish();
        } else {
            BmobUser bmobUser = new BmobUser();
            bmobUser.setUsername(user_id);
            bmobUser.setPassword(user_pass);
            bmobUser.login(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser bmobUser, BmobException e) {
                    ToastShow b = new ToastShow();
                    if (e == null) {
                        b.toastShow(EnterActivity.this,"登录成功");
                        startActivity(new Intent(EnterActivity.this, LineShowActivity.class));
                        finish();
                    } else {
                        b.toastShow(EnterActivity.this,"未注册!");
                        startActivity(new Intent(EnterActivity.this, MainActivity.class));
                        finish();
                    }
                }
            });
        }

    }
}
