package com.example.asus_pc.trainer.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.asus_pc.trainer.Me_Activty.BFRActivity;
import com.example.asus_pc.trainer.Me_Activty.BMIActivity;
import com.example.asus_pc.trainer.Me_Activty.BMRActivity;
import com.example.asus_pc.trainer.R;

import cn.bmob.v3.BmobUser;
import io.reactivex.annotations.Nullable;


public class fragment_me extends Fragment {
    private View mView;
    private Button logout;
    private Button BMI;
    private Button BFR;
    private Button BMR;

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
        logout = mView.findViewById(R.id.logout);
        BMI = mView.findViewById(R.id.BMI);
        BFR = mView.findViewById(R.id.BFR);
        BMR = mView.findViewById(R.id.BMR);

           logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BmobUser.logOut();
                }
            });

           BMI.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   startActivity(new Intent(getActivity(),BMIActivity.class));
               }
           });

           BFR.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   startActivity(new Intent(getActivity(),BFRActivity.class));
               }
           });
           BMR.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   startActivity(new Intent(getActivity(),BMRActivity.class));
               }
           });
        }

        @Override
        public void onDestroyView () {
            super.onDestroyView();
            ((ViewGroup) mView.getParent()).removeView(mView);
        }
    }


