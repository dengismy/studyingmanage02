package com.example.database_manage.teacher;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;

import com.example.database_manage.administractor.add_news;
import com.example.database_manage.administractor.change_news;
import com.example.database_manage.utils.Common_toolbarColor;
import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;
import com.example.database_manage.database.image_store;
import com.example.database_manage.utils.alertDialog_builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
/*
    教师端主界面
 */

public class activity_teacher extends AppCompatActivity {

    //用于显示信息的列表listview
    private ListView listView;

    //数据库对象，用来操作数据库
    private SQLiteDatabase db;

    //侧滑
    private DrawerLayout drawerLayout;

    //记录状态
    private String state = "";

    //用于下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;

    //用于接收的intent
    private Intent receive_intent;

    //用于替代ActionBar的toolbar
    private Toolbar toolbar;

    //触发查增加资讯的button
    private FloatingActionButton button_add_news;

    //圆形imageview 用于显示头像
    private CircleImageView circleImageView;

    //初始自带的actionbar
    private ActionBar actionBar;

    //
    private NavigationView navigationView;

    //对话框类
    private AlertDialog.Builder builder;

    //navigationView的顶部view
    private View headview;

    //登录成功后，侧滑菜单显示的用户身份标识
    private TextView textView_welcome;

    private static final int TAKE_PHOTO = 1;
    private image_store imageStore;
    private Bitmap bitmap_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teacher);

        init();

        //去查找该用户有没有头像
        bitmap_temp = imageStore.getBmp(db, receive_intent.getStringExtra("teacher_id"));

        //如果有头像，那就把查到的这个放大头像
        if (bitmap_temp != null) {
            circleImageView.setImageBitmap(bitmap_temp);
        }
        //监听navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_myinfo_t:
                        Intent intent_about = new Intent(activity_teacher.this, teacher_about_me.class);
                        intent_about.putExtra("teacher_id", receive_intent.getStringExtra("teacher_id"));
                        startActivity(intent_about);

                        break;
                    //退出登录
                    case R.id.nav_menu_changeacc_t:

                        builder = new alertDialog_builder(activity_teacher.this).build();
                        //    显示出该对话框
                        builder.show();
                        break;
                    //查看留言
                    case R.id.nav_menu_lookliuyan:
                        Cursor cursor_look = db.query("message", null, null, null, null, null, null);

                        //对游标进行遍历
                        if (cursor_look.getCount() == 0) {
                            Toast.makeText(activity_teacher.this, "还没有任何留言", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<Map<String, String>> arrayList_mycourse = new ArrayList<Map<String, String>>();
                            while (cursor_look.moveToNext()) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("message", cursor_look.getString(cursor_look.getColumnIndex("message")));
                                arrayList_mycourse.add(map);

                            }
                            //设置适配器
                            SimpleAdapter simpleAdapter = new SimpleAdapter(activity_teacher.this, arrayList_mycourse, R.layout.list_item_message_t_look,
                                    new String[]{"message"}, new int[]{R.id.text});
                            listView.setAdapter(simpleAdapter);

                        }
                        break;

                        //老师修改密码
                    case R.id.nav_menu_change_teaaccount:
                        Intent intent_change02= new Intent(activity_teacher.this,tea_password_change.class);
                        startActivity(intent_change02);
                        break;

                        //资讯管理
                    case R.id.nav_menu_manage_message:
                        state = "mycourse";
                        look_news();
                        break;

                    //宿舍分配
                    case R.id.nav_menu_assign_dormitory:
                        Toast.makeText(activity_teacher.this,"宿舍分配",Toast.LENGTH_LONG).show();
                        break;
                    //资讯管理
                    case R.id.nav_menu_scan_code:
                        state = "scan_code";
                        Toast.makeText(activity_teacher.this,"扫描二维码",Toast.LENGTH_LONG).show();
                        break;

                    default:
                        break;
                }

                return true;
            }
        });

        //为listview设定监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /****点击资讯进行修改****/
                HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);
                Intent intent_change_news = new Intent(activity_teacher.this, change_news.class);
                //获取map中的数据，并放入intent
                intent_change_news.putExtra("title",map_item.get("title")+"");
                intent_change_news.putExtra("id",map_item.get("id")+"");

                startActivity(intent_change_news);
            }
        });

        //为SwipeRefreshLayout设置颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        //为SwipeRefreshLayout设置监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                look_news();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();

            }
        });


        /**悬浮按钮监听器，增加资讯**/
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.floatingbutton_add_news:
                        startActivity(new Intent(activity_teacher.this,add_news.class));
                        Toast.makeText(activity_teacher.this, "增加资讯", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };
        button_add_news.setOnClickListener(listener);
        circleImageView.setOnClickListener(listener);

    }

    //初始化组件
    public void init() {
        //设置标题栏与状态栏颜色保持一致
        new Common_toolbarColor().toolbarColorSet(activity_teacher.this);

        //设置toolbar
        toolbar = findViewById(R.id.toolbar_teacher);
        setSupportActionBar(toolbar);

        //actionbar设置
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.a);
        }


        //DrawerLayout设置
        drawerLayout = findViewById(R.id.drawerlayout_teacher);

        //获取数据库对象
        db = new CommonDatabase().getSqliteObject(activity_teacher.this, "test_db");

        receive_intent = getIntent();

        swipeRefreshLayout = findViewById(R.id.teacher_SwipeRefreshLayout);

        listView = findViewById(R.id.listview_teacher);

        button_add_news = findViewById(R.id.floatingbutton_add_news);

        //NavigationView绑定及监听子项
        navigationView = findViewById(R.id.navigation_view_t);

        headview = navigationView.inflateHeaderView(R.layout.headlayout_teacher);

        textView_welcome = headview.findViewById(R.id.welcome_textview_teacher);

        circleImageView = headview.findViewById(R.id.circleimage_teacher);

        //表示欢迎的textview
        textView_welcome.setText(receive_intent.getStringExtra("teacher_id"));
        //头像初始化
        imageStore = new image_store();


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    /**显示所有资讯**/
    public void look_news() {

        Cursor cursor = db.query("news", null, null, null, null, null, null);
        //对游标进行遍历
        if (cursor.getCount() == 0) {
            Toast.makeText(activity_teacher.this, "暂时无资讯", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Map<String, String>> arrayList_news = new ArrayList<Map<String, String>>();
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("title", cursor.getString(cursor.getColumnIndex("title")));
                map.put("date", cursor.getString(cursor.getColumnIndex("new_date")));
                map.put("id", cursor.getString(cursor.getColumnIndex("news_id")));
                arrayList_news.add(map);

            }
            ////////////////////
            CoordinatorLayout teacher_coordinatorlayout = findViewById(R.id.teacher_coordinatorlayout);
            Snackbar.make(teacher_coordinatorlayout, "一共有" + arrayList_news.size() + "条资讯", Snackbar.LENGTH_LONG)
                    .setAction("好的", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .show();
            //设置适配器
            SimpleAdapter simpleAdapter = new SimpleAdapter(activity_teacher.this, arrayList_news, R.layout.list_item_allnews,
                    new String[]{"title", "date"}, new int[]{R.id.text_news_title, R.id.text_news_date});
            listView.setAdapter(simpleAdapter);


        }
    }
}
