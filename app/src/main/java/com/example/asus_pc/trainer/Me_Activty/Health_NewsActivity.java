package com.example.asus_pc.trainer.Me_Activty;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.asus_pc.trainer.Fragment.Fragment_Health_New;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.adapter.Health_list_recy_adapter;
import com.example.asus_pc.trainer.bean.HealthBean;
import com.example.asus_pc.trainer.db.DBHelper;
import com.example.asus_pc.trainer.until.CheckNetwork;
import com.example.asus_pc.trainer.until.GetJsonFromTianAndParse;
import com.example.asus_pc.trainer.until.ViewPagerAdapter;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

public class Health_NewsActivity extends FragmentActivity {

    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.title)
    TextView title;
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
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    private GetJsonFromTianAndParse get;
    private List<HealthBean> healthBeanList;
    private boolean flag, btnOk_flag = true;
    private DBHelper dbHelper;
    private SQLiteDatabase w_sql, r_sql;
    private ContentValues cv = new ContentValues();
    private ArrayList<String> list = new ArrayList();
    private List<Fragment> fragmentList = new ArrayList<>();


    private String url;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GetJsonFromTianAndParse.PARSESUCCESS:
                    healthBeanList = (List<HealthBean>) msg.obj;
                    initData(healthBeanList);
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

        for(int i = 0; i < 10; i++){
            fragmentList.add(new Fragment_Health_New());
        }

        flag = CheckNetwork.getInfo(this);
        if (flag) {
            get = new GetJsonFromTianAndParse(mHandler);
            get.getJsonFromTian();
        } else {
            tv.setVisibility(View.VISIBLE);
            rel.setVisibility(View.GONE);
            linear.setVisibility(View.GONE);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, false);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    case 8:

                        break;
                    case 9:

                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    @SuppressLint("JavascriptInterface")
    private void initData(final List<HealthBean> healthBeanList) {
        url = healthBeanList.get(0).getUrl();
        title.setText(healthBeanList.get(0).getTitle());

        mediaName.setText(healthBeanList.get(0).getDescription());
        ctime.setText(healthBeanList.get(0).getCtime());

//        aboutWebView(list.get(0).getUrl());
        Fragment fragment = fragmentList.get(0);
        Bundle bundle = new Bundle();
        bundle.putString("url", healthBeanList.get(0).getUrl());
        Log.e("url", healthBeanList.get(0).getUrl());
        fragment.setArguments(bundle);

//        android.app.FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.viewPager,fragment);
//        fragmentTransaction.commit();
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewPager,fragment);
        fragmentTransaction.commit();



        dbHelper = new DBHelper(this);
        w_sql = dbHelper.getWritableDatabase();
        r_sql = dbHelper.getReadableDatabase();

        cv.put("title", healthBeanList.get(0).getTitle());
        cv.put("description", healthBeanList.get(0).getDescription());
        cv.put("ctime", healthBeanList.get(0).getCtime());
        cv.put("url", healthBeanList.get(0).getUrl());

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
                Cursor cursor = r_sql.query(DBHelper.TABLE_NAME_HEALTH_INFORMATION, null, null, null, null, null, null);
                Log.e("cancel", String.valueOf(cursor.getCount()));
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        Log.e("cancel", title);
                        if (title.equals(healthBeanList.get(0).getTitle())) {
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
                } else {
                    Log.e("没有数据", "未插入成功");
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
//                            aboutWebView(cursor.getString(cursor.getColumnIndex("url")));
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

//    @SuppressLint("JavascriptInterface")
//    private void aboutWebView(String url) {
//        if (url == null) {
//            return;
//        }
//        content.loadUrl(url);
//        content.addJavascriptInterface(this, "android");
//        content.setWebViewClient(webViewClient);
//        content.setWebChromeClient(webChromeClient);
//
//        WebSettings webSettings = content.getSettings();
//        webSettings.setJavaScriptEnabled(true); // 允许使用js
//
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //不使用缓存，只从网络获取
//        webSettings.setSupportZoom(true); //支持屏幕缩放
//        webSettings.setBuiltInZoomControls(true);
//    }

}
