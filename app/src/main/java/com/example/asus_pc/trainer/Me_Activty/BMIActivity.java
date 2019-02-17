package com.example.asus_pc.trainer.Me_Activty;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.ConditionVariable;
import android.os.IInterface;
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

public class BMIActivity extends Activity {
    private ImageButton bmi_back_btn;
    private TextView age, height, weight;
    private Button bmi_res;
    private CheckBox sex;
    private TextView ideal_weight;
    private boolean isFirst_click = true;
    private DBHelper userDBHelper;
    private SQLiteDatabase mSQL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        //StatusBarUtil.setTranslucent(BMIActivity.this,15);
        StatusBarUtil.setTransparent(BMIActivity.this);
        initView();
        showConfig();
    }

    private void initView() {
        bmi_back_btn = findViewById(R.id.bmi_back_btn);
        age = findViewById(R.id.age);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        bmi_res = findViewById(R.id.bmi_res);
        sex = findViewById(R.id.sex);
        ideal_weight = findViewById(R.id.ideal_weight);

        //点击计算结果
        bmi_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (age.getText().toString().isEmpty() || height.getText().toString().isEmpty() || weight.getText().toString().isEmpty()) {
                    ToastShow b = new ToastShow();
                    b.toastShow(BMIActivity.this, "请输入完整信息！");
                } else {
                    if (isFirst_click) {
                        getWindow().getDecorView().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                double hh = Double.valueOf(height.getText().toString());
                                double h = hh * 0.01;
                                double w = Double.valueOf(weight.getText().toString());
                                double res = w / (h * h);
                                double value = new BigDecimal(res).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                String bmi = "" + value;
                                bmi_res.setText(String.valueOf(value));
                                bmi_res.setTextSize(25);
                                sWeight();
                               //saveConfig(BMIActivity.this, age.getText().toString(), height.getText().toString(), weight.getText().toString(),bmi);

                            }
                        }, 2000);
                    }
                    isFirst_click = false;
                }
            }
        });

        //返回LineShowActivity中的fragment_me
        bmi_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BMIActivity.this, LineShowActivity.class);
                i.putExtra("id", 2);
                startActivity(i);
                finish();
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

    public void saveConfig(Context context, String age, String height, String weight, String bmi){
        SharedPreferences s =  context.getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putString("age", age);
        editor.putString("height", height);
        editor.putString("weight", weight);
        editor.putString("bmi", bmi);
        editor.commit(); //提交数据保存至config文件
    }

    public void showConfig(){
        userDBHelper = new DBHelper(getApplicationContext());
        mSQL = userDBHelper.getWritableDatabase();
        Cursor cursor = mSQL.query(DBHelper.TABLE_NAME, null,null, null, null, null, null);
        while (cursor.moveToLast()){

        }
        age.setText(cursor.getString(cursor.getColumnIndex("age")));
        height.setText(cursor.getString(cursor.getColumnIndex("height")));
        weight.setText(cursor.getString(cursor.getColumnIndex("weight")));
    }

}
