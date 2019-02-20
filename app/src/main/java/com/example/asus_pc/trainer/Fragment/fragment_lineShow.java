package com.example.asus_pc.trainer.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.asus_pc.trainer.R;

import io.reactivex.annotations.Nullable;
import lecho.lib.hellocharts.view.LineChartView;

public class fragment_lineShow extends Fragment implements View.OnClickListener{
    private LineChartView lineChartView;
    private Button line_weight, line_BMI, line_BFR, line_BMR, line_Whtr;

    private View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragmnet_lineshow_layout, container, false);
        }
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        lineChartView = mView.findViewById(R.id.lineChartView);
        line_BFR = mView.findViewById(R.id.line_BFR);
        line_BMI = mView.findViewById(R.id.line_BMI);
        line_BMR = mView.findViewById(R.id.line_BMR);
        line_Whtr = mView.findViewById(R.id.line_Whtr);
        line_weight = mView.findViewById(R.id.line_weight);
        line_Whtr.setOnClickListener(this);
        line_BMR.setOnClickListener(this);
        line_BMI.setOnClickListener(this);
        line_BFR.setOnClickListener(this);
        line_weight.setOnClickListener(this);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) mView.getParent()).removeView(mView);
    }



    @Override
    public void onClick(View view) {

    }
}
