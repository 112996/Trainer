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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.asus_pc.trainer.DBHelper;
import com.example.asus_pc.trainer.LineShowActivity;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.until.ActivityCollector;
import com.jaeger.library.StatusBarUtil;

public class BMRActivity extends Activity {
    private ImageButton bmr_back_btn;
    private TextView mAge, mHeight, mWeight, bmr_res, kll_range;
    private CheckBox sex;
    private RadioGroup mSport;
    private RadioButton high, middle, low;
    private DBHelper userDBHelper;
    private SQLiteDatabase mSQL, mSQL2;
    private String man = "男", woman = "女";
    private String Rbtn_high = "高", Rbtn_middle = "中", Rbtn_low = "低";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmr);
        StatusBarUtil.setColor(BMRActivity.this, Color.parseColor("#2D374C"),0);
        ActivityCollector.addActivity(this);

        userDBHelper = new DBHelper(getApplicationContext());

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
        high = findViewById(R.id.Rhigh);
        middle = findViewById(R.id.Rmiddle);
        low = findViewById(R.id.Rlow);
        bmr_res = findViewById(R.id.bmr_res);
        kll_range = findViewById(R.id.kll_range);
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
     * 从数据库中获取数据显示在EditText中
     */
    public void showConfig(){

        mSQL = userDBHelper.getReadableDatabase();
        Cursor cursor = mSQL.query(DBHelper.TABLE_NAME, null,null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToLast();
            mAge.setText(cursor.getString(cursor.getColumnIndex("Age")));
            mHeight.setText(cursor.getString(cursor.getColumnIndex("Height")));
            mWeight.setText(cursor.getString(cursor.getColumnIndex("Weight")));
            String SEX = cursor.getString(cursor.getColumnIndex("Sex"));
            if (SEX.equals(woman)){
                sex.setChecked(true);
            }else {
                sex.setChecked(false);
            }
            String Sports = cursor.getString(cursor.getColumnIndex("Sport"));
            if (Sports.equals(Rbtn_high)){
                high.setChecked(true);
            }else if (Sports.equals(Rbtn_middle)){
                middle.setChecked(true);
            }else if (Sports.equals(Rbtn_low)){
                low.setChecked(true);
            }
            cursor.close();
        }
    }

    /**
     * 点击获取结果
     */
    public void click(){
        //BMR（男）=（13.7×体重（公斤））+（5.0×身高（公分））-（6.8×年龄）+66
        //BMR（女）=（9.6×体重（公斤））+（1.8×身高（公分））-（4.7×年龄）+655
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String weight = sharedPreferences.getString("weight", "");
        String height = sharedPreferences.getString("height", "");
        String age = sharedPreferences.getString("age", "");
        Double bmr;
        if (sex.isChecked()){
             bmr = ((9.6 * Double.valueOf(weight)) + (1.8 * Double.valueOf(height))) - (4.7 * Double.valueOf(age)) + 655;
        }else{
             bmr = ((13.7 * Double.valueOf(weight)) + (5.0 * Double.valueOf(height))) - (6.8 * Double.valueOf(age)) + 66;
        }


        //少量运动（每周1-3天轻量运动）：卡路里 = BMR × 1.375
        //中等运动量（每周3-5天中等程度运动）：卡路里 =  BMR × 1.55
        //高运动量（每周6-7天高强度运动）：卡路里 = BMR × 1.725
        Double KLL_low, KLL_high ;
        KLL_low = bmr * 1.375;
        KLL_high = bmr * 1.725;

        bmr_res.setText(String.valueOf(bmr) + "kcal");
        kll_range.setText(String.valueOf(KLL_low)+"-"+String.valueOf(KLL_high)+"Kcal");
        saveBMRToSP(String.valueOf(bmr));
        saveAllToSQL();
    }

    private void saveBMRToSP(String bmr){
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("BMR", bmr);
        editor.commit();
    }

    private void saveAllToSQL(){
        SharedPreferences s = getSharedPreferences("config", MODE_PRIVATE);
        String bmi = s.getString("BMI", "");
        String whtr = s.getString("Whtr", "");
        String bfr = s.getString("BFR", "");
        String bmr = s.getString("BMR", "");
        mSQL2 = userDBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("BMI",bmi);
        cv.put("Whtr", whtr);
        cv.put("BFR", bfr);
        cv.put("BMR", bmr);
        mSQL2.insert(DBHelper.TABLE_NAME_ARGS, null, cv);
    }
}

