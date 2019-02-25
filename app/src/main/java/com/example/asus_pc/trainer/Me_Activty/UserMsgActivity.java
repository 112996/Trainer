package com.example.asus_pc.trainer.Me_Activty;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.asus_pc.trainer.DBHelper;
import com.example.asus_pc.trainer.LineShowActivity;
import com.example.asus_pc.trainer.MyUsers;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.RegisterActivity;
import com.example.asus_pc.trainer.ToastShow;
import com.example.asus_pc.trainer.User_Message;
import com.example.asus_pc.trainer.UsersMsg;
import com.example.asus_pc.trainer.until.ActivityCollector;
import com.jaeger.library.StatusBarUtil;

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
    private ImageButton user_back_btn;
    private Button ok;
    private DBHelper userDBHelper;
    private SQLiteDatabase mSQL, mSQL1;
    private String MAN = "男", WOMAN = "女";
    private String HIGH = "高", MIDDLE = "中", LOW = "低";


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
        user_back_btn = findViewById(R.id.user_back_btn);
        user_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserMsgActivity.this, LineShowActivity.class);
                i.putExtra("id", 2);
                startActivity(i);
                finish();
            }
        });
        ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDatas();
                saveToBmob();
                query();
            }
        });
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
        saveConfig(UserMsgActivity.this, mAge.getText().toString(), mHeight.getText().toString(), mWeight.getText().toString(), mWaist.getText().toString(), mNeck.getText().toString(), add_sex, add_sport);
        ToastShow b = new ToastShow();
        b.toastShow(UserMsgActivity.this, "添加成功！");
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
        contentValues.put("age", usersMsg.age);
        contentValues.put("height", usersMsg.height);
        contentValues.put("weight", usersMsg.weight);
        contentValues.put("waist", usersMsg.waist);
        contentValues.put("neck", usersMsg.neck);
        contentValues.put("sex", usersMsg.sex);
        contentValues.put("sport", usersMsg.sport);
        return contentValues;
    }

    /**
     * 存在sharePreference
     */
    public void saveConfig(Context context, String age, String height, String weight, String waist, String neck, String sex, String sport) {
        SharedPreferences s = context.getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putString("age", age);
        editor.putString("height", height);
        editor.putString("weight", weight);
        editor.putString("waist", waist);
        editor.putString("neck", neck);
        editor.putString("sex", sex);
        editor.putString("sport", sport);
        editor.commit(); //提交数据保存至config文件
    }

    /**
     * 存储数据 并添加关联
     */
    private void saveToBmob() {
        User_Message user_message = new User_Message();
        user_message.setAge(mAge.getText().toString());
        user_message.setHeight(mHeight.getText().toString());
        user_message.setWeight(mWeight.getText().toString());
        user_message.setWaist(mWaist.getText().toString());
        user_message.setNeck(mNeck.getText().toString());
        if (man.isChecked()) {
            user_message.setSex(MAN) ;
        } else {
            user_message.setSex(WOMAN) ;
        }
        if (high.isChecked()) {
            user_message.setSport(HIGH); ;
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
                    Log.d("queryFromBmob","okokokokoko");
                } else {
                    Log.e("FromBmob", e.toString());
                }
            }

        });

    }
}
