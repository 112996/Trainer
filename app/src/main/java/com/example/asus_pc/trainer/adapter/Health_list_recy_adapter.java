package com.example.asus_pc.trainer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus_pc.trainer.R;

import java.util.ArrayList;

public class Health_list_recy_adapter extends RecyclerView.Adapter<Health_list_recy_adapter.VH> {
    ArrayList<String> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class VH extends RecyclerView.ViewHolder{
        public final TextView title;

        public VH (View v){
            super(v);
            title = v.findViewById(R.id.adapter_item_title);

        }
    }
    public Health_list_recy_adapter(ArrayList<String> list){
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_health_item, viewGroup, false);
        return new VH (v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, final int i) {
        String title = list.get(i);
        Log.e("list.get(i)",list.get(i));
        if (title == null){
            vh.title.setText("暂无数据");
        }else{
            vh.title.setText(list.get(i));
        }


        if (onItemClickListener != null) {
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, i);
                }

            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
