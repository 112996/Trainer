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
import android.util.Log;
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
import com.jaeger.library.StatusBarUtil;

import java.math.BigDecimal;

public class WhtrActivity extends Activity {
    private ImageButton whtr_back_btn;
    private TextView mHeight, mWaist, mAge;
    private Button whtr_res;
    private TextView waistline;
    private CheckBox sex;
    private boolean isFirstClick = true;
    private DBHelper userDBHelper;
    private SQLiteDatabase mSQL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whtr);
        StatusBarUtil.setColor(WhtrActivity.this, Color.parseColor("#2D374C"),0);

        initView();
        showConfig();
        click();

    }

    private void initView() {

        mHeight = findViewById(R.id.height);
        mWaist = findViewById(R.id.waist);
        mAge = findViewById(R.id.age);
        sex = findViewById(R.id.sex);
        whtr_res = findViewById(R.id.whtr_res);
        waistline = findViewById(R.id.waistline);

        whtr_back_btn = findViewById(R.id.whtr_back_btn);
        whtr_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WhtrActivity.this, LineShowActivity.class);
                i.putExtra("id", 2);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * 计算理想腰围
     */
    private void sWaist() {
        int height = Integer.parseInt(mHeight.getText().toString());
        int minDeal = (new Double(height * 0.43)).intValue();
        int maxDeal = (new Double(height * 0.53)).intValue();
        waistline.setText(minDeal + " - " + maxDeal);
    }

    /**
     * 点击获取结果
     */
    public void click() {
        whtr_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAge.getText().toString().isEmpty() || mHeight.getText().toString().isEmpty() || mWaist.getText().toString().isEmpty()) {
                    ToastShow b = new ToastShow();
                    b.toastShow(WhtrActivity.this, "请输入完整信息！");
                } else {
                    if (isFirstClick) {
                        getWindow().getDecorView().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                double h = Double.valueOf(mHeight.getText().toString());
                                double w = Double.valueOf(mWaist.getText().toString());
                                double res = w / h;
                                double value = new BigDecimal(res).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                String whtr = "" + value;
                                whtr_res.setText(String.valueOf(value));
                                whtr_res.setTextSize(25);
                                sWaist();

                            }
                        }, 2000);
                    }
                    isFirstClick = false;
                }

            }
        });
    }

    /**
     * 从数据库里面获取信息并且显示
     */
    public void showConfig() {

        userDBHelper = new DBHelper(getApplicationContext());
        mSQL = userDBHelper.getReadableDatabase();
        Cursor cursor = mSQL.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null){
             cursor.moveToLast();
            if (cursor.getCount() != 0){
                mAge.setText(cursor.getString(cursor.getColumnIndex("Age")));
                mHeight.setText(cursor.getString(cursor.getColumnIndex("Height")));
                mWaist.setText(cursor.getString(cursor.getColumnIndex("Waist")));
                cursor.close();
            }else{
            }
        }

    }
}
