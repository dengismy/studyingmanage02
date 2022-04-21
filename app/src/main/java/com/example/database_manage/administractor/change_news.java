package com.example.database_manage.administractor;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class change_news extends AppCompatActivity implements View.OnClickListener{
EditText news_title;
EditText news_content;
TextView change_time;
String id;
SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_news);
        CommonDatabase commonDatabase = new CommonDatabase();
        db = commonDatabase.getSqliteObject(change_news.this,"test_db");
        //按钮绑定
        Button button_delete = findViewById(R.id.button_delete_news);
        Button button_baocun = findViewById(R.id.button_change_news);
        //监听器
        button_delete.setOnClickListener(this);
        button_baocun.setOnClickListener(this);

        //编辑框绑定
        news_title=findViewById(R.id.et_news_title);
        news_content=findViewById(R.id.et_news_content);
        change_time=findViewById(R.id.et_news_change_time);

        Intent intent_receive = getIntent();
        id=intent_receive.getStringExtra("id");
        Cursor cursor_about = db.query("news", null, "news_id = ?", new String[]{intent_receive.getStringExtra("id")}, null, null, null);
        while (cursor_about.moveToNext()) {
            news_title.setText(cursor_about.getString(cursor_about.getColumnIndex("title")));
            news_content.setText(cursor_about.getString(cursor_about.getColumnIndex("content")));
            change_time.setText(cursor_about.getString(cursor_about.getColumnIndex("new_date")));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_delete_news:
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(change_news.this);
                //    设置Title的图标
                builder.setIcon(R.drawable.delete);
                //    设置Title的内容
                builder.setTitle("弹出警告框");
                //    设置Content来显示一个信息
                builder.setMessage("确定删除吗？");
                //    设置一个PositiveButton
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.delete("news", "news_id=? ", new String[]{id});
                        finish();
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                //    显示出该对话框
                builder.show();
                break;

            /****更改*****/
            case R.id.button_change_news:
                ContentValues values = new ContentValues();
                values.put("title",news_title.getText().toString());
                values.put("content", news_content.getText().toString());

                //获取当前时间
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String ndate=simpleDateFormat.format(date);
                values.put("new_date", ndate);

                db.update("news", values, "news_id = ?", new String[]{id});
                finish();
                break;

            default:
                break;
        }
    }
}
