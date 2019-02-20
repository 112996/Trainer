package com.example.asus_pc.trainer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.asus_pc.trainer.Me_Activty.BMIActivity;
import com.example.asus_pc.trainer.R;
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
    private CheckBox cbPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setTransparent(LoginActivity.this);
        Bmob.initialize(this, "5f0a55cbd319099d5f48f6b952cb17fc");

        initView();
    }

    private void initView() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
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
                            b.toastShow(LoginActivity.this, "登录成功");
                            startActivity(new Intent(LoginActivity.this, LineShowActivity.class));
                            finish();
                        } else {
                            b.toastShow(LoginActivity.this, "账户或密码错误，请重试！");
                            Log.e("错误", e.getMessage());
                        }
                    }
                });
                saveUserMsgToSP();
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
    }
}

















