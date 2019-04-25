package com.example.asus_pc.trainer.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.asus_pc.trainer.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.jaeger.library.StatusBarUtil;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoViewActivity extends Activity {


    @BindView(R.id.photo_view_back_iv)
    public ImageView photoViewBackIv;
    @BindView(R.id.photo_view_title_tb)
    public Toolbar photoViewTitleTb;
    @BindView(R.id.photo_view)
    public PhotoView photoView;
    @BindView(R.id.photo_view_avi)
    public AVLoadingIndicatorView photoViewAvi;

    private String pictureUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        StatusBarUtil.setColor(PhotoViewActivity.this, Color.parseColor("#2D374C"), 0);
        ButterKnife.bind(this);
        pictureUrl = getIntent().getStringExtra("pictureUrl");
        Glide.with(this)
                .load(pictureUrl)
                .listener(glideRequestListener)
                .crossFade()
                .into(photoView);
    }

    private RequestListener<String, GlideDrawable> glideRequestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            Toast.makeText(PhotoViewActivity.this, "图片加载失败，请稍后再试。。。", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            loadCompleted();
            return false;
        }
    };

    private void loadCompleted() {
        photoViewAvi.setVisibility(View.GONE);
        photoView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.photo_view_back_iv)
    public void onClick() {
        finish();
    }
}
