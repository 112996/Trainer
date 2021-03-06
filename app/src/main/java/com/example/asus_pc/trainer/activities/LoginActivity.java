package com.example.asus_pc.trainer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.until.ToastShow;
import com.example.asus_pc.trainer.until.ActivityCollector;
import com.example.asus_pc.trainer.until.BaseCompatActivity;
import com.jaeger.library.StatusBarUtil;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseCompatActivity {
    private EditText username;
    private EditText password;
    private Button login_btn;
    private TextView passwd_forget;
    private CheckBox cbPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setTransparent(LoginActivity.this);
        Bmob.initialize(this, "5f0a55cbd319099d5f48f6b952cb17fc");

        ActivityCollector.addActivity(this);

        initView();
    }

    private void initView() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        passwd_forget = findViewById(R.id.password_forget);
        cbPassword = findViewById(R.id.cbPassword);

        cbPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbPassword.isChecked()) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser bmobUser = new BmobUser();
                bmobUser.setUsername(username.getText().toString());
                bmobUser.setPassword(password.getText().toString());
                bmobUser.login(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        ToastShow b = new ToastShow();
                        if (e == null) {
                            saveUserMsgToSP();
                            startActivity(new Intent(LoginActivity.this, LineShowActivity.class));
                            finish();
                        } else {
                            b.toastShow(LoginActivity.this, "账户或密码错误，请重试！");
                            Log.e("错误", e.getMessage());
                        }
                    }
                });
            }
        });

        passwd_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswdActivity.class));
                //根据手机号验证登录
            }
        });

    }

    private void saveUserMsgToSP() {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        SharedPreferences preferences = getSharedPreferences("UserMsg", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id", user);
        editor.putString("user_pass", pass);
        editor.commit();
        Log.e("存储成功", user+pass);
    }
}

















