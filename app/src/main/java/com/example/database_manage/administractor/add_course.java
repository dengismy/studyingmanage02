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

import static com.example.database_manage.database.MD5Demo.md5;


public class add_course extends AppCompatActivity {
    SQLiteDatabase db;
    EditText add_teacher_name;
    EditText add_course_name;
    EditText add_course_time;
    EditText add_course_period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        db =new CommonDatabase().getSqliteObject(add_course.this,"test_db");

       //编辑框绑定
        add_teacher_name = findViewById(R.id.add_teacher_name);
        add_course_name = findViewById(R.id.add_course_name);
        add_course_time = findViewById(R.id.add_course_time);
        add_course_period = findViewById(R.id.add_course_period);


        Button change = findViewById(R.id.button_add_course);
        change.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ContentValues values = new ContentValues();
                values.put("teacher_name", add_teacher_name.getText().toString());
                values.put("course_name", add_course_name.getText().toString());
                values.put("course_time", add_course_time.getText().toString());
                values.put("course_period", add_course_period.getText().toString());

                db.insert("course",null,values);
                Toast.makeText(add_course.this,"课程信息增加成功！",Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }
}
