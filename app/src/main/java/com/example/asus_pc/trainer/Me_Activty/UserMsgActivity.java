package com.example.asus_pc.trainer.Me_Activty;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.asus_pc.trainer.db.DBHelper;
import com.example.asus_pc.trainer.activities.LineShowActivity;
import com.example.asus_pc.trainer.bean.MyUsers;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.until.ToastShow;
import com.example.asus_pc.trainer.bean.User_Message;
import com.example.asus_pc.trainer.bean.UsersMsg;
import com.example.asus_pc.trainer.until.ActivityCollector;
import com.jaeger.library.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class UserMsgActivity extends Activity {
    private EditText mAge, mHeight, mWeight, mWaist, mNeck;
    private RadioButton man, woman, high, middle, low;
    private Button ok;
    private DBHelper userDBHelper;
    private SQLiteDatabase mSQL, mSQL1;
    private String MAN = "男", WOMAN = "女";
    private String HIGH = "高", MIDDLE = "中", LOW = "低";
    private String CurrentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg);
        StatusBarUtil.setColor(UserMsgActivity.this, Color.parseColor("#2D374C"), 0);
        Bmob.initialize(this, "5f0a55cbd319099d5f48f6b952cb17fc");

        ActivityCollector.addActivity(this);

        initViews();
        userDBHelper = new DBHelper(getApplicationContext());
        mSQL = userDBHelper.getWritableDatabase();
        mSQL1 = userDBHelper.getReadableDatabase();


    }


    private void initViews() {
        mAge = findViewById(R.id.age);
        mHeight = findViewById(R.id.height);
        mWeight = findViewById(R.id.weight);
        mWaist = findViewById(R.id.waist);
        mNeck = findViewById(R.id.neck);
        man = findViewById(R.id.man);
        woman = findViewById(R.id.woman);
        high = findViewById(R.id.high);
        middle = findViewById(R.id.middle);
        low = findViewById(R.id.low);

        ok = findViewById(R.id.ok);

        readConfig();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        CurrentDate = simpleDateFormat.format(date);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDatas();
                saveToBmob();
                query();
                querySql();
            }
        });
    }

    private void readConfig(){
        SharedPreferences s  = getSharedPreferences("config", MODE_PRIVATE);
        if (s != null){
            String con_age = s.getString("age", "");
            String con_height = s.getString("height", "");
            String con_weight = s.getString("weight", "");
            String con_waist = s.getString("waist", "");
            String con_neck = s.getString("neck", "");
            String con_sex = s.getString("sex", "");
            String con_sport = s.getString("sport", "");
            mAge.setText(con_age);
            mHeight.setText(con_height);
            mWeight.setText(con_weight);
            mWaist.setText(con_waist);
            mNeck.setText(con_neck);
            if (con_sex.equals(MAN)){
                man.setChecked(true);
            }else{
                woman.setChecked(true);
            }

            if (con_sport.equals(HIGH)){
                high.setChecked(true);
            }else if (con_sport.equals(MIDDLE)){
                middle.setChecked(true);
            }else{
                low.setChecked(true);
            }
        }
    }

    private void addDatas() {
        insertUsersMsg();
        String add_sex;
        if (man.isChecked()) {
            add_sex = MAN;
        } else {
            add_sex = WOMAN;
        }
        String add_sport;
        if (high.isChecked()) {
            add_sport = HIGH;
        } else if (middle.isChecked()) {
            add_sport = MIDDLE;
        } else {
            add_sport = LOW;
        }
        saveConfig(UserMsgActivity.this, mAge.getText().toString(), mHeight.getText().toString(), mWeight.getText().toString(), mWaist.getText().toString(), mNeck.getText().toString(), add_sex, add_sport, CurrentDate);
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
    }

    public void insertUsersMsg() {
        ContentValues values = userToContentValues(mockUsersMsg());
        mSQL.insert(DBHelper.TABLE_NAME, null, values);
    }

    private UsersMsg mockUsersMsg() {
        UsersMsg users = new UsersMsg();
        users.age = mAge.getText().toString();
        users.height = mHeight.getText().toString();
        users.weight = mWeight.getText().toString();
        users.waist = mWaist.getText().toString();
        users.neck = mNeck.getText().toString();
        users.currentDate = CurrentDate;
        if (man.isChecked()) {
            users.sex = MAN;
        } else {
            users.sex = WOMAN;
        }
        if (high.isChecked()) {
            users.sport = HIGH;
        } else if (middle.isChecked()) {
            users.sport = MIDDLE;
        } else {
            users.sport = LOW;
        }
        return users;
    }

    private ContentValues userToContentValues(UsersMsg usersMsg) {
        ContentValues contentValues = new ContentValues();
        if (!usersMsg.age.equals("")&&!usersMsg.height.equals("")&& !usersMsg.weight.equals("")&& !usersMsg.waist.equals("")&& !usersMsg.neck.equals("")){
            contentValues.put("Age", usersMsg.age);
            contentValues.put("Height", usersMsg.height);
            contentValues.put("Weight", usersMsg.weight);
            contentValues.put("Waist", usersMsg.waist);
            contentValues.put("Neck", usersMsg.neck);
            contentValues.put("Sex", usersMsg.sex);
            contentValues.put("Sport", usersMsg.sport);
            contentValues.put("CurrentDate", usersMsg.currentDate);
            return contentValues;
        }
        return null;
    }

    /**
     * 存在sharePreference
     */
    public void saveConfig(Context context, String age, String height, String weight, String waist, String neck, String sex, String sport, String currentDate) {
        SharedPreferences s = context.getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        if (!age.equals("")&& !height.equals("")&& !weight.equals("") && !waist.equals("") && !neck.equals("")){
            editor.putString("age", age);
            editor.putString("height", height);
            editor.putString("weight", weight);
            editor.putString("waist", waist);
            editor.putString("neck", neck);
            editor.putString("sex", sex);
            editor.putString("sport", sport);
            editor.putString("currentDate", currentDate);
            editor.commit(); //提交数据保存至config文件
        }
    }

    /**
     * 存储数据 并添加关联
     */
    private void saveToBmob() {
        User_Message user_message = new User_Message();
        if (!mAge.getText().toString().equals("") &&
                !mHeight.getText().toString().equals("") &&
                !mWeight.getText().toString().equals("") &&
                !mWaist.getText().toString().equals("") &&
                !mNeck.getText().toString().equals("")){
            user_message.setAge(mAge.getText().toString());
            user_message.setHeight(mHeight.getText().toString());
            user_message.setWeight(mWeight.getText().toString());
            user_message.setWaist(mWaist.getText().toString());
            user_message.setNeck(mNeck.getText().toString());
            user_message.setCurrentDate(CurrentDate);
            if (man.isChecked()) {
                user_message.setSex(MAN) ;
            } else {
                user_message.setSex(WOMAN) ;
            }
            if (high.isChecked()) {
                user_message.setSport(HIGH);
            } else if (middle.isChecked()) {
                user_message.setSport(MIDDLE) ;
            } else {
                user_message.setSport(LOW) ;
            }

            user_message.setAuthor(BmobUser.getCurrentUser(MyUsers.class));
            user_message.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null){
                        Log.d("saveToBmob","okokokokoko");
                    }else {
                        Log.e("saveToBmob",e.toString());
                    }
                }
            });
        }
    }

    /**
     * 查询检测
     */
    private void query(){
        BmobQuery<User_Message> query = new BmobQuery<>();
        query.addWhereEqualTo("author", BmobUser.getCurrentUser(MyUsers.class));
        query.order("-updatedAt");
        //包含作者信息
        query.include("author");
        query.findObjects(new FindListener<User_Message>() {

            @Override
            public void done(List<User_Message> object, BmobException e) {
                if (e == null) {
                    //object.size();
                    for (User_Message user_message : object){
                        user_message.getAge();
                        user_message.getHeight();
                        user_message.getWaist();
                        user_message.getWeight();
                        user_message.getNeck();
                        user_message.getCurrentDate();
                        Log.d("年龄", user_message.getAge());
                    }
                    Log.d("queryFromBmob","查询成功");
                } else {
                    Log.e("FromBmob", e.toString());
                }
            }

        });

    }

    public void querySql(){
        Cursor cursor = mSQL1.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0){
            Log.e("cursor的大小", String.valueOf(cursor.getCount()));
        }
    }
}
