package com.example.asus_pc.trainer.Me_Activty;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.example.asus_pc.trainer.LineShowActivity;
import com.example.asus_pc.trainer.R;
import com.jaeger.library.StatusBarUtil;

public class BMRActivity extends Activity {
    private ImageButton bmr_back_btn;
    private EditText mAge, mHeight, mWeight;
    private CheckBox sex;
    private RadioGroup mSport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmr);
        //StatusBarUtil.setTranslucent(BMRActivity.this,15);
        StatusBarUtil.setTransparent(BMRActivity.this);

        initView();
        showConfig();
        click();
    }

    private void initView() {
        mAge = findViewById(R.id.age);
        mHeight = findViewById(R.id.height);
        mWeight = findViewById(R.id.weight);
        sex = findViewById(R.id.sex);
        mSport = findViewById(R.id.sport);
        bmr_back_btn = findViewById(R.id.bmr_back_btn);
        bmr_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BMRActivity.this, LineShowActivity.class);
                i.putExtra("id", 2);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * 从sharedPreference中获取数据显示在EditText中
     */
    public void showConfig(){
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String age = sharedPreferences.getString("age", "");
        String height = sharedPreferences.getString("height","");
        String weight = sharedPreferences.getString("weight", "");
        mAge.setText(age);
        mHeight.setText(height);
        mWeight.setText(weight);
    }

    /**
     * 点击获取结果
     */
    public void click(){

    }
}

