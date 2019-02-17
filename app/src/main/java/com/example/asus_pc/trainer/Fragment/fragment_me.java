package com.example.asus_pc.trainer.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.asus_pc.trainer.CircleImageView;
import com.example.asus_pc.trainer.Me_Activty.BFRActivity;
import com.example.asus_pc.trainer.Me_Activty.BMIActivity;
import com.example.asus_pc.trainer.Me_Activty.BMRActivity;
import com.example.asus_pc.trainer.Me_Activty.WhtrActivity;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.ToastShow;

import java.io.FileNotFoundException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import io.reactivex.annotations.Nullable;


public class fragment_me extends Fragment {
    private  TextView user_ID;
    private View mView;
    private Button logout, BMI, BFR, BMR, whtr, choosePhoto, cancel, Msg;
    private  ImageButton user_compile;
    private CircleImageView user_protrait;
    private Dialog dialog;
    private View inflate;
    private String path;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_me_layout, container, false);
        }
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bmob.initialize(getActivity(), "5f0a55cbd319099d5f48f6b952cb17fc");

        init();
        startToActivity();
        logout();
    }

    /**
     * 初始化控件
     */
    private void init() {
        logout = mView.findViewById(R.id.logout);
        BMI = mView.findViewById(R.id.BMI);
        BFR = mView.findViewById(R.id.BFR);
        BMR = mView.findViewById(R.id.BMR);
        whtr = mView.findViewById(R.id.whtr);
        user_protrait = mView.findViewById(R.id.user_portrait);
        user_ID = mView.findViewById(R.id.user_ID);
        user_compile = mView.findViewById(R.id.user_compile);
        Msg = mView.findViewById(R.id.Msg);

        user_protrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
                inflate = LayoutInflater.from(getActivity()).inflate(R.layout.portrait_dialog_layout, null);
                choosePhoto = inflate.findViewById(R.id.choosePhoto);
                cancel = inflate.findViewById(R.id.btn_cancel);
                choosePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectPic(view);
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                showDialog();
            }
        });

        user_compile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View mView = View.inflate(getActivity(), R.layout.alertdialog_layout, null);
                builder.setView(mView);
                builder.setCancelable(true);
                final EditText input_ID = view.findViewById(R.id.change_ID);  //修改ID
                Button btn_OK = view.findViewById(R.id.alertDialog_OK);  //确认修改按钮
                Button btn_cancel = view.findViewById(R.id.alertDialog_cancel);  //取消修改按钮
                final AlertDialog dialog = builder.create();
                dialog.show();
                btn_OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (input_ID.getText() != null){
                            user_ID.setText(input_ID.getText().toString());
                        }else{
                            ToastShow ts = new ToastShow();
                            ts.toastShow(getActivity(),"还未更改ID名！");
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }














    /**
     * 底部显示选择照片
     */
    private void showDialog() {
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        int width = getResources().getDisplayMetrics().widthPixels;
        lp.width = width;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * 打开系统相册并选择图片
     */
    public void selectPic(View view) {
        Intent intent = new Intent("android.intent.action.PICK ");
        intent.setType("image/*");
        getActivity().startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            path = getImagePath(uri, null);
            ContentResolver cr = getActivity().getContentResolver();   //在fragment中无法直接使用getContentResolver()类，需要先获取到activity。即getActivity().getContentResolver().
            try {
                Log.e("qwe", path.toString());
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                /* 将Bitmap设定到ImageView */
                user_protrait.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 跳转到身体指数界面
     */
    private void startToActivity() {
        Msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UserMsgActivity.class));
            }
        });

        BMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BMIActivity.class));
            }
        });

        BFR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BFRActivity.class));
            }
        });
        BMR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BMRActivity.class));
            }
        });
        whtr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), WhtrActivity.class));
            }
        });
    }

    /**
     * 退出账号
     */
    private void logout() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("com.example.asus_pc.trainer.logout");
                //sendBroadcast(i);
                BmobUser.logOut();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) mView.getParent()).removeView(mView);
    }
}


