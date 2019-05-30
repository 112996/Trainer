package com.example.asus_pc.trainer.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.activities.AddNoteActivity;
import com.example.asus_pc.trainer.activities.PhotoViewActivity;
import com.example.asus_pc.trainer.bean.MyUsers;
import com.example.asus_pc.trainer.bean.TrainNoteEntity;
import com.example.asus_pc.trainer.until.CircleImageView;
import com.example.asus_pc.trainer.until.DecimalUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

import cn.bmob.v3.BmobUser;

import static android.content.Context.MODE_PRIVATE;

public class TrainNoteAdapter extends BaseQuickAdapter<TrainNoteEntity, BaseViewHolder> {

    private Context mContext;

    public TrainNoteAdapter(Context context, @Nullable List<TrainNoteEntity> data) {
        super(R.layout.note_list_list_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final TrainNoteEntity item) {
        helper.setText(R.id.item_date, TextUtils.isEmpty(item.getDate()) ? "暂无数据" : item.getDate())
                .setText(R.id.item_exercise_length_tv, TextUtils.isEmpty(item.getExerciseDuration().toString()) ? "暂无数据" : DecimalUtils.formatDecimalWithZero2(item.getExerciseDuration()) + " (h)")
                .setText(R.id.item_run_length_tv, TextUtils.isEmpty(item.getRunLength().toString()) ? "暂无数据" : DecimalUtils.formatDecimalWithZero2(item.getRunLength()) + " (km)")
                .setText(R.id.item_situp_tv, TextUtils.isEmpty(item.getSitUps().toString()) ? "暂无数据" : item.getSitUps().toString() + " 个")
                .setText(R.id.item_sports_apparatus_tv, TextUtils.isEmpty(item.getSportsApparatusTimes().toString()) ? "暂无数据" : item.getSportsApparatusTimes().toString() + " 组")
                .setText(R.id.item_site_tv, TextUtils.isEmpty(item.getSpace())? "暂无数据" : item.getSpace());

        ImageView todayPictureNote = helper.getView(R.id.item_today_picture);
        if (item.getPictures() != null) {
            Glide.with(mContext)
                    .load(item.getPictures().get(0))
                    .placeholder(R.drawable.item_time)
                    .dontAnimate()
                    .thumbnail(0.1f)
                    .into(todayPictureNote);
        }

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddNoteActivity.class);
                intent.putExtra("objectId", item.getObjectId());
                mContext.startActivity(intent);
            }
        });
        helper.setOnClickListener(R.id.item_today_picture, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoViewActivity.class);
                if (item.getPictures() != null && item.getPictures().size() > 0) {
                    intent.putExtra("pictureUrl", item.getPictures().get(0));
                } else {
                    intent.putExtra("pictureUrl", "http://bmob-cdn-12073.b0.upaiyun.com/2017/06/21/a3f32615604747338c388c5678d3ca07.jpg");
                }
                mContext.startActivity(intent);
            }
        });

        //头像
        SharedPreferences s = mContext.getSharedPreferences("UserMsg", MODE_PRIVATE);
        String resImageBase64 = s.getString("savePortrait", "");
        if (!resImageBase64.isEmpty()) {
            byte[] base64byte = Base64.decode(resImageBase64, Base64.DEFAULT);
            ByteArrayInputStream instream = new ByteArrayInputStream(base64byte);
            CircleImageView user_protrait1 = helper.getView(R.id.item_avatar);
            user_protrait1.setImageDrawable(Drawable.createFromStream(instream, "res_img"));
    }else{
            CircleImageView user_protrait = helper.getView(R.id.item_avatar);
            MyUsers myUsers = BmobUser.getCurrentUser(MyUsers.class);
            String picPath = myUsers.getPic();

            if (!picPath.isEmpty()){
                Glide.with(mContext)
                        .load(picPath)
                        .placeholder(R.drawable.item_time)
                        .dontAnimate()
                        .thumbnail(0.1f)
                        .into(user_protrait);
            }else{
                Log.e("trainer号", picPath);
            }
        }
    }
}
