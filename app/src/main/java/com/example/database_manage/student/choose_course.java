package com.example.database_manage.student;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.database_manage.R;
import com.example.database_manage.administractor.search_college;
import com.example.database_manage.database.CommonDatabase;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class choose_course extends AppCompatActivity {
    private SQLiteDatabase db;
    private List<pay_item> arrayList;
    private courseAdapter c;
    private ListView listView_course;
    private Button button_back;
    private Button button_choose;
    private CommonDatabase commonDatabase;
    private Intent intent_3;
    String student_college;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_course);
        /////////////////////////////
        commonDatabase = new CommonDatabase();
        db = commonDatabase.getSqliteObject(choose_course.this, "test_db");
        /////////////////////////////
        listView_course = findViewById(R.id.listview_course);
        button_back = findViewById(R.id.back);
        button_choose = findViewById(R.id.choose);
        intent_3 = getIntent();
        //获取学生所属学院
        Cursor cursor_about = db.query("student", null, "id = ?", new String[]{intent_3.getStringExtra("student_id")}, null, null, null);
        while (cursor_about.moveToNext()) {
            //将通过id查询学生所属学院
            student_college=cursor_about.getString(cursor_about.getColumnIndex("college"));
        }

        View.OnClickListener listener_choose = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    /**缴费**/
                    case R.id.choose:
                        String string_chongtu ="";
                        for (int i = 0; i < c.getCount(); i++) {
                            //获取子项的对象
                            pay_item it = (pay_item) c.getItem(i);
                            //如果是被选中的状态
                            if (it.ischeck == true) {
                                //去表中查一下是否已经缴费
                                String p_name = it.pay_name;
                                Cursor cursor01 = db.query("student_pay", null, "student_id=?",
                                        new String[]{intent_3.getStringExtra("student_id")}, null, null, null);
                                if (cursor01.getCount() == 0) {
                                    ContentValues values01 = new ContentValues();
                                    values01.put("student_id", intent_3.getStringExtra("student_id"));
                                    values01.put("tuition", 111);
                                    values01.put("house", 0);
                                    values01.put("commodities", 0);
                                    values01.put("books", 0);
                                    db.insert("student_pay", null, values01);
                                }
                                while (cursor01.moveToNext()) {
                                    switch (p_name) {
                                        /**学费**/
                                        case ("学费"):
                                            if (cursor01.getInt(cursor01.getColumnIndex("tuition")) == 0) {
                                                ContentValues values = new ContentValues();
                                                values.put("tuition", 111);
                                                db.update("student_pay", values, "student_id=?", new String[]{intent_3.getStringExtra("student_id")});
                                            } else {
                                                string_chongtu += p_name;
                                                string_chongtu += "/";
                                            }
                                            break;
                                        case ("住宿费"):
                                            if (cursor01.getInt(cursor01.getColumnIndex("house")) == 0) {
                                                ContentValues values = new ContentValues();
                                                values.put("house", 111);
                                                db.update("student_pay", values, "student_id = ?", new String[]{intent_3.getStringExtra("student_id")});
                                            } else {
                                                string_chongtu += p_name;
                                                string_chongtu += "/";
                                            }
                                            break;
                                        case ("日用品套餐费"):
                                            if (cursor01.getInt(cursor01.getColumnIndex("commodities")) == 0) {
                                                ContentValues values = new ContentValues();
                                                values.put("commodities", 111);
                                                db.update("student_pay", values, "student_id = ?", new String[]{intent_3.getStringExtra("student_id")});
                                            } else {
                                                string_chongtu += p_name;
                                                string_chongtu += "/";
                                            }
                                            break;
                                        case ("书本费"):
                                            if (cursor01.getInt(cursor01.getColumnIndex("books")) == 0) {
                                                ContentValues values = new ContentValues();
                                                values.put("books", 111);
                                                db.update("student_pay", values, "student_id = ?", new String[]{intent_3.getStringExtra("student_id")});
                                            } else {
                                                string_chongtu += p_name;
                                                string_chongtu += "/";
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                            }
                            //如果没有冲突的
                            if (string_chongtu.equals("")) {
                                Toast.makeText(choose_course.this, "缴费成功！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(choose_course.this, string_chongtu + "重复缴费，其他缴费成功！", Toast.LENGTH_SHORT).show();
                            }
                        break;
                        /**返回键**/
                    case R.id.back:
                        finish();
                        break;
                }
            }
        };
        button_choose.setOnClickListener(listener_choose);
        button_back.setOnClickListener(listener_choose);

        //获取费用信息
        Cursor cursor = db.query("about_college", null, "college_name = ?", new String[]{student_college}, null, null, null);
        arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int tuition = cursor.getInt(cursor.getColumnIndex("tuition"));
            pay_item it1 = new pay_item("学费", tuition);
            int house = cursor.getInt(cursor.getColumnIndex("house"));
            pay_item it2 = new pay_item("住宿费", house);
            int commodities = cursor.getInt(cursor.getColumnIndex("commodities"));
            pay_item it3 = new pay_item("日用品套餐费", commodities);
            int books = cursor.getInt(cursor.getColumnIndex("books"));
            pay_item it4 = new pay_item("书本费", books);
            arrayList.add(it1);
            arrayList.add(it2);
            arrayList.add(it3);
            arrayList.add(it4);
        }
        //实例化Adapter
        c = new courseAdapter(choose_course.this, arrayList);
        listView_course.setAdapter(c);
    }
}





