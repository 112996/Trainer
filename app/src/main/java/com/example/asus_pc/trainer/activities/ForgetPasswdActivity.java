package com.example.asus_pc.trainer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.asus_pc.trainer.bean.MyUsers;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.until.ToastShow;
import com.example.asus_pc.trainer.until.ActivityCollector;
import com.jaeger.library.StatusBarUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

public class ForgetPasswdActivity extends AppCompatActivity {
    private EditText tel_num, code;
    private Button get_code;
    private ImageButton forget_passwd_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_passwd);
        StatusBarUtil.setTransparent(ForgetPasswdActivity.this);

        ActivityCollector.addActivity(this);

        Bmob.initialize(this, "5f0a55cbd319099d5f48f6b952cb17fc");
        BmobSMS.initialize(this, "5f0a55cbd319099d5f48f6b952cb17fc");

        initViews();
    }

    private void initViews(){
        tel_num = findViewById(R.id.tel_num);
        code = findViewById(R.id.code);
        get_code = findViewById(R.id.get_code);
        forget_passwd_enter = findViewById(R.id.forget_passwd_enter);
        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBmobSMSCode();
                getCode();


            }
        });
        forget_passwd_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });
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
     * 获取短信验证码
     */
    private void getBmobSMSCode() {
        String tel_phone = tel_num.getText().toString();
        Log.e("电话", tel_phone);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            BmobSMS.requestSMSCode(ForgetPasswdActivity.this, tel_phone, "Trainer", new RequestSMSCodeListener() {
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

    /**
     * 确认手机号
     */
    private void verify(){
        final BmobQuery<MyUsers> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<MyUsers>() {
            @Override
            public void done(List<MyUsers> list, cn.bmob.v3.exception.BmobException e) {
                ToastShow b = new ToastShow();
                if (e == null) {
                    verifyBmobSMSCode();

                } else {
                    b.toastShow(ForgetPasswdActivity.this, "错误，请重试！");
                    Log.e("错误", e.getMessage());
                }
            }
            /*@Override
            public void done(List<MyUsers> object, BmobException e) {
                if (e == null) {
                    Snackbar.make(view, "查询成功", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, "查询失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }*/
        });

    }

    private void verifyBmobSMSCode() {
        BmobSMS.verifySmsCode(ForgetPasswdActivity.this, tel_num.getText().toString(), code.getText().toString(), new VerifySMSCodeListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    startActivity(new Intent(ForgetPasswdActivity.this, LineShowActivity.class));
                    finish();
                    Log.e("bomb", "验证通过");
                }
            }
        });
    }
}
