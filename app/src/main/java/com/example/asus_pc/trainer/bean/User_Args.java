package com.example.asus_pc.trainer.bean;

import cn.bmob.v3.BmobObject;

public class User_Args extends BmobObject {
    private String bmi;
    private String bmr;
    private String bfr;
    private String whtr;
    private String currentDate;
    private MyUsers author;

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getBmr() {
        return bmr;
    }

    public void setBmr(String bmr) {
        this.bmr = bmr;
    }

    public String getBfr() {
        return bfr;
    }

    public void setBfr(String bfr) {
        this.bfr = bfr;
    }

    public String getWhtr() {
        return whtr;
    }

    public void setWhtr(String whtr) {
        this.whtr = whtr;
    }

    public MyUsers getAuthor() {
        return author;
    }

    public void setAuthor(MyUsers author) {
        this.author = author;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

}
