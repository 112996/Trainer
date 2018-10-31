package com.example.asus_pc.trainer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.asus_pc.trainer.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class EnterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        Bmob.initialize(this,"5f0a55cbd319099d5f48f6b952cb17fc");
        currentUser();
    }
    private void currentUser(){
        BmobUser bmobUser = new BmobUser();
        if (bmobUser != null){
            Intent i = new Intent();
            i.setClass(this,LineShowActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent();
            i.setClass(this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}