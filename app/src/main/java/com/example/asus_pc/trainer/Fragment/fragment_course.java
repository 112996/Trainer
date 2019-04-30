package com.example.asus_pc.trainer.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.asus_pc.trainer.MyApplication;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.activities.AddNoteActivity;
import com.example.asus_pc.trainer.adapter.TrainNoteAdapter;
import com.example.asus_pc.trainer.bean.MyUsers;
import com.example.asus_pc.trainer.bean.TrainNoteEntity;
import com.example.asus_pc.trainer.until.DecimalUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.annotations.Nullable;

public class fragment_course extends Fragment {

    private View mView;
    private TextView total_times, total_days, totao_run, total_yangwo, total_qixie;
    private SwipeRefreshLayout srl;  //新增
    private RecyclerView logRecycler;
    private List<TrainNoteEntity>  trainNoteEntityList;  //新增
    private ImageButton note_add;
    private TrainNoteAdapter trainNoteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_course_layout, container, false);

            initView();
            initData();

        }
        return mView;
    }

    private void  initView(){
        srl = mView.findViewById(R.id.srl);    //新增
        logRecycler = mView.findViewById(R.id.log_recycler);
        logRecycler.setLayoutManager(new LinearLayoutManager(MyApplication.mContext));
        note_add = mView.findViewById(R.id.note_add);
        total_times = mView.findViewById(R.id.total_times);
        total_days = mView.findViewById(R.id.total_days);
        totao_run = mView.findViewById(R.id.total_run);
        total_yangwo = mView.findViewById(R.id.total_yangwo);
        total_qixie = mView.findViewById(R.id.total_qixie);


        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        note_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNoteActivity.class));
            }
        });
    }

    private void initData(){
        srl.setRefreshing(true);
        BmobQuery<TrainNoteEntity> query = new BmobQuery<>();
        query.addWhereEqualTo("author", BmobUser.getCurrentUser(MyUsers.class).getObjectId());
        //包含作者信息
        query.include("author");
        query.setLimit(100);
        query.findObjects(new FindListener<TrainNoteEntity>() {
            @Override
            public void done(List<TrainNoteEntity> list, BmobException e) {
                srl.setRefreshing(false);
                if (e == null) {
                    refreshList(list);
                } else {
                    Log.e("刷新列表", e.getMessage());
                }
            }
        });
    }

    private void refreshList(List<TrainNoteEntity> list) {
        trainNoteEntityList = new ArrayList<>();
        for(int i = list.size() -1; i >= 0; i--){
            trainNoteEntityList.add(list.get(i));
        }
        trainNoteAdapter = new TrainNoteAdapter(getActivity(), trainNoteEntityList);
        trainNoteAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        logRecycler.setAdapter(trainNoteAdapter);

        float totalTime = 0f;
        float totalRunLength = 0;
        int totalSitUp = 0;
        int totalSportsApparatusTimes = 0;

        for (int i = 0; i < trainNoteEntityList.size(); i++) {
            TrainNoteEntity bean = trainNoteEntityList.get(i);
            totalTime += bean.getExerciseDuration();
            totalRunLength += bean.getRunLength();
            totalSitUp += bean.getSitUps();
            totalSportsApparatusTimes += bean.getSportsApparatusTimes();
        }
        //设置上半部分的各总数
        total_times.setText(DecimalUtils.formatDecimalWithZero(totalTime, 1));
        total_days.setText(String.valueOf(trainNoteEntityList.size()));
        totao_run.setText(String.valueOf(DecimalUtils.formatDecimalWithZero(totalRunLength, 2)));
        total_yangwo.setText(String.valueOf(totalSitUp));
        total_qixie.setText(String.valueOf(totalSportsApparatusTimes));

        trainNoteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
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