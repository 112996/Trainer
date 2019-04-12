package com.example.asus_pc.trainer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.until.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Log_RecyAdapter extends RecyclerView.Adapter<Log_RecyAdapter.VH> {
    private List<Map<String, String>> mDatas = new ArrayList<Map<String, String>>();


    public static class VH extends RecyclerView.ViewHolder {

        public final TextView item_space, item_date, item_time, item_runlength, item_yangwo, item_qixie;

        public final CircleImageView item_portrait;

        public final ImageView item_picture;

        public VH(View v) {
            super(v);
            item_portrait = v.findViewById(R.id.recycler_item_protrait);
            item_space = v.findViewById(R.id.recycler_item_space);
            item_date = v.findViewById(R.id.recycler_item_date);
            item_picture = v.findViewById(R.id.log_recycler_item_picture);
            item_time = v.findViewById(R.id.recycler_item_time);
            item_runlength = v.findViewById(R.id.recycler_item_runlength);
            item_yangwo = v.findViewById(R.id.recycler_item_yangwo);
            item_qixie = v.findViewById(R.id.recycler_item_qixie);

        }
    }

    public Log_RecyAdapter(Context context, List<Map<String, String>> listNewsBean) {
        this.mDatas = listNewsBean;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.log_recycler_item_layout, viewGroup, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        Map map = mDatas.get(i);
        vh.item_portrait.setImageResource(R.drawable.user_portrait);
        vh.item_space.setText((String) map.get("space"));
        vh.item_date.setText((String)map.get("date"));
        vh.item_picture.setImageResource(R.drawable.log_total_days);
        vh.item_time.setText((String)map.get("time"));
        vh.item_runlength.setText((String)map.get("length"));
        vh.item_yangwo.setText((String)map.get("yangwo"));
        vh.item_qixie.setText((String)map.get("qixie"));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}
