package com.example.asus_pc.trainer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.asus_pc.trainer.Me_Activty.BMIActivity;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.until.BaseCompatActivity;
import com.jaeger.library.StatusBarUtil;

import cn.bmob.v3.Bmob;

public class MainActivity extends BaseCompatActivity {
    private Button login_btn;
    private Button regist_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setTransparent(MainActivity.this);
        Bmob.initialize(this,"5f0a55cbd319099d5f48f6b952cb17fc");
        initView();

    }
    private void initView(){
        login_btn = findViewById(R.id.login_btn);
        regist_btn = findViewById(R.id.regist_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        regist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }
}
