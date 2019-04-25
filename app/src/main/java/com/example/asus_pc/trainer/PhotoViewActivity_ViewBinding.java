package com.example.asus_pc.trainer;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;

import com.example.asus_pc.trainer.activities.PhotoViewActivity;
import com.github.chrisbanes.photoview.PhotoView;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;

public class PhotoViewActivity_ViewBinding implements Unbinder {
    private PhotoViewActivity target;

    private View view2131230912;

    @UiThread
    public PhotoViewActivity_ViewBinding(PhotoViewActivity target) {
        this(target, target.getWindow().getDecorView());
    }

    @UiThread
    public PhotoViewActivity_ViewBinding(final PhotoViewActivity target, View source) {
        this.target = target;

        View view;
        target.photoView = Utils.findRequiredViewAsType(source, R.id.photo_view, "field 'photoView'", PhotoView.class);
        view = Utils.findRequiredView(source, R.id.photo_view_back_iv, "field 'photoViewBackIv' and method 'onClick'");
        target.photoViewBackIv = Utils.castView(view, R.id.photo_view_back_iv, "field 'photoViewBackIv'", ImageView.class);
        view2131230912 = view;
        view.setOnClickListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View p0) {
                target.onClick();
            }
        });
        target.photoViewAvi = Utils.findRequiredViewAsType(source, R.id.photo_view_avi, "field 'photoViewAvi'", AVLoadingIndicatorView.class);
    }

    @Override
    @CallSuper
    public void unbind() {
        PhotoViewActivity target = this.target;
        if (target == null) throw new IllegalStateException("Bindings already cleared.");
        this.target = null;

        target.photoView = null;
        target.photoViewBackIv = null;
        target.photoViewAvi = null;

        view2131230912.setOnClickListener(null);
        view2131230912 = null;
    }
}
