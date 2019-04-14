package com.example.asus_pc.trainer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "user.db";
    public static final String TABLE_NAME = "user_Message";
    public static final String TABLE_NAME_USER = "user_me";
    public static final String TABLE_NAME_ARGS = "user_Args";

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //年龄、身高、体重、腰围、颈围、性别、运动、
        String sql = "create table if not exists " + TABLE_NAME + " (Id integer primary key, Age text, Height text, Weight text, Waist text, Neck text, Sex text, Sport text)";
        sqLiteDatabase.execSQL(sql);
        //账号、密码、手机号、头像、昵称
        String sql_user = "create table if not exists " + TABLE_NAME_USER + " (Id integer primary key, User_ID text, PassWd text, Tel text, Nickname text, Portrait_uri BLOB)";
        sqLiteDatabase.execSQL(sql_user);
        //BMI、BFR、BMR、Whtr
        String sql_args = "create table if not exists " + TABLE_NAME_ARGS + " (Id integer primary key, BFR text, BMI text, BMR text, Whtr text)";
        sqLiteDatabase.execSQL(sql_args);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String sql_user = "DROP TABLE IF EXISTS " + TABLE_NAME_USER;
        String sql_args = "DROP TABLE IF EXISTS " + TABLE_NAME_ARGS;
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql_user);
        sqLiteDatabase.execSQL(sql_args);
        onCreate(sqLiteDatabase);
    }
}
