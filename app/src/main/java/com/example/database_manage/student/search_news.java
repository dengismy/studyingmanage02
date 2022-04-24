package com.example.database_manage.student;

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
import com.example.database_manage.administractor.change_news;
import com.example.database_manage.database.CommonDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class search_news extends AppCompatActivity {
    private SearchView mSearchView;
    private ListView lListView;
    SQLiteDatabase db;
    SimpleAdapter simpleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_news);
        mSearchView = findViewById(R.id.searchView);
        lListView = findViewById(R.id.listView);
        lListView.setTextFilterEnabled(false);
        CommonDatabase commonDatabase = new CommonDatabase();
        db = commonDatabase.getSqliteObject(search_news.this, "test_db");
        //得到cursor
        Cursor cursor = db.rawQuery("select * from news", null);
        ArrayList<Map<String, String>> arrayList_news = new ArrayList<Map<String, String>>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("title", cursor.getString(cursor.getColumnIndex("title")));
            map.put("date", cursor.getString(cursor.getColumnIndex("new_date")));
            map.put("id", cursor.getString(cursor.getColumnIndex("news_id")));
            arrayList_news.add(map);
        }
        //设置适配器，并绑定布局文件
        simpleAdapter = new SimpleAdapter(search_news.this, arrayList_news, R.layout.list_item_allnews,
                new String[]{"title", "date"}, new int[]{R.id.text_news_title, R.id.text_news_date});
        lListView.setAdapter(simpleAdapter);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                CommonDatabase commonDatabase = new CommonDatabase();
                db = commonDatabase.getSqliteObject(search_news.this, "test_db");
                //得到cursor
                Cursor cursor = db.rawQuery("select * from news where title like '%" + query + "%'", null);
                if (cursor.getCount() == 0) {
                    Toast.makeText(search_news.this, "无匹配", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Map<String, String>> arrayList_news = new ArrayList<Map<String, String>>();
                    while (cursor.moveToNext()) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("title", cursor.getString(cursor.getColumnIndex("title")));
                        map.put("date", cursor.getString(cursor.getColumnIndex("new_date")));
                        map.put("id", cursor.getString(cursor.getColumnIndex("news_id")));
                        arrayList_news.add(map);
                    }
                    //设置适配器，并绑定布局文件
                    simpleAdapter = new SimpleAdapter(search_news.this, arrayList_news, R.layout.list_item_allnews,
                            new String[]{"title", "date"}, new int[]{R.id.text_news_title, R.id.text_news_date});
                    lListView.setAdapter(simpleAdapter);
                }
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    lListView.setFilterText(newText);
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
                /****点击资讯进行查看****/
                HashMap<String,Object > map_item = (HashMap<String,Object >)lListView.getItemAtPosition(position);
                Intent intent_change_news = new Intent(search_news.this, show_news_detail.class);
                //获取map中的数据，并放入intent
                intent_change_news.putExtra("title",map_item.get("title")+"");
                intent_change_news.putExtra("id",map_item.get("id")+"");
                startActivity(intent_change_news);
            }
        });
    }
}
