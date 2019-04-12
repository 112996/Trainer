package com.example.asus_pc.trainer;

import android.util.Log;

import com.example.asus_pc.trainer.bean.MyUsers;
import com.example.asus_pc.trainer.bean.User_Message;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchFromBmob {
    private List list = new ArrayList();
    private void queryWeight(){
        BmobQuery<User_Message> query = new BmobQuery<>();
        query.addWhereEqualTo("author", BmobUser.getCurrentUser(MyUsers.class));
        query.order("-updatedAt");
        //包含作者信息
        query.include("author");
        query.findObjects(new FindListener<User_Message>() {

            @Override
            public void done(List<User_Message> object, BmobException e) {
                if (e == null) {
                    for (User_Message user_message : object) {
                        list.add(user_message.getWeight());
                        Log.d("queryFromBmob", list.toString());
                        Log.d("queryFromBmob", "查询成功");
                    }
                    Log.d("hahaah", String.valueOf(list.size()));
                } else {
                    Log.e("FromBmob", e.toString());
                }
            }

        });
    }
}
