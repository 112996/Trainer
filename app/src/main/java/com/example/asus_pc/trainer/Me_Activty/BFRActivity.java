package com.example.asus_pc.trainer.Me_Activty;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.asus_pc.trainer.db.DBHelper;
import com.example.asus_pc.trainer.activities.LineShowActivity;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.until.ToastShow;
import com.example.asus_pc.trainer.until.ActivityCollector;
import com.jaeger.library.StatusBarUtil;

import java.math.BigDecimal;

public class BFRActivity extends Activity {
    private TextView mAge, mHeight, mWaist, mNeck;
    private Button bfr_res;
    private Boolean isFirstClick = true;
    private CheckBox sex;
    private DBHelper userDBHelper;
    private SQLiteDatabase mSQL;
    private String man="男", woman = "女";
    private String SEX, BMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfr);
        StatusBarUtil.setColor(BFRActivity.this, Color.parseColor("#2D374C"), 0);
        ActivityCollector.addActivity(this);
        initView();
        showConfig();
        click();
        checkBoxListener();
    }

    private void initView() {

        mAge = findViewById(R.id.age);
        mHeight = findViewById(R.id.height);
        mWaist = findViewById(R.id.waist);
        mNeck = findViewById(R.id.neck);
        bfr_res = findViewById(R.id.bfr_res);
        sex = findViewById(R.id.sex);



    }

    private void checkBoxListener(){
        sex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ToastShow toastShow = new ToastShow();
                toastShow.toastShow(BFRActivity.this, "您现在是"+SEX+"生，别乱改！");
            }
        });
    }

    /**
     * 点击获取结果
     */
    public void click() {
        bfr_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAge.getText().toString().isEmpty() || mHeight.getText().toString().isEmpty() || mWaist.getText().toString().isEmpty() || mNeck.getText().toString().isEmpty()) {
                    ToastShow b = new ToastShow();
                    b.toastShow(BFRActivity.this, "请输入完整信息！");
                } else {
                    bfr_res.setText("请稍后...");
                    if (isFirstClick) {
                        getWindow().getDecorView().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //体脂率 =1.2×BMI+0.23× 年龄-5.4-10.8×性别（男为1，女为0）
                                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                                String bmi = sharedPreferences.getString("BMI", "");
                                String age = mAge.getText().toString();
                                if (bmi.isEmpty() || age.isEmpty()){
                                    Toast.makeText(BFRActivity.this,bmi+age,Toast.LENGTH_SHORT).show();
                                }else {
                                    String bfr;
                                    if (sex.isChecked()) {
                                        double bfr_ = Double.valueOf(bmi) * 1.2 + 0.23 * Double.valueOf(age) - 5.4 - 10.8 * 0;
                                        double value = new BigDecimal(bfr_).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                        bfr = "" + value;
                                    } else {
                                        double bfr_ = Double.valueOf(bmi) * 1.2 + 0.23 * Double.valueOf(age) - 5.4 - 10.8 * 1;
                                        double value = new BigDecimal(bfr_).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                        bfr = "" + value;
                                    }
                                    bfr_res.setText(bfr);
                                    bfr_res.setTextSize(25);
                                    saveBFRToSP(bfr);
                                }

                            }
                        }, 1500);
                    }
                    isFirstClick = false;
                }
            }
        });
    }

    /**
     * 从数据库里面获取信息并且显示在EditText上
     */
    public void showConfig() {
        userDBHelper = new DBHelper(getApplicationContext());
        mSQL = userDBHelper.getReadableDatabase();
        Cursor cursor = mSQL.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            mAge.setText(cursor.getString(cursor.getColumnIndex("Age")));
            mHeight.setText(cursor.getString(cursor.getColumnIndex("Height")));
            mWaist.setText(cursor.getString(cursor.getColumnIndex("Waist")));
            mNeck.setText(cursor.getString(cursor.getColumnIndex("Neck")));
            SEX = cursor.getString(cursor.getColumnIndex("Sex"));
            if (SEX.equals(woman)){
                sex.setChecked(true);
            }else {
                sex.setChecked(false);
            }
            cursor.close();
        }
    }

    private void saveBFRToSP(String bfr){
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("BFR", bfr);
        editor.commit();
    }
}
