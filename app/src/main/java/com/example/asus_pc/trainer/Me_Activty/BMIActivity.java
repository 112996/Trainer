package com.example.asus_pc.trainer.Me_Activty;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.asus_pc.trainer.db.DBHelper;
import com.example.asus_pc.trainer.activities.LineShowActivity;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.until.ToastShow;
import com.example.asus_pc.trainer.until.ActivityCollector;
import com.jaeger.library.StatusBarUtil;

import java.math.BigDecimal;

public class BMIActivity extends Activity {
    private TextView age, height, weight;
    private Button bmi_res;
    private CheckBox sex;
    private TextView ideal_weight;
    private boolean isFirst_click = true;
    private DBHelper userDBHelper;
    private SQLiteDatabase mSQL;
    private String woman = "女", man = "男";
    private String SEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        StatusBarUtil.setColor(BMIActivity.this, Color.parseColor("#2D374C"), 0);
        ActivityCollector.addActivity(this);

        initView();

        userDBHelper = new DBHelper(getApplicationContext());
        mSQL = userDBHelper.getWritableDatabase();
        showConfig();
        click();
        checkBoxListener();
    }

    private void initView() {
        age = findViewById(R.id.age);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        bmi_res = findViewById(R.id.bmi_res);
        sex = findViewById(R.id.sex);
        ideal_weight = findViewById(R.id.ideal_weight);


    }

    public void click() {
        //点击计算结果
        bmi_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFirst_click) {
                    getWindow().getDecorView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String hfromheight = height.getText().toString();
                            String wfromweight = weight.getText().toString();
                            if (hfromheight.isEmpty() || wfromweight.isEmpty()){
                                ToastShow b = new ToastShow();
                                b.toastShow(BMIActivity.this,"数据为空");
                            }else {
                                double hh = Double.valueOf(height.getText().toString());
                                double h = hh * 0.01;
                                double w = Double.valueOf(weight.getText().toString());
                                double res = w / (h * h);
                                double value = new BigDecimal(res).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                String bmi = "" + value;
                                bmi_res.setText(String.valueOf(value));
                                bmi_res.setTextSize(25);
                                sWeight();
                                saveBMIToSP(bmi);
                                saveBMIToSQL(bmi);
                            }

                        }
                    }, 2000);
                }
                isFirst_click = false;
            }
        });
    }


    /**
     * 计算标准体重
     */
    private void sWeight() {
        double Height = Double.valueOf(height.getText().toString());
        double sWeight;
        if (sex.isChecked()) {
            //女性标准体重计算方法为（身高cm－70）×60﹪
            sWeight = (Height - 70) * 0.6;
        } else {
            //男性标准体重计算方法为（身高cm－80）×70﹪
            sWeight = (Height - 80) * 0.7;
        }
        double value = new BigDecimal(sWeight).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        ideal_weight.setText(String.valueOf(value));

    }

    private void checkBoxListener(){
        sex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ToastShow toastShow = new ToastShow();
                toastShow.toastShow(BMIActivity.this, "您现在是"+SEX+"生，别乱改！");
            }
        });
    }

    public void showConfig() {

        Cursor cursor = mSQL.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToLast();
            if (cursor.getCount() != 0) {
                String AGE = cursor.getString(cursor.getColumnIndex("Age"));
                String HEIGHT = cursor.getString(cursor.getColumnIndex("Height"));
                String WEIGHT = cursor.getString(cursor.getColumnIndex("Weight"));
                SEX= cursor.getString(cursor.getColumnIndex("Sex"));
                if (AGE.isEmpty() || HEIGHT.isEmpty() || WEIGHT.isEmpty()) {
                    ToastShow b = new ToastShow();
                    b.toastShow(BMIActivity.this, "请前往设置界面完善个人信息！");
                } else {
                    age.setText(AGE);
                    height.setText(HEIGHT);
                    weight.setText(WEIGHT);
                    if (SEX.equals(woman)){
                        sex.setChecked(true);
                    }else {
                        sex.setChecked(false);
                    }
                }
                cursor.close();
            }
        }
    }

    private void saveBMIToSP(String bmi){
        if (bmi == null){
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("BMI", bmi);
        editor.commit();
    }
    private void saveBMIToSQL(String bmi){
        ContentValues cv = new ContentValues();
        cv.put("BMI", bmi);
        mSQL.insert(DBHelper.TABLE_NAME_ARGS, null, cv);
    }

}
