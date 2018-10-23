package com.example.asus_pc.trainer.Fragment;

import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.asus_pc.trainer.LineShowActivity;
import com.example.asus_pc.trainer.R;

import io.reactivex.annotations.Nullable;


public class fragment_me extends Fragment {
        private View mView;
        private Button setting;
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            if(mView == null){
                mView = inflater.inflate(R.layout.fragment_me_layout, container, false);
            }
            return mView;
        }

        @Override
        public void onStart(){
            super.onStart();
            setting = mView.findViewById(R.id.setting);

            setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("TAG","你点了设置");
                }
            });
        }



        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ((ViewGroup) mView.getParent()).removeView(mView);
        }
    }

