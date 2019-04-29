package com.example.asus_pc.trainer.Me_Activty;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus_pc.trainer.activities.AddNoteActivity;
import com.example.asus_pc.trainer.db.DBHelper;
import com.example.asus_pc.trainer.activities.LineShowActivity;
import com.example.asus_pc.trainer.bean.MyUsers;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.until.ToastShow;
import com.example.asus_pc.trainer.bean.User_Args;
import com.example.asus_pc.trainer.until.ActivityCollector;
import com.jaeger.library.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class BMRActivity extends Activity {
    private TextView mAge, mHeight, mWeight, bmr_res, kll_range;
    private CheckBox sex;
    private RadioGroup mSport;
    private RadioButton high, middle, low;
    private DBHelper userDBHelper;
    private SQLiteDatabase mSQL, mSQL2;
    private String man = "男", woman = "女";
    private String Rbtn_high = "高", Rbtn_middle = "中", Rbtn_low = "低";
    private String SEX, t;
    private List list_t = new ArrayList();
    private List list_objectId = new ArrayList();
    private String CurrentDate;
    private boolean isUpdate, isSave;
    private String ObjectId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmr);
        StatusBarUtil.setColor(BMRActivity.this, Color.parseColor("#2D374C"), 0);
        ActivityCollector.addActivity(this);

        userDBHelper = new DBHelper(getApplicationContext());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        CurrentDate = simpleDateFormat.format(date);



        initView();
        showConfig();
        click();
        searchBmob();
        //saveToBmob();
        checkBoxListener();
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
    }

    private void checkBoxListener() {
        sex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ToastShow toastShow = new ToastShow();
                toastShow.toastShow(BMRActivity.this, "您现在是" + SEX + "生，别乱改！");
            }
        });
    }

    /**
     * 从数据库中获取数据显示在EditText中
     */
    public void showConfig() {

        mSQL = userDBHelper.getReadableDatabase();
        Cursor cursor = mSQL.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            if (cursor.getString(cursor.getColumnIndex("Age")) != null &&
                    cursor.getString(cursor.getColumnIndex("Height")) != null &&
                    cursor.getString(cursor.getColumnIndex("Weight")) != null &&
                    cursor.getString(cursor.getColumnIndex("Sex")) != null){

                mAge.setText(cursor.getString(cursor.getColumnIndex("Age")));
                mHeight.setText(cursor.getString(cursor.getColumnIndex("Height")));
                mWeight.setText(cursor.getString(cursor.getColumnIndex("Weight")));
                SEX = cursor.getString(cursor.getColumnIndex("Sex"));
                if (SEX.equals(woman)) {
                    sex.setChecked(true);
                } else {
                    sex.setChecked(false);
                }
                String Sports = cursor.getString(cursor.getColumnIndex("Sport"));
                if (Sports.equals(Rbtn_high)) {
                    high.setChecked(true);
                } else if (Sports.equals(Rbtn_middle)) {
                    middle.setChecked(true);
                } else if (Sports.equals(Rbtn_low)) {
                    low.setChecked(true);
                }
            }
            cursor.close();
        }
    }

    /**
     * 获取结果
     */
    public void click() {
        //BMR（男）=（13.7×体重（公斤））+（5.0×身高（公分））-（6.8×年龄）+66
        //BMR（女）=（9.6×体重（公斤））+（1.8×身高（公分））-（4.7×年龄）+655
        String weight = mWeight.getText().toString();
        String height = mHeight.getText().toString();
        String age = mAge.getText().toString();
        if (!weight.equals("") && !height.equals("") && !age.equals("")){
            Double bmr;
            if (sex.isChecked()) {
                bmr = ((9.6 * Double.valueOf(weight)) + (1.8 * Double.valueOf(height))) - (4.7 * Double.valueOf(age)) + 655;
            } else {
                bmr = ((13.7 * Double.valueOf(weight)) + (5.0 * Double.valueOf(height))) - (6.8 * Double.valueOf(age)) + 66;
            }

            //少量运动（每周1-3天轻量运动）：卡路里 = BMR × 1.375
            //中等运动量（每周3-5天中等程度运动）：卡路里 =  BMR × 1.55
            //高运动量（每周6-7天高强度运动）：卡路里 = BMR × 1.725
            Double KLL_low, KLL_high;
            KLL_low = bmr * 1.375;
            KLL_high = bmr * 1.725;

            bmr_res.setText(String.format("%.2f", bmr) + "kcal");
            kll_range.setText(String.format("%.2f", KLL_low) + "-" + String.format("%.2f", KLL_high));
            saveBMRToSP(String.valueOf(bmr));
            saveAllToSQL();
        }


    }

    private void saveBMRToSP(String bmr) {
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("BMR", bmr);
        editor.commit();
    }

    //相同日期改为修改
    private void saveAllToSQL() {
        SharedPreferences s = getSharedPreferences("config", MODE_PRIVATE);
        String bmi = s.getString("BMI", "");
        String whtr = s.getString("Whtr", "");
        String bfr = s.getString("BFR", "");
        String bmr = s.getString("BMR", "");
        mSQL2 = userDBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("BMI", bmi);
        cv.put("Whtr", whtr);
        cv.put("BFR", bfr);
        cv.put("BMR", bmr);
        cv.put("CurrentDate", CurrentDate);
        mSQL2.insert(DBHelper.TABLE_NAME_ARGS, null, cv);
    }

    /**
     * 到服务器
     */
    private void saveToBmob() {

        SharedPreferences s = getSharedPreferences("config", MODE_PRIVATE);
        String bmi = s.getString("BMI", "");
        String whtr = s.getString("Whtr", "");
        String bfr = s.getString("BFR", "");
        String bmr = s.getString("BMR", "");
        Log.e("    ", bmi + whtr + bfr + bmr);
        if (bmi.isEmpty() || whtr.isEmpty() || bfr.isEmpty() || bmr.isEmpty()) {
            ToastShow t = new ToastShow();
            t.toastShow(this, "还没有完善数据哦");
        }
        User_Args user_args = new User_Args();
        user_args.setBfr(bfr);
        user_args.setBmi(bmi);
        user_args.setBmr(bmr);
        user_args.setWhtr(whtr);
        user_args.setCurrentDate(CurrentDate);
        user_args.setAuthor(BmobUser.getCurrentUser(MyUsers.class));
        if(isSave){
            user_args.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.d("saveToBmob", "okokokokoko");
                    } else {
                        Log.e("saveToBmob", e.toString());
                    }
                }
            });
        }
        if (isUpdate){
            user_args.update(ObjectId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.e("更新", "update");
                    } else {
                        Log.e("TAG","done: " + e.getMessage());
                    }
                }
            });
        }
    }

    private void searchBmob() {
        BmobQuery<User_Args> query_other = new BmobQuery<>();
        query_other.addWhereEqualTo("author", BmobUser.getCurrentUser(MyUsers.class));
        query_other.order("-updatedAt");
        //包含作者信息
        query_other.include("author");
        query_other.findObjects(new FindListener<User_Args>() {

            @Override
            public void done(List<User_Args> object, BmobException e) {

                if (e == null) {
                    for (User_Args user_Args : object) {
                        if (user_Args.getBmi().isEmpty() && user_Args.getBfr().isEmpty() && user_Args.getWhtr().isEmpty() && user_Args.getBmr().isEmpty()) {
                            Log.e("数据不全", "数据不全");
                        } else {
                            list_t.add(user_Args.getCurrentDate());
                            list_objectId.add(user_Args.getObjectId());
                        }
                    }
                    t = list_t.get(0).toString();
                    ObjectId = list_objectId.get(0).toString();
                    if (!t.equals(CurrentDate)){
                        isSave = true;
                    }else{
                        isUpdate = true;
                        //isSave = true;   ///////测试用
                    }
                    saveToBmob();
                }else{
                    Log.e("查找Bmob失败", e.toString());
                }
            }
        });
    }
}