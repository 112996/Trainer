package com.example.asus_pc.trainer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity{
    private Button login_btn;
    private Button regist_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this,"5f0a55cbd319099d5f48f6b952cb17fc");

        initView();
    }

    private void initView(){
        login_btn = findViewById(R.id.login_btn);
        regist_btn = findViewById(R.id.regist_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(MainActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        regist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

}
