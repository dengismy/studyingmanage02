package com.example.database_manage.teacher;

import android.Manifest;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database_manage.R;
import com.example.database_manage.administractor.change_account_teacher;
import com.example.database_manage.database.CommonDatabase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class save_email extends AppCompatActivity{
    private Button btn;
    private Button sendBtn;
    private EditText emailEdit;
    private SQLiteDatabase db;
    private static final String reg = "\\w+[\\w]*@[\\w]+\\.[\\w]+$";
    Intent intent;
    String teacher_id;
    SimpleDateFormat simpleDateFormat;
    //用于传递信息
    Date date;
    String mydate;
    String strFilePath;
    String strFileName;
    String email01;//老师默认地址
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_email);
        //返回上一级
        btn=findViewById(R.id.button_email_back);
        sendBtn=findViewById(R.id.button_email_save);
        emailEdit=findViewById(R.id.email_address);
        db = new CommonDatabase().getSqliteObject(save_email.this, "test_db");
        //获取上个activity穿过来的intent
         intent = getIntent();

        //获取系统的日期
         simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
      //获取当前时间
        date = new Date(System.currentTimeMillis());
         mydate= getDate();
//外部存储地址
        strFilePath ="/sdcard/DJH";
        strFileName=mydate+"mymessage.txt";
        Toast.makeText(save_email.this,"文件名称"+strFileName, Toast.LENGTH_SHORT).show();
        String path01=Environment.getExternalStorageDirectory().getPath()+File.separator + strFilePath + File.separator+strFileName;
     //   Toast.makeText(save_email.this,"外部存储路径"+path01, Toast.LENGTH_SHORT).show();

//获得老师邮箱并保存
        //初始化数据库对象
        CommonDatabase commonDatabase = new CommonDatabase();
        db = commonDatabase.getSqliteObject(save_email.this,"test_db");
        email01="";
        Cursor cursor = db.query("teacher_email", null, "account = ?", new String[]{intent.getStringExtra("teacher_id")}, null, null, null);
        while (cursor.moveToNext()) {
            email01=cursor.getString(cursor.getColumnIndex("email"));
        }
        if(email01.equals("")){
            Toast.makeText(save_email.this, "您还没有默认邮箱地址请输入有效邮箱地址", Toast.LENGTH_SHORT).show();
        }else{
            emailEdit.setText(email01);
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           String emailss= String.valueOf(emailEdit.getText());
           //判断邮箱是否合法
                if ((emailss.matches(reg)&&emailss.endsWith(".com"))) {
//合法
                    if(email01.equals("")){     //当是老师第一次输入邮箱的时候进行存储
                        ContentValues values = new ContentValues();
                        values.put("account",intent.getStringExtra("teacher_id"));
                        values.put("email", emailss);
                        db.insert("teacher_email",null,values);
                    }
                       savetoText();
                       sendEmail(emailss);
                } else {
//非法
                    Toast.makeText(save_email.this, "该邮箱不合法", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendEmail(String emailaddress) {
        Intent email = new Intent(android.content.Intent.ACTION_SEND);
//邮件发送类型：无附件，纯文本
        email.setType("text/plain");
//邮件接收者（数组，可以是多位接收者）
        String[] emailReciver = new String[]{emailaddress};
        String  emailTitle = "学生成绩备份";
        String emailContent =simpleDateFormat.format(date);

        //  File file =FileUtils.makeFilePath(strFilePath,strFileName);
        String mypath=Environment.getExternalStorageDirectory() + strFilePath + File.separator+strFileName;
        //Toast.makeText(save_email.this,"附件地址"+mypath, Toast.LENGTH_SHORT).show();
        System.out.println("附件"+mypath+mydate);

        // File file = new File("/storage/emulated/0/sdcard/DJH/message.txt"); //附件文件地址
        File file = new File(mypath); //附件文件地址
    //设置邮件地址
        email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
    //设置邮件标题
        email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailTitle);
   //设置发送的内容
        email.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));

        email.putExtra(android.content.Intent.EXTRA_TEXT, emailContent);
        //调用系统的邮件系统
        startActivity(Intent.createChooser(email, "请选择邮件发送软件"));

    }


    private String getDate() {
        /*
         * 使用Calendar获取系统时间
         */
        Calendar calendars;
            calendars = Calendar.getInstance();
            calendars.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            String year = String.valueOf(calendars.get(Calendar.YEAR));
            String month = String.valueOf(calendars.get(Calendar.MONTH));
            String day = String.valueOf(calendars.get(Calendar.DATE));
            String hour = String.valueOf(calendars.get(Calendar.HOUR));
            String min = String.valueOf(calendars.get(Calendar.MINUTE));
            String second = String.valueOf(calendars.get(Calendar.SECOND));
           String mm=year+"年"+month+"月"+day+"日"+hour+"："+min+"："+second;
            Log.i("md", " 年："+year+" 月： "+month+" 日："+day+" 时： "+hour+" 分： "+min+" 秒： "+second);
            return  mm;
    }


    private void savetoText() {

        String topMessage="学生姓名      学号         学科        成绩";
        FileUtils.writeTxtToFile(topMessage, strFilePath, strFileName);

        //去查找目前登录老师的信息
        Cursor cursor_about = db.query("teacher", null, "teacher_id = ?", new String[]{intent.getStringExtra("teacher_id")}, null, null, null);
        String tes_name="";
        while (cursor_about.moveToNext()) {
          tes_name = cursor_about.getString(cursor_about.getColumnIndex("name"));
        }
        System.out.println("老师是"+tes_name);
       //去查找目前登录老师的信息
        Cursor cursor_1 = db.rawQuery(
                "select * from student inner join student_course " +
                        "on student.id =student_course.student_id " +
                        "where teacher_name= ?" +
                        "order by name ASC", new String[]{tes_name});
        while (cursor_1.moveToNext()) {
            String oneMessage =cursor_1.getString(cursor_1.getColumnIndex("name")) + "  "+cursor_1.getString(cursor_1.getColumnIndex("student_id"))+"  "+
                    cursor_1.getString(cursor_1.getColumnIndex("course_name"))+"    "+cursor_1.getString(cursor_1.getColumnIndex("score"));
            FileUtils.writeTxtToFile(oneMessage, strFilePath, strFileName);
        }
    }


}
