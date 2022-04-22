package com.example.database_manage.administractor;

import android.content.ContentValues;
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

public class set_college extends AppCompatActivity implements View.OnClickListener{
    EditText college_tuition;
    EditText college_house;
    EditText college_commodities;
    EditText college_books;
    EditText college_people;
    TextView college_name;
    String name;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_college);

        CommonDatabase commonDatabase = new CommonDatabase();
        db = commonDatabase.getSqliteObject(set_college.this,"test_db");
        //按钮绑定
        Button button_baocun = findViewById(R.id.button_change_college);
        //监听器
        button_baocun.setOnClickListener(this);
        //编辑框绑定
        college_name=findViewById(R.id.text_college_name);
        college_tuition=findViewById(R.id.et_college_tuition);
        college_commodities=findViewById(R.id.et_college_commodities);
        college_house=findViewById(R.id.et_college_house);
        college_books=findViewById(R.id.et_college_books);
        college_people=findViewById(R.id.et_college_people);
        Intent intent_receive = getIntent();
        name=intent_receive.getStringExtra("college_name");
        college_name.setText(name);
        Cursor cursor_about = db.query("about_college", null, "college_name = ?", new String[]{name}, null, null, null);
        while (cursor_about.moveToNext()) {
            college_tuition.setText(cursor_about.getString(cursor_about.getColumnIndex("tuition")));
            college_commodities.setText(cursor_about.getString(cursor_about.getColumnIndex("commodities")));
            college_house.setText(cursor_about.getString(cursor_about.getColumnIndex("house")));
            college_books.setText(cursor_about.getString(cursor_about.getColumnIndex("books")));
            college_people.setText(cursor_about.getString(cursor_about.getColumnIndex("people")));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /****更改*****/
            case R.id.button_change_college:
                ContentValues values = new ContentValues();
                values.put("tuition", college_tuition.getText().toString());
                values.put("commodities", college_commodities.getText().toString());
                values.put("house", college_house.getText().toString());
                values.put("books", college_books.getText().toString());
                values.put("people", college_people.getText().toString());

                db.update("about_college", values, "college_name= ?", new String[]{name});
                finish();
                break;

            default:
                break;
        }
    }
}
