package com.example.database_manage.student;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.database_manage.R;
import com.example.database_manage.administractor.change_news;
import com.example.database_manage.database.CommonDatabase;

public class show_news_detail extends AppCompatActivity {
    TextView news_title;
    TextView news_content;
    TextView change_time;
    String id;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news_detail);
        CommonDatabase commonDatabase = new CommonDatabase();
        db = commonDatabase.getSqliteObject(show_news_detail.this,"test_db");

        //显示框绑定
        news_title=findViewById(R.id.show_news_title);
        news_content=findViewById(R.id.show_news_content);
        change_time=findViewById(R.id.show_news_change_time);

        Intent intent_receive = getIntent();
        id=intent_receive.getStringExtra("id");
        Cursor cursor_about = db.query("news", null, "news_id = ?", new String[]{intent_receive.getStringExtra("id")}, null, null, null);
        while (cursor_about.moveToNext()) {
            news_title.setText(cursor_about.getString(cursor_about.getColumnIndex("title")));
            news_content.setText(cursor_about.getString(cursor_about.getColumnIndex("content")));
            change_time.setText(cursor_about.getString(cursor_about.getColumnIndex("new_date")));
        }
    }
}
