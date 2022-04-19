package com.example.database_manage.student;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database_manage.R;
import com.example.database_manage.database.CommonDatabase;
import com.example.database_manage.login.load;

import static com.example.database_manage.database.MD5Demo.md5;


/***
 * 用于学生修改密码
 */

public class stu_password_change extends AppCompatActivity {
    private  SQLiteDatabase db;
    private  Button button_change;
    private  EditText edit_account;
    private  EditText edit_oldpassword ;
    private  EditText edit_newpassword ;
    private  EditText edit_confirm ;
    private  String true_password ="";
    private  String state = "";
    private  TextInputLayout textInputLayout;
    private  Button button_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stu_password_change);

        initView();

        //为按钮设置监听器
        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    check("load_account"); }

        });
        //为返回按钮设置监听器
        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //为新密码进行检查
        edit_newpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(textInputLayout.getCounterMaxLength()<edit_newpassword.length())
                {
                    textInputLayout.setError("超出字数限制！");
                }
                else
                {
                    textInputLayout.setErrorEnabled(false);
                }
            }
        });

    }

    //绑定组件
    public void initView()
    {
        button_change = findViewById(R.id.button_change_password_ok);

        edit_account = findViewById(R.id.et_c_account);

        edit_oldpassword = findViewById(R.id.et_c_oldpassword);

        edit_newpassword = findViewById(R.id.et_c_newpassword);

        textInputLayout = findViewById(R.id.textinput_newpassword);

        edit_confirm = findViewById(R.id.et_c_confirm_newpassword);

        db = new CommonDatabase().getSqliteObject(com.example.database_manage.student.stu_password_change.this,"test_db");
        button_return=findViewById(R.id.button_return);
    }
    //检验正确性
    public  void  check(String string)
    {
        if(edit_account.getText().toString().equals("")||edit_confirm.getText().toString().equals("")||edit_newpassword.getText().toString().equals("")||edit_oldpassword.getText().toString().equals(""))
        {
            Toast.makeText(com.example.database_manage.student.stu_password_change.this,"不能为空！",Toast.LENGTH_SHORT).show();
        }
        else
        {
            //去找真正密码
            Cursor cursor =db.query(string,null,"account = ?",new String[]{edit_account.getText().toString()},null,null,null);
            //如果根本没这个账户
            if(cursor.getCount()==0)
            {
                Toast.makeText(com.example.database_manage.student.stu_password_change.this,"没有找到该账户",Toast.LENGTH_SHORT).show();
            }
            else
            {
                while (cursor.moveToNext())
                {
                    true_password = cursor.getString(cursor.getColumnIndex("password"));
                }

                //如果原密码错误
                if(!md5(edit_oldpassword.getText().toString()).equals(true_password))
                {
                    Toast.makeText(com.example.database_manage.student.stu_password_change.this,"原密码错误！",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //如果用户前后输入密码不同
                    if(!edit_newpassword.getText().toString().equals(edit_confirm .getText().toString()))
                    {
                        Toast.makeText(com.example.database_manage.student.stu_password_change.this,"前后两次输入不一致！",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        ContentValues values = new ContentValues();
                        values.put("password",md5(edit_newpassword.getText().toString()));
                        //更新数据库
                        db.update(string, values, "account = ? ", new String[]{edit_account.getText().toString()});

                        Toast.makeText(com.example.database_manage.student.stu_password_change.this,"修改成功！",Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(com.example.database_manage.student.stu_password_change.this, load.class));
                    }
                }

            }



        }
    }
}
