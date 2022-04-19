package com.example.database_manage.teacher;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;

import static java.sql.DriverManager.println;
/*
    该界面主要用于修改学生分数的信息
 */
public class change_student_score extends AppCompatActivity {
    private SQLiteDatabase db;
    private Button button_change;
    private Button button_back;
    private Intent intent;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;

    private EditText editText11;
    private EditText editText12;
    private EditText editText21;
    private EditText editText22;
    private EditText editText23;
    private EditText editText31;
    private EditText editText32;
    private EditText editText33;
    private EditText editText34;
    private EditText editText35;

    private TextView textView5;

    public change_student_score() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_student_score);
        init();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_score_change:
                        ContentValues values = new ContentValues();
                        values.put("student_id", textView1.getText().toString());

                        values.put("score11", editText11.getText().toString());
                        String s11=editText11.getText().toString();
                        float score11;
                        if(s11.equals("null")){
                            score11=0;
                        } else { score11= Float.parseFloat(s11);}


                        values.put("score12", editText12.getText().toString());
                        String s12=editText12.getText().toString();
                        float score12;
                        if(s12.equals("null")){
                            score12=0;
                            System.out.println("输入为空");
                        } else {score12= Float.parseFloat(s12);}


                        values.put("score21", editText21.getText().toString());
                        String s21=editText21.getText().toString();
                        float score21;
                        if(s21.equals("null")){
                            score21=0;
                        } else { score21= Float.parseFloat(s21);}


                        values.put("score22", editText22.getText().toString());
                        String s22=editText22.getText().toString();
                        float score22;
                        if(s22.equals("null")){
                            score22=0;
                        } else { score22= Float.parseFloat(s22);}


                        values.put("score23",editText23.getText().toString());
                        String s23=editText23.getText().toString();
                        float score23;
                        if(s23.equals("null")){
                            score23=0;
                        } else { score23= Float.parseFloat(s23);}


                        values.put("score31", editText31.getText().toString());
                        String s31=editText31.getText().toString();
                        float score31;
                        if(s31.equals("null")){
                            score31=0;
                        } else { score31= Float.parseFloat(s31);}

                        values.put("score32", editText32.getText().toString());
                        String s32=editText32.getText().toString();
                        float score32;
                        if(s32.equals("null")){
                            score32=0;
                        } else { score32= Float.parseFloat(s32);}

                        values.put("score33", editText33.getText().toString());
                        String s33=editText33.getText().toString();
                        float score33;
                        if(s33.equals("null")){
                            score33=0;
                        } else { score33= Float.parseFloat(s33);}

                        values.put("score34", editText34.getText().toString());
                        String s34=editText34.getText().toString();
                        float score34;
                        if(s34.equals("null")){
                            score34=0;
                        } else { score34= Float.parseFloat(s34);}

                        values.put("score35", editText35.getText().toString());
                        String s35=editText35.getText().toString();
                        float score35;
                        if(s35.equals("null")){
                            score35=0;
                        } else { score35= Float.parseFloat(s35);}
                     //总分
                        float score=score11+score12+score21+score22+score23+score31+score32+score33+score34+score35;
                        System.out.println("总分"+score);
                        values.put("score", score);

                        db.update("student_course", values, "student_id = ? AND course_name = ? ",
                                new String[]{textView1.getText().toString(), intent.getStringExtra("course_name")});
                        finish();
                        break;
                    case R.id.button_score_back:
                        finish();
                        break;
                    default:
                }

            }
        };

        button_change.setOnClickListener(listener);
        button_back.setOnClickListener(listener);

    }
    public void init()
    {
        //获取数据库对象
        db = new CommonDatabase().getSqliteObject(change_student_score.this, "test_db");

        //绑定按钮
        button_change = findViewById(R.id.button_score_change);
        button_back = findViewById(R.id.button_score_back);
        //从上一个活动接受数据，并置上去
        intent = getIntent();
        textView1 = findViewById(R.id.t_id);
        textView1.setText(intent.getStringExtra("student_id"));
        textView2 = findViewById(R.id.t_name);
        textView2.setText(intent.getStringExtra("name"));
        textView3 = findViewById(R.id.t_banji);
        textView3.setText(intent.getStringExtra("banji"));
        textView4 = findViewById(R.id.tc_course_name);
        textView4.setText(intent.getStringExtra("course_name"));

        editText11 = findViewById(R.id.edit_score11);
        editText11.setText(intent.getStringExtra("score11"));
        editText12 = findViewById(R.id.edit_score12);
        editText12.setText(intent.getStringExtra("score12"));
        editText21 = findViewById(R.id.edit_score21);
        editText21.setText(intent.getStringExtra("score21"));
        editText22 = findViewById(R.id.edit_score22);
        editText22.setText(intent.getStringExtra("score22"));
        editText23 = findViewById(R.id.edit_score23);
        editText23.setText(intent.getStringExtra("score23"));
        editText31 = findViewById(R.id.edit_score31);
        editText31.setText(intent.getStringExtra("score31"));
        editText32 = findViewById(R.id.edit_score32);
        editText32.setText(intent.getStringExtra("score32"));
        editText33 = findViewById(R.id.edit_score33);
        editText33.setText(intent.getStringExtra("score33"));
        editText34 = findViewById(R.id.edit_score34);
        editText34.setText(intent.getStringExtra("score34"));
        editText35 = findViewById(R.id.edit_score35);
        editText35.setText(intent.getStringExtra("score35"));

        textView5 = findViewById(R.id.edit_score);
        textView5.setText(intent.getStringExtra("score"));
    }
}
