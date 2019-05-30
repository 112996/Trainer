package com.example.asus_pc.trainer.bean;



import cn.bmob.v3.BmobUser;

public class MyUsers extends BmobUser {

    private String phone;
    private String nickname;
    private String pic;

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


}
