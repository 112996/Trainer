package com.example.asus_pc.trainer.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.until.CheckNetwork;


public class Fragment_Health_New extends Fragment {
    private View mView;
    private WebView content;
    private ScrollView scrollView;
    private boolean isNetwork;
    private String url;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_health_new_layout, container, false);

            Bundle bundle = this.getArguments();
            if (bundle != null){
                url = bundle.getString("url");
            }else{
                Log.e("bundle", "为空");
            }

            initView();
            initData();



        }
        return mView;
    }

    private void initView(){
        content = mView.findViewById(R.id.content);
        scrollView = mView.findViewById(R.id.scrollView);

    }

    @SuppressLint("JavascriptInterface")
    private void initData(){

        CheckNetwork checkNetwork = new CheckNetwork();
        isNetwork = checkNetwork.getInfo(getActivity());
        if (isNetwork){

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        content.destroy();
        content = null;
    }
}
