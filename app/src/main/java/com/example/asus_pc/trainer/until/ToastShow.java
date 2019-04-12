package com.example.asus_pc.trainer.until;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus_pc.trainer.R;

public class ToastShow {

    public void toastShow(Context context, CharSequence text){
        View rootView = LayoutInflater.from(context).inflate(R.layout.toast_layout,null);
        TextView mTextView = rootView.findViewById(R.id.toast_message);

        mTextView.setText(text);
        Toast toastStart = new Toast(context);

        //获取屏幕高度
        WindowManager vm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = vm.getDefaultDisplay().getHeight();

        //y坐标偏移量设为屏幕高度的1/7，适配所有机型
        toastStart.setGravity(Gravity.BOTTOM, 0, height / 7);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(rootView);
        toastStart.show();


    }
}















