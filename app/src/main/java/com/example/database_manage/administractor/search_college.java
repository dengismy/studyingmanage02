package com.example.database_manage.administractor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;
import com.example.database_manage.student.show_news_detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class search_college extends AppCompatActivity {
    private SearchView mSearchView;
    private ListView lListView;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_news);
        mSearchView = findViewById(R.id.searchView);
        lListView = findViewById(R.id.listView);
        lListView.setTextFilterEnabled(false);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            SimpleAdapter simpleAdapter;
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                CommonDatabase commonDatabase = new CommonDatabase();
                db = commonDatabase.getSqliteObject(search_college.this, "test_db");
                //得到cursor
                Cursor cursor = db.rawQuery("select * from about_college where college_name like '%" + query + "%'", null);
                if (cursor.getCount() == 0) {
                    Toast.makeText(search_college.this, "无匹配", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Map<String, String>> arrayList_news = new ArrayList<Map<String, String>>();
                    while (cursor.moveToNext()) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", cursor.getString(cursor.getColumnIndex("college_name")));
                        map.put("tuition", cursor.getString(cursor.getColumnIndex("tuition")));
                        map.put("commodities", cursor.getString(cursor.getColumnIndex("commodities")));
                        map.put("house", cursor.getString(cursor.getColumnIndex("house")));
                        map.put("books", cursor.getString(cursor.getColumnIndex("books")));
                        map.put("people", cursor.getString(cursor.getColumnIndex("people")));
                        arrayList_news.add(map);
                    }
                    //设置适配器，并绑定布局文件
                    simpleAdapter = new SimpleAdapter(search_college.this, arrayList_news, R.layout.list_item_colleges,
                            new String[]{"name", "people"}, new int[]{R.id.text_college_name, R.id.text_recruit_number});
                    lListView.setAdapter(simpleAdapter);
                }
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    lListView.setFilterText(newText);
                    CommonDatabase commonDatabase = new CommonDatabase();
                    db = commonDatabase.getSqliteObject(search_college.this, "test_db");
                    //得到id
                    Cursor cursor = db.rawQuery("select * from about_college where college_name like '%" + newText + "%'", null);
                    if (cursor.getCount() == 0) {
                        Toast.makeText(search_college.this, "无匹配", Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayList<Map<String, String>> arrayList_news = new ArrayList<Map<String, String>>();
                        while (cursor.moveToNext()) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("name", cursor.getString(cursor.getColumnIndex("college_name")));
                            map.put("tuition", cursor.getString(cursor.getColumnIndex("tuition")));
                            map.put("commodities", cursor.getString(cursor.getColumnIndex("commodities")));
                            map.put("house", cursor.getString(cursor.getColumnIndex("house")));
                            map.put("books", cursor.getString(cursor.getColumnIndex("books")));
                            map.put("people", cursor.getString(cursor.getColumnIndex("people")));
                            arrayList_news.add(map);
                        }
                        //设置适配器，并绑定布局文件
                        simpleAdapter = new SimpleAdapter(search_college.this, arrayList_news, R.layout.list_item_colleges,
                                new String[]{"name", "people"}, new int[]{R.id.text_college_name, R.id.text_recruit_number});
                        lListView.setAdapter(simpleAdapter);
                    }
                }else{
                    lListView.clearTextFilter();
                }
                return false;
            }
        });
        //为搜索结果listview设定监听器
        lListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /****点击学院进行查看****/
                HashMap<String,Object > map_item = (HashMap<String,Object >)lListView.getItemAtPosition(position);
                Intent intent_change_college = new Intent(search_college.this,set_college.class);
                //获取map中的数据，并放入intent
                intent_change_college.putExtra("college_name",map_item.get("name")+"");
                startActivity(intent_change_college);
            }
        });
    }
}
