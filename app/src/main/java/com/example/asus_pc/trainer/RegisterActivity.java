package com.example.asus_pc.trainer;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.asus_pc.trainer.Me_Activty.BMIActivity;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.until.ActivityCollector;
import com.example.asus_pc.trainer.until.BaseCompatActivity;
import com.jaeger.library.StatusBarUtil;

import java.util.concurrent.TimeUnit;

import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

public class RegisterActivity extends BaseCompatActivity {
    private EditText userName;
    private EditText password;
    private EditText phone;
    private Button get_code;
    private EditText code;
    private Button register_btn;
    private DBHelper mDBHelper;
    private SQLiteDatabase mSQL, mSQL2;

    private Subscription mSubscription = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarUtil.setTransparent(RegisterActivity.this);

        ActivityCollector.addActivity(this);

        //初始化Bmob
        Bmob.initialize(this, "5f0a55cbd319099d5f48f6b952cb17fc");
        BmobSMS.initialize(this, "5f0a55cbd319099d5f48f6b952cb17fc");

        //初始化控件
        initView();
        initListener();

        //数据库
        mDBHelper = new DBHelper(getApplicationContext());
        mSQL = mDBHelper.getWritableDatabase();
        mSQL2 = mDBHelper.getReadableDatabase();
    }

    private void initView() {
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.tel_num);
        get_code = findViewById(R.id.get_code);
        code = findViewById(R.id.code);
        register_btn = findViewById(R.id.register_btn);
    }

    private void initListener() {
        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBmobSMSCode();
                getCode();


            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser();
                verifyBmobSMSCode();
                uploadUserInfo();
                saveUserID_Ps();
                saveUserMsgToSP();
                EnterLineActvity();


            }
        });
    }

    /**
     * 获取短信验证码
     */
    private void getBmobSMSCode() {
        String tel_phone = phone.getText().toString();
        Log.e("电话", tel_phone);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            BmobSMS.requestSMSCode(RegisterActivity.this, tel_phone, "Trainer", new RequestSMSCodeListener() {
                @Override
                public void done(Integer integer, BmobException e) {
                    if (e == null) {
                        Log.e("bmob", "短信ID：" + integer);
                    } else {
                        Log.e("bmob", "验证码发送失败" + e.getMessage());
                    }
                }
            });
        }
    }

    private void verifyBmobSMSCode() {
        BmobSMS.verifySmsCode(RegisterActivity.this, phone.getText().toString(), code.getText().toString(), new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("bomb", "验证通过");
                }
            }
        });
    }

    /**
     * 验证用户设置账户、密码是否符合规格
     */
    private void verifyUser() {
        String user = userName.getText().toString();
        String pass = password.getText().toString();
        String tel = phone.getText().toString();

        ToastShow toastShow = new ToastShow();

        if (user.isEmpty()) {
            toastShow.toastShow(this, "用户名不能为空");
        } else if (user.length() > 6) {
            toastShow.toastShow(this, "用户名不能超过6位");
        }

        if (pass.isEmpty()) {
            toastShow.toastShow(this, "密码不能为空");
        } else if (pass.length() < 6) {
            toastShow.toastShow(this, "密码不能少于6位");
        }

        if (tel.length() != 11) {
            toastShow.toastShow(this, "请输入11位手机号码");
        }
    }

    /**
     * 获取验证码按钮上面的倒计时
     */
    private void getCode() {
        final int count = 59;
        Observable.interval(0, 1, TimeUnit.SECONDS)//设置0延迟，每隔一秒发送一条数据
                .take(count) //设置循环次数
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return count - aLong; //
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        get_code.setEnabled(false);//在发送数据的时候设置为不能点击

                        get_code.setTextColor(Color.GRAY);//字体颜色设为灰色
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.d("TAG", "onCompleted: ");
                        get_code.setText("点击重发");//数据发送完后设置为原来的文字
                        get_code.setTextColor(Color.WHITE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) { //接受到一条就是会操作一次UI
                        Log.d("TAG", "onNext: " + aLong);
                        get_code.setText(aLong + "s后重发");
                        get_code.setEnabled(true);
                        get_code.setTextColor(Color.GRAY);
                    }
                });
    }

    /**
     * 上传用户信息
     */
    private void uploadUserInfo() {
        String user = userName.getText().toString();
        String pass = password.getText().toString();
        String tel = phone.getText().toString();

        MyUsers myUsers = new MyUsers();
        myUsers.setUsername(user);
        myUsers.setPassword(pass);
        myUsers.setPhone(tel);
        Log.e("TAG", user + pass + tel);
        Log.e("输出", myUsers.toString());
        myUsers.signUp(new SaveListener<MyUsers>() {
            @Override
            public void done(MyUsers myUsers, cn.bmob.v3.exception.BmobException e) {
                ToastShow toastShow = new ToastShow();
                if (e == null) {
                    toastShow.toastShow(RegisterActivity.this, "注册成功");
                } else {
                    toastShow.toastShow(RegisterActivity.this, "注册失败，请重试！");
                    Log.e("错误", e.getMessage());
                }
            }
        });
    }

    /**
     * 注册成功，进入主界面
     */
    private void EnterLineActvity() {
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(RegisterActivity.this, LineShowActivity.class));
                finish();
            }
        }, 1000);
        finish();
    }

    /**
     * 将用户注册信息存入数据库
     */
    private void saveUserID_Ps() {
        String user = userName.getText().toString();
        String pass = password.getText().toString();
        String tel = phone.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put("User_ID", user);
        contentValues.put("PassWd", pass);
        contentValues.put("Tel", tel);
        mSQL.insert(DBHelper.TABLE_NAME_USER, null, contentValues);
    }

    private void  saveUserMsgToSP(){
        String user = userName.getText().toString();
        String pass = password.getText().toString();
        String tel = phone.getText().toString();
        SharedPreferences preferences = getSharedPreferences("UserMsg",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id",user);
        editor.putString("user_pass",pass);
        editor.putString("user_tel",tel);
        editor.commit();
    }

}

















