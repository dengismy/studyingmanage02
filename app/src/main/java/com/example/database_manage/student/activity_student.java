package com.example.database_manage.student;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database_manage.administractor.change_news;
import com.example.database_manage.school_map.map_Activity;
import com.example.database_manage.teacher.activity_teacher;
import com.example.database_manage.utils.Common_toolbarColor;
import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;
import com.example.database_manage.database.image_store;
import com.example.database_manage.utils.alertDialog_builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_student extends AppCompatActivity {
    private SQLiteDatabase db;

    //切换用户弹出的对话框
    private AlertDialog.Builder builder;

    //Toolbar用于替代原有的actionBar
    private Toolbar toolbar;

    //用于显示学生选课信息的listview
    private ListView listView_news;

    //侧滑
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //用于获取NavigationView的headlayout，方便监听子项
    private View headview;

    //headlayout中的textview
    private TextView textView_welcome;

    //headlayout中circleimage
    private CircleImageView circleImageView;


    private Uri imageUri;

    private static final int TAKE_PHOTO = 1;

    private image_store imageStore;

    private Intent intent_1;

    //悬浮按钮也就是加号，用于选课
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student);
        //获取登录信息，以锁定用户
        intent_1 = getIntent();

        initView();
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            //设置左箭头图片
            actionBar.setHomeAsUpIndicator(R.drawable.a);

        }

        //headlayout中的欢迎实现
        textView_welcome.setText(findNameById(intent_1.getStringExtra("student_id")));


        //菜单栏实现
        navigationView.setCheckedItem(R.id.nav_menu_myinfo);
        navigationView.setCheckedItem(R.id.nav_menu_changeacc);


        //设置标题栏与状态栏颜色保持一致
        new Common_toolbarColor().toolbarColorSet(activity_student.this);

        //头像初始化
        Bitmap bitmap_temp = imageStore.getBmp(db, intent_1.getStringExtra("student_id"));

        if (bitmap_temp != null) {
            circleImageView.setImageBitmap(bitmap_temp);
        }


        //NavigationView的菜单项监听器
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    /**更新个人信息**/
                    case R.id.nav_menu_myinfo:
                        Intent intent_about = new Intent(activity_student.this, about_me.class);
                        intent_about.putExtra("student_id", intent_1.getStringExtra("student_id"));
                        startActivity(intent_about);
                        break;
                    /**查看我的二维码**/
                    case R.id.nav_menu_mycode:
                        Toast.makeText(activity_student.this,"查看我的二维码",Toast.LENGTH_LONG).show();
                        break;

                    /**退出登录**/
                    case R.id.nav_menu_changeacc:
                        builder = new alertDialog_builder(activity_student.this).build();
                        //显示出该对话框
                        builder.show();
                        break;
                    /**留言**/
                    case R.id.nav_menu_liuyan:
                        Intent intent_submit = new Intent(activity_student.this, submit_message.class);
                        intent_submit.putExtra("student_id", intent_1.getStringExtra("student_id"));
                        startActivity(intent_submit);
                        break;

                        /**查看缴费情况**/
                    case R.id.nav_menu_mypay:
                        //再将从登陆界面接受的学生学号传给选择课程的活动
                        Intent intent_2 = new Intent(activity_student.this, choose_course.class);
                        intent_2.putExtra("student_id", intent_1.getStringExtra("student_id"));
                        startActivity(intent_2);
                        break;

                       /**查看宿舍安排情况**/
                    case R.id.nav_menu_mydormitory:
                      Toast.makeText(activity_student.this,"查看宿舍",Toast.LENGTH_LONG).show();
                        break;

                    /**修改密码**/
                    case R.id.nav_menu_change_stuaccount:
                        Intent intent_change01 = new Intent(activity_student.this,stu_password_change.class);
                        startActivity(intent_change01);
                        break;
                    /**查看校园地图**/
                    case R.id.nav_menu_school_map:
                        Intent intent_map = new Intent(activity_student.this,map_Activity.class);
                        startActivity(intent_map);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });


        //为view设定监听器
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.floatingbutton_mysearch:
                        Intent intent_search_news = new Intent(activity_student.this, search_news.class);
                        startActivity(intent_search_news);
                     Toast.makeText(activity_student.this,"搜索资讯",Toast.LENGTH_LONG).show();
                    default:
                        break;
                }
            }
        };
        //为listview设定监听器
        listView_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /****点击资讯进行查看****/
                HashMap<String,Object > map_item = (HashMap<String,Object >)listView_news.getItemAtPosition(position);
                Intent intent_change_news = new Intent(activity_student.this, show_news_detail.class);
                //获取map中的数据，并放入intent
                intent_change_news.putExtra("title",map_item.get("title")+"");
                intent_change_news.putExtra("id",map_item.get("id")+"");

                startActivity(intent_change_news);
            }
        });

        floatingActionButton.setOnClickListener(listener);
        circleImageView.setOnClickListener(listener);

    }


    private void initView() {

        //获取数据库对象
        db = new CommonDatabase().getSqliteObject(activity_student.this, "test_db");

        listView_news = findViewById(R.id.listview_news);

        toolbar = findViewById(R.id.toolbar_student);

        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerlayout_student);

        navigationView = findViewById(R.id.navigation_view);

        headview = navigationView.inflateHeaderView(R.layout.headlayout);

        textView_welcome = headview.findViewById(R.id.welcome_textview);

        circleImageView = headview.findViewById(R.id.circleimage);

        floatingActionButton = findViewById(R.id.floatingbutton_mysearch);

        imageStore = new image_store();

        /**初始化主页面资讯显示所有资讯**/
        Cursor cursor = db.query("news", null, null, null, null, null, null);
        //对游标进行遍历
        if (cursor.getCount() == 0) {
            Toast.makeText(activity_student.this, "暂时无资讯", Toast.LENGTH_SHORT).show();
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
            SimpleAdapter simpleAdapter = new SimpleAdapter(activity_student.this, arrayList_news, R.layout.list_item_allnews,
                    new String[]{"title", "date"}, new int[]{R.id.text_news_title, R.id.text_news_date});
            listView_news.setAdapter(simpleAdapter);
        }

    }

    //
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

    //根据用户的学号去查找姓名
    public String findNameById(String id) {
        Cursor cursor = db.query("student", null, "id = ?", new String[]{id}, null, null, null, null);

        //如果没查到
        if (cursor.getCount() == 0) {
            return "无法获取您的个人信息";
        } else {
            String str = "";
            while (cursor.moveToNext()) {
                str = cursor.getString(cursor.getColumnIndex("name"));
            }
            return str + " 欢迎您！";
        }

    }

}
