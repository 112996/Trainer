package com.example.asus_pc.trainer.Me_Activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.asus_pc.trainer.DBHelper;
import com.example.asus_pc.trainer.LineShowActivity;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.ToastShow;
import com.example.asus_pc.trainer.until.ActivityCollector;
import com.jaeger.library.StatusBarUtil;

import java.math.BigDecimal;

public class BFRActivity extends Activity {
    private ImageButton bfr_back_btn;
    private TextView mAge, mHeight, mWaist, mNeck;
    private Button bfr_res;
    private Boolean isFirstClick = true;
    private CheckBox sex;
    private DBHelper userDBHelper;
    private SQLiteDatabase mSQL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfr);
        StatusBarUtil.setColor(BFRActivity.this, Color.parseColor("#2D374C"), 0);
        ActivityCollector.addActivity(this);
        initView();
        showConfig();
        click();
    }

    private void initView() {

        mAge = findViewById(R.id.age);
        mHeight = findViewById(R.id.height);
        mWaist = findViewById(R.id.waist);
        mNeck = findViewById(R.id.neck);
        bfr_res = findViewById(R.id.bfr_res);
        sex = findViewById(R.id.sex);

        bfr_back_btn = findViewById(R.id.bfr_back_btn);
        bfr_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BFRActivity.this, LineShowActivity.class);
                i.putExtra("id", 2);
                startActivity(i);
                finish();
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
                    if (isFirstClick) {
                        getWindow().getDecorView().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //体脂率 =1.2×BMI+0.23× 年龄-5.4-10.8×性别（男为1，女为0）
                                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                                String bmi = sharedPreferences.getString("bmi", "");
                                String age = sharedPreferences.getString("age", "");
                                String bfr;
                                if (sex.isChecked()) {
                                    double bfr_ = Double.parseDouble(bmi) * 1.2 + 0.23 * Double.parseDouble(age) - 5.4 - 10.8 * 0;
                                    double value = new BigDecimal(bfr_).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                    bfr = "" + value;
                                } else {
                                    double bfr_ = Double.parseDouble(bmi) * 1.2 + 0.23 * Double.parseDouble(age) - 5.4 - 10.8 * 1;
                                    double value = new BigDecimal(bfr_).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                    bfr = "" + value;
                                }
                                bfr_res.setText(bfr);
                                bfr_res.setTextSize(25);
                                //saveConfig(BFRActivity.this, mNeck.getText().toString(), bfr);
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
        if (cursor != null) {
            cursor.moveToLast();
            if (cursor.getCount() != 0) {
                mAge.setText(cursor.getString(cursor.getColumnIndex("Age")));
                mHeight.setText(cursor.getString(cursor.getColumnIndex("Height")));
                mWaist.setText(cursor.getString(cursor.getColumnIndex("Waist")));
                mNeck.setText(cursor.getString(cursor.getColumnIndex("Neck")));
                cursor.close();
            }
        }
    }
}
