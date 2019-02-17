package com.example.asus_pc.trainer.Me_Activty;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.asus_pc.trainer.DBHelper;
import com.example.asus_pc.trainer.R;
import com.example.asus_pc.trainer.UsersMsg;

public class UserMsgActivity extends AppCompatActivity {
    private EditText mAge, mHeight, mWeight, mWaist, mNeck;
    private RadioButton man, woman;
    private Button ok;
    private DBHelper userDBHelper;
    private SQLiteDatabase mSQL;
    private String MAN = "男", WOMAN ="女";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg);

        initViews();
        userDBHelper = new DBHelper(getApplicationContext());
        mSQL = userDBHelper.getWritableDatabase();


        addDatas();

    }

    private void initViews(){
        mAge = findViewById(R.id.age);
        mHeight = findViewById(R.id.height);
        mWeight = findViewById(R.id.weight);
        mWaist = findViewById(R.id.waist);
        mNeck = findViewById(R.id.neck);
        man = findViewById(R.id.man);
        woman = findViewById(R.id.woman);
        ok =findViewById(R.id.ok);
    }
    private void addDatas(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertUsersMsg();
            }
        });
    }

    public void insertUsersMsg(){
        ContentValues values = userToContentValues(mockUsersMsg());
        mSQL.insert(DBHelper.TABLE_NAME, null,values);
    }

    private UsersMsg mockUsersMsg(){
        UsersMsg users = new UsersMsg();
        users.age = mAge.getText().toString();
        users.height = mHeight.getText().toString();
        users.weight = mWeight.getText().toString();
        users.waist = mWaist.getText().toString();
        users.neck = mNeck.getText().toString();
        if(man.isChecked()){
            users.sex = MAN;
        }
        if (woman.isChecked()){
            users.sex = WOMAN;
        }
        return users;
    }
    private ContentValues userToContentValues(UsersMsg usersMsg){
        ContentValues contentValues = new ContentValues();
        contentValues.put("age",usersMsg.age);
        contentValues.put("height", usersMsg.height);
        contentValues.put("weight", usersMsg.weight);
        contentValues.put("waist", usersMsg.waist);
        contentValues.put("neck", usersMsg.neck);
        contentValues.put("sex", usersMsg.sex);
        return contentValues;
    }

}
