package com.example.asus_pc.trainer.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus_pc.trainer.MyApplication;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.adapter.Log_RecyAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.Nullable;

public class fragment_course extends Fragment {


   /* @BindView(R.id.total_times)
    TextView totalTimes;
    @BindView(R.id.total_days)
    TextView totalDays;
    @BindView(R.id.total_run)
    TextView totalRun;
    @BindView(R.id.total_yangwo)
    TextView totalYangwo;
    @BindView(R.id.total_qixie)
    TextView totalQixie;
    @BindView(R.id.log_recycler)
    RecyclerView logRecycler;*/
    private View mView;
    private RecyclerView logRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //ButterKnife.bind(getActivity());
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_course_layout, container, false);
            logRecycler = mView.findViewById(R.id.log_recycler);
            logRecycler.setLayoutManager(new LinearLayoutManager(MyApplication.mContext));
        }
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Map<String, String> map = new HashMap();
        map.put("space","传奇健身房(成都店)");
        map.put("date","2019-4-12");
        map.put("time","1.00(h)");
        map.put("length","2.00(km)");
        map.put("yangwo","20(个)");
        map.put("qixie","5(组)");
        Map<String, String> map1 = new HashMap();
        map1.put("space","传奇健身房(成都店)");
        map1.put("date","2019-4-13");
        map1.put("time","1.50(h)");
        map1.put("length","2.00(km)");
        map1.put("yangwo","40(个)");
        map1.put("qixie","6(组)");
        Map<String, String> map2 = new HashMap();
        map2.put("space","传奇健身房(成都店)");
        map2.put("date","2019-4-14");
        map2.put("time","0.50(h)");
        map2.put("length","2.00(km)");
        map2.put("yangwo","20(个)");
        map2.put("qixie","2(组)");
        List<Map<String, String>> mDatas = new ArrayList<Map<String, String>>();
           mDatas.add(map);
           mDatas.add(map1);
           mDatas.add(map2);


        Log_RecyAdapter log_recyAdapter = new Log_RecyAdapter(MyApplication.mContext,mDatas );
        logRecycler.setAdapter(log_recyAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) mView.getParent()).removeView(mView);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }
}