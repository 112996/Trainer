package com.example.asus_pc.trainer;

import cn.bmob.v3.BmobObject;

public class User_Message extends BmobObject {
    public String age;
    public String height;
    public String weight;
    public String waist;
    public String neck;
    public String sex;
    public String sport;
    private MyUsers author;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getNeck() {
        return neck;
    }

    public void setNeck(String neck) {
        this.neck = neck;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public MyUsers getAuthor() {
        return author;
    }

    public void setAuthor(MyUsers author) {
        this.author = author;
    }

}
