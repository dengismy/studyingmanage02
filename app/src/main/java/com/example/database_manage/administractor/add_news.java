package com.example.database_manage.administractor;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


public class add_news extends AppCompatActivity {
    SQLiteDatabase db;
    EditText add_id;
    EditText add_title;
    EditText add_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        db =new CommonDatabase().getSqliteObject(add_news.this,"test_db");

       //编辑框绑定
        add_id = findViewById(R.id.add_news_id);
        add_title = findViewById(R.id.add_news_title);
        add_content = findViewById(R.id.add_news_content);



        Button change = findViewById(R.id.button_add_course);
        change.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ContentValues values = new ContentValues();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
                Date date = new Date(System.currentTimeMillis());
                String newdate=simpleDateFormat.format(date);
                values.put("news_id", add_id.getText().toString());
                values.put("title", add_title.getText().toString());
                values.put("content", add_content.getText().toString());
                values.put("new_date", newdate);
                db.insert("news",null,values);
                Toast.makeText(add_news.this,"资讯增加成功！",Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }
}
