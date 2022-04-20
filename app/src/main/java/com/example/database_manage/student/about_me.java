package com.example.database_manage.student;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;

/*
    我的信息功能实现，主要根据登录时传过来的intent所携带的数据
 */
public class about_me extends AppCompatActivity {
    SQLiteDatabase db;
    //绑定组件
    TextView t_about_id ;
    TextView t_about_name ;
    EditText t_about_phone;
    TextView t_about_banji ;
    TextView t_about_sex ;
    TextView t_about_age;
    TextView t_about_college;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        //获取数据库对象
        db = new CommonDatabase().getSqliteObject(about_me.this, "test_db");

        //获取登录时传来的信息
        final Intent intent_about_me = getIntent();

        //绑定组件
        t_about_id = findViewById(R.id.aboutme_id);
        t_about_name = findViewById(R.id.aboutme_name);
        t_about_phone = findViewById(R.id.aboutme_phone);
        t_about_banji = findViewById(R.id.aboutme_banji);
        t_about_sex = findViewById(R.id.aboutme_sex);
        t_about_age = findViewById(R.id.aboutme_age);
        t_about_college = findViewById(R.id.aboutme_college);


        Cursor cursor_about = db.query("student", null, "id = ?", new String[]{intent_about_me.getStringExtra("student_id")}, null, null, null);
        while (cursor_about.moveToNext()) {
            //将通过id查询到的学生信息显示到界面中
            t_about_id.setText(cursor_about.getString(cursor_about.getColumnIndex("id")));
            t_about_name.setText(cursor_about.getString(cursor_about.getColumnIndex("name")));
            t_about_banji.setText(cursor_about.getString(cursor_about.getColumnIndex("banji")));
            t_about_phone.setText(cursor_about.getString(cursor_about.getColumnIndex("phone")));
            t_about_sex.setText(cursor_about.getString(cursor_about.getColumnIndex("sex")));
            t_about_age.setText(cursor_about.getString(cursor_about.getColumnIndex("age")));
            t_about_college.setText(cursor_about.getString(cursor_about.getColumnIndex("college")));

        }
        Button button_back = findViewById(R.id.button_finish_about);
        Button button_save=findViewById(R.id.button_save_about);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                String phone = t_about_phone.getText().toString();
                values.put("phone", phone);

                db.update("student", values, "id = ? ", new String[]{t_about_id.getText().toString()});
                finish();
            }
        });
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
