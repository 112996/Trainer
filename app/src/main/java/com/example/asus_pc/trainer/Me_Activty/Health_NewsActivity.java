package com.example.asus_pc.trainer.Me_Activty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.adapter.Health_list_recy_adapter;
import com.example.asus_pc.trainer.bean.HealthBean;
import com.example.asus_pc.trainer.bean.JsoupBean;
import com.example.asus_pc.trainer.db.DBHelper;
import com.example.asus_pc.trainer.until.CheckNetwork;
import com.example.asus_pc.trainer.until.GetJsonFromTianAndParse;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

public class Health_NewsActivity extends Activity {

    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.media_name)
    TextView mediaName;
    @BindView(R.id.ctime)
    TextView ctime;
    @BindView(R.id.rel)
    RelativeLayout rel;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.btn_health_list)
    Button btnHealthList;
    @BindView(R.id.content)
    WebView content;


    private GetJsonFromTianAndParse get;
    private HealthBean healthBean;
    private boolean flag, btnOk_flag = true;
    private DBHelper dbHelper;
    private SQLiteDatabase w_sql, r_sql;
    private ContentValues cv = new ContentValues();
    private ArrayList<String> list = new ArrayList();


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GetJsonFromTianAndParse.PARSESUCCESS:
                    healthBean = (HealthBean) msg.obj;
                    initData(healthBean);
                    break;
                case GetJsonFromTianAndParse.FAILED:

            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train__log);
        StatusBarUtil.setTransparent(Health_NewsActivity.this);
        ButterKnife.bind(this);
        Bmob.initialize(this, "5f0a55cbd319099d5f48f6b952cb17fc");

        flag = CheckNetwork.getInfo(this);
        if (flag) {
            get = new GetJsonFromTianAndParse(mHandler);
            get.getJsonFromTian();
        } else {
            tv.setVisibility(View.VISIBLE);
            rel.setVisibility(View.GONE);
            linear.setVisibility(View.GONE);
        }

    }


    @SuppressLint("JavascriptInterface")
    private void initData(final HealthBean healthBean) {
        title.setText(healthBean.getTitle());

        mediaName.setText(healthBean.getDescription());
        ctime.setText(healthBean.getCtime());

        aboutWebView(healthBean.getUrl());

        dbHelper = new DBHelper(this);
        w_sql = dbHelper.getWritableDatabase();
        r_sql = dbHelper.getReadableDatabase();

        cv.put("title", healthBean.getTitle());
        cv.put("description", healthBean.getDescription());
        cv.put("ctime", healthBean.getCtime());
        cv.put("url", healthBean.getUrl());

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnOk_flag) {
                    Drawable tihaun = getResources().getDrawable(R.drawable.health_add_clicked);
                    tihaun.setBounds(0, 0, tihaun.getMinimumWidth(), tihaun.getMinimumHeight());
                    btnOk.setCompoundDrawables(tihaun, null, null, null);
                    btnOk_flag = false;
                    w_sql.insert(DBHelper.TABLE_NAME_HEALTH_INFORMATION, null, cv);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("cancel", "cancel");
                Cursor cursor = r_sql.query(DBHelper.TABLE_NAME_HEALTH, null, null, null, null, null, null);
                Log.e("cancel", String.valueOf(cursor.getCount()));
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        Log.e("cancel", title);
                        if (title.equals(healthBean.getTitle())) {
                            list.remove(title);
                            w_sql.delete(DBHelper.TABLE_NAME_HEALTH_INFORMATION, "title=?", new String[]{title});
                        }
                    }
                }
                cursor.close();
                btnOk_flag = true;
                Drawable tihaun = getResources().getDrawable(R.drawable.health_add);
                tihaun.setBounds(0, 0, tihaun.getMinimumWidth(), tihaun.getMinimumHeight());
                btnOk.setCompoundDrawables(tihaun, null, null, null);
            }
        });

        btnHealthList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Health_NewsActivity.this, R.style.DialogNobg);
                View mView = View.inflate(Health_NewsActivity.this, R.layout.health_alertdailog_layout, null);
                builder.setView(mView);
                builder.setCancelable(true);
                RecyclerView recy = mView.findViewById(R.id.health_list_recy);  //修改ID
                final AlertDialog dialog = builder.create();
                dialog.show();

                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = 800;
                params.height = 600;
                dialog.getWindow().setAttributes(params);

                Cursor cursor = r_sql.query(DBHelper.TABLE_NAME_HEALTH_INFORMATION, new String[]{"title"}, null, null, null, null, null);
                Log.e("list", String.valueOf(cursor.getCount()));
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        list.add(title);
                    }
                }else{
                    Log.e("没有数据","未插入成功" );
                }
                cursor.close();

                recy.setLayoutManager(new LinearLayoutManager(Health_NewsActivity.this));
                Health_list_recy_adapter recyAdapter = new Health_list_recy_adapter(list);
                Log.e("list", String.valueOf(list));
                recyAdapter.notifyDataSetChanged();
                recy.setAdapter(recyAdapter);

                recyAdapter.setOnItemClickListener(new Health_list_recy_adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String list_title = list.get(position);
                        Cursor cursor = r_sql.query(DBHelper.TABLE_NAME_HEALTH_INFORMATION, null, "title=?", new String[]{list_title}, null, null, null);
                        while (cursor.moveToNext()) {
                            title.setText(cursor.getString(cursor.getColumnIndex("title")));
                            aboutWebView(cursor.getString(cursor.getColumnIndex("url")));
                            mediaName.setText(cursor.getString(cursor.getColumnIndex("description")));
                            ctime.setText(cursor.getString(cursor.getColumnIndex("ctime")));
                            dialog.dismiss();
                        }
                    }
                });

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);
            }
        });

    }

    @SuppressLint("JavascriptInterface")
    private void aboutWebView(String url){
        content.loadUrl(url);
        content.addJavascriptInterface(this, "android");
        content.setWebViewClient(webViewClient);
        content.setWebChromeClient(webChromeClient);

        WebSettings webSettings = content.getSettings();
        webSettings.setJavaScriptEnabled(true); // 允许使用js

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //不使用缓存，只从网络获取
        webSettings.setSupportZoom(true); //支持屏幕缩放
        webSettings.setBuiltInZoomControls(true);
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) { //页面加载完成，进度条消失
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {  //页面开始加载
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    };
    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) { //获取网页标题
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {  //加载进度条回调
        }

    };

}
