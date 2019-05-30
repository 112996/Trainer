package com.example.asus_pc.trainer.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
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
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.asus_pc.trainer.bean.MyUsers;
import com.example.asus_pc.trainer.until.CircleImageView;
import com.example.asus_pc.trainer.db.DBHelper;
import com.example.asus_pc.trainer.Me_Activty.BFRActivity;
import com.example.asus_pc.trainer.Me_Activty.BMIActivity;
import com.example.asus_pc.trainer.Me_Activty.BMRActivity;
import com.example.asus_pc.trainer.Me_Activty.Health_NewsActivity;
import com.example.asus_pc.trainer.Me_Activty.UserMsgActivity;
import com.example.asus_pc.trainer.Me_Activty.WhtrActivity;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.until.GetBitmapFromUri;
import com.example.asus_pc.trainer.until.ToastShow;
import com.example.asus_pc.trainer.until.ActivityCollector;
import com.example.asus_pc.trainer.until.CleanCache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.reactivex.annotations.Nullable;
import okhttp3.internal.http.RetryableSink;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class fragment_me extends Fragment {
    private TextView user_ID, trainer_ID, cache;
    private View mView;
    private Button logout, clear_cache, BMI, BFR, BMR, whtr, choosePhoto, takePhoto, cancel, Msg, train_log;
    private ImageButton user_compile;
    private CircleImageView user_protrait;
    private Dialog dialog;
    private View inflate;
    private File file;
    private Uri mUri;
    public final int TYPE_TAKE_PHOTO = 1;//Uri获取类型判断
    public final int CODE_TAKE_PHOTO = 1;//相机RequestCode
    public final int CODE_SELECT_IMAGE = 2;//相册RequestCode
    private DBHelper mDBHelper;
    private SQLiteDatabase mSQL, mSQL2;
    private String username, nickname, picPath;

    public Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
//                    Toast.makeText(getActivity(), "清理完成", Toast.LENGTH_SHORT).show();
                    ToastShow ts = new ToastShow();
                    ts.toastShow(getActivity(), "清理完成！");
                    try {
                        cache.setText(CleanCache.getTotalCacheSize(getActivity()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    };

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

        mDBHelper = new DBHelper(getActivity().getApplicationContext());
        mSQL = mDBHelper.getReadableDatabase();
        mSQL2 = mDBHelper.getWritableDatabase();

        init();
        startToActivity();
        logout();
    }

    /**
     * 初始化控件
     */
    private void init() {
        logout = mView.findViewById(R.id.logout);
        clear_cache = mView.findViewById(R.id.clear_cache);
        cache = mView.findViewById(R.id.cache);
        BMI = mView.findViewById(R.id.BMI);
        BFR = mView.findViewById(R.id.BFR);
        BMR = mView.findViewById(R.id.BMR);
        whtr = mView.findViewById(R.id.whtr);
        user_protrait = mView.findViewById(R.id.user_portrait);
        user_compile = mView.findViewById(R.id.user_compile);
        Msg = mView.findViewById(R.id.Msg);
        train_log = mView.findViewById(R.id.train_log);

        try {
            cache.setText(CleanCache.getTotalCacheSize(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Clean()).start();
            }
        });

        //进入应用时的user_id和trainer号
        user_ID = mView.findViewById(R.id.user_ID); //昵称
        trainer_ID = mView.findViewById(R.id.trainer_ID);  //用户名
        getUserAndNick();


        //头像显示
        SharedPreferences s = getActivity().getSharedPreferences("UserMsg", MODE_PRIVATE);
        String resImageBase64 = s.getString("savePortrait", "");
        if (!resImageBase64.isEmpty()) {
            byte[] base64byte = Base64.decode(resImageBase64, Base64.DEFAULT);
            ByteArrayInputStream instream = new ByteArrayInputStream(base64byte);
            user_protrait.setImageDrawable(Drawable.createFromStream(instream, "res_img"));
        }else{
            getPicFromBmob();
        }


        //修改头像
        user_protrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
                inflate = LayoutInflater.from(getActivity()).inflate(R.layout.portrait_dialog_layout, null);
                choosePhoto = inflate.findViewById(R.id.choosePhoto);
                takePhoto = inflate.findViewById(R.id.takePhoto);
                cancel = inflate.findViewById(R.id.btn_cancel);
                choosePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openGallery(view);
                        dialog.dismiss();
                    }
                });
                takePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        takePhoto();
                        dialog.dismiss();
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

        //修改昵称
        user_compile.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogNobg);
                View mView = View.inflate(getActivity(), R.layout.alertdialog_layout, null);
                builder.setView(mView);
                builder.setCancelable(true);
                final EditText input_ID = mView.findViewById(R.id.change_ID);  //修改ID
                Button btn_OK = mView.findViewById(R.id.alertDialog_OK);  //确认修改按钮
                Button btn_cancel = mView.findViewById(R.id.alertDialog_cancel);  //取消修改按钮
                final AlertDialog dialog = builder.create();
                dialog.show();
                btn_OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!input_ID.getText().toString().isEmpty()) {
                            user_ID.setText(input_ID.getText().toString()); //显示昵称
                            updateUser(input_ID.getText().toString());

                        } else {
                            ToastShow ts = new ToastShow();
                            ts.toastShow(getActivity(), "还未更改ID名！");
                        }
                        dialog.dismiss();

                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(false);//返回键不消失
                dialog.setCanceledOnTouchOutside(false);//点击屏幕其他地方不消失
            }
        });
    }

    /**
     * 底部显示选择
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
     * 拍照
     */
    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            // 步骤一：创建存储照片的文件
            file = new File(Environment.getExternalStorageDirectory() + File.separator + "pictures", "portrait.jpg");
            Log.e("file", String.valueOf(file));
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //步骤二：Android 7.0及以上获取文件 Uri
                String p = getActivity().getPackageName() + ".FileProvider";
                mUri = FileProvider.getUriForFile(getActivity(), p, file);
                Log.e("mUri", String.valueOf(mUri));
            } else {
                //步骤三：获取文件Uri
                mUri = Uri.fromFile(file);
            }
            //步骤四：调取系统拍照
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            startActivityForResult(intent, CODE_TAKE_PHOTO);
        }
    }

    /**
     * 打开系统相册并选择图片
     */
    public void openGallery(View view) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(albumIntent, CODE_SELECT_IMAGE);
        }
    }


     @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode != RESULT_OK){
            return;
        }
        switch (requestCode) {
            case CODE_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    selectPic(data);
                    savePortraitToSP();
                }
                break;
            case CODE_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    //user_protrait.setImageURI(mUri);  //直接设置会因为原始Bitmap过大而造成OOM,需要进行压缩，（质量压缩法）
                    Bitmap bm = null;
                    try {
                        bm = GetBitmapFromUri.getInstance().getBitmapFormUri(getActivity(), mUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    user_protrait.setImageBitmap(bm);
                    savePortraitToSP();
                    uploadPic(String.valueOf(file));
                }
        }

    }

    public String bitmapToString(Bitmap bitmap){
        if (bitmap == null){
            return  null;
        }
        //将Bitmap转换成字符串
        String string=null;
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[]bytes=bStream.toByteArray();
        string=Base64.encodeToString(bytes,Base64.DEFAULT);
        return string;
    }

    /**
     * 选择图片
     *
     * @param intent
     */
    private void selectPic(Intent intent) {
        if (intent == null || intent.getData() == null){
            return;
        }
        Uri selectImageUri = intent.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectImageUri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        Log.e("选择路径", picturePath);
        cursor.close();
        user_protrait.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        uploadPic(picturePath);
    }


    /**
     * 保存头像到SP
     */
    private void savePortraitToSP() {
        SharedPreferences preferences = getActivity().getSharedPreferences("UserMsg", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ((BitmapDrawable) user_protrait.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 50, stream);
        String imgBase64 = new String(Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT));
        editor.putString("savePortrait", imgBase64);
        editor.commit();
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
        train_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Health_NewsActivity.class));
            }
        });
    }

    /**
     * 退出登录
     */
    private void logout() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("com.example.asus_pc.trainer.logout");
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserMsg", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                ActivityCollector.finishAll();
            }
        });
    }


    /**
     * 保存头像到数据库
     */
    private void savePortraitToSQLite() {
        //将图片转换为字节
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) user_protrait.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] img = stream.toByteArray();

        mDBHelper = new DBHelper(getActivity().getApplicationContext());
        mSQL2 = mDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Portrait_uri", img);
        mSQL2.insert(mDBHelper.TABLE_NAME_USER, null, contentValues);
    }

    /**
     * 实现Runnable完成清空缓存
     * 内部类
     */
    class Clean implements Runnable {
        @Override
        public void run() {
            CleanCache.clearAllCache(getActivity());
            try {
                Thread.sleep(1500);
                if (CleanCache.getTotalCacheSize(getActivity()).startsWith("0")) ;
                handler.sendEmptyMessage(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新保存nickname到服务器
     *
     * @param nickname
     */
    private void updateUser(String nickname) {
        if (nickname == null){
            return;
        }
        MyUsers myUsers = BmobUser.getCurrentUser(MyUsers.class);
        myUsers.setNickname(nickname);
        myUsers.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("更新成功", "success");
                } else {
                    Log.e("更新失败", e.toString());
                }
            }
        });
    }

    /**
     * 从服务器获取昵称和Id
     */
    private void getUserAndNick(){
        MyUsers myUsers = BmobUser.getCurrentUser(MyUsers.class);
        nickname = myUsers.getNickname();
        username = myUsers.getUsername();



        if (!username.isEmpty()) {
                trainer_ID.setText("trainer号：" + username);
            } else {
                Log.e("trainer号", "为空");
            }
        if (nickname == null ){
            user_ID.setText("昵称");
        }else{
            user_ID.setText(nickname);
        }

    }

    /**
     * sp里面没有数据就从服务器拿头像
     */
    private void getPicFromBmob(){
        MyUsers myUsers = BmobUser.getCurrentUser(MyUsers.class);
        picPath = myUsers.getPic();

        if (!picPath.isEmpty()){
            Glide.with(getActivity())
                    .load(picPath)
                    .placeholder(R.drawable.item_time)
                    .dontAnimate()
                    .thumbnail(0.1f)
                    .into(user_protrait);
        }else{
            Log.e("trainer号", picPath);
        }
    }



    private void uploadPic(String path){
        if (path == null){
            return;
        }
        final BmobFile file = new BmobFile(new File(path));
        file.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e== null){
                    afterUploadPic(file.getFileUrl());
                }else{
                    Log.e("上传pic失败", e.toString());
                }
            }
        });
    }

    private void afterUploadPic(String path){
        if (path == null){
            return;
        }
        MyUsers myUsers = BmobUser.getCurrentUser(MyUsers.class);
        myUsers.setPic(path);
        myUsers.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {

                } else {
                    Log.e("更新hhhh失败", e.toString());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //savePortraitToSQLite(); 最开始未设置头像，这里会出错，显示bitmap是个空对象
        ((ViewGroup) mView.getParent()).removeView(mView);
    }

}


