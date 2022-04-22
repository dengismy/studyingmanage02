package com.example.database_manage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
   该界面主要用于建表，以及设定完整性
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    //带全部参数的构造函数，此构造函数必不可少
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /**学生信息表student**/
        String sql_student = "create table student(" +
                "id int primary key not null ," +
                "name varchar(20) not null," +
                "sex varchar(10) not null," +
                "age int check(age<100 and age>0)," +
                "banji varchar(20) ," +
                "phone int ," +
                "college varchar(20) default '无')" ;

        /**学生——登陆密码表load_account**/
        String sql_student_load = "create table load_account(account int primary Key not null ,password varchar(20))";
        //触发器
        String sql_ctrigger = "CREATE TRIGGER load_add AFTER INSERT ON student  " +
                "BEGIN INSERT INTO load_account(account,password) VALUES (new.id,'e10adc3949ba59abbe56e057f20f883e'); END;";
        String sql_trigger = "CREATE TRIGGER delete_load BEFORE DELETE ON student BEGIN DELETE  FROM load_account WHERE account =old.id ;END ; ";
        String sql_trigger2 = "CREATE TRIGGER student_load_change " +
                " AFTER UPDATE ON student BEGIN UPDATE  load_account SET ACCOUNT = NEW.ID WHERE ACCOUNT = OLD.ID; END;";
        db.execSQL(sql_student_load);
        db.execSQL(sql_student);
        db.execSQL(sql_ctrigger);
        db.execSQL(sql_trigger);
        db.execSQL(sql_trigger2);


        /**工作人员个人信息表teacher**/
        String sql_teacher = "create table teacher(" +
                "teacher_id int primary key not null," +
                "name varchar(20)," +
                "sex  varchar(20) check(sex = '男' or sex = '女')," +
                "age int check(age>0 and age<100)," +
                "level varchar(20)," +
                "phone int ," +
                "college varchar(20))";

        /**老师登录信息表load_teacher**/
        String sql_teacher_load = "create table load_teacher(" +
                "account int primary key not null ," +
                "password varchar(20))";

        //老师邮箱记录表
        String sql_teacher_email = "create table teacher_email(" +
                "account int primary key not null ," +
                "email varchar(20))";

        //触发器
        String sql_ttrigger = "CREATE TRIGGER load_add_teacher AFTER INSERT ON teacher  " +
                "BEGIN INSERT INTO load_teacher(account,password) VALUES (new.teacher_id,'e10adc3949ba59abbe56e057f20f883e'); END;";
        String sql_dttrigger = "CREATE TRIGGER delete_teacher_load " +
                " BEFORE DELETE ON teacher BEGIN DELETE FROM load_teacher WHERE ACCOUNT = OLD.TEACHER_ID; END;";
        String sql_dttrigger1 = "CREATE TRIGGER teacher_load_change " +
                " AFTER UPDATE ON teacher BEGIN UPDATE  load_teacher SET ACCOUNT = NEW.TEACHER_ID WHERE ACCOUNT = OLD.TEACHER_ID; END;";

        db.execSQL(sql_teacher_load);
        db.execSQL(sql_teacher);
        db.execSQL(sql_ttrigger);
        db.execSQL(sql_dttrigger);
        db.execSQL(sql_dttrigger1);
        db.execSQL(sql_teacher_email);

        //学生选课信息表
        String sql_student_course = "create table student_course(" +
                "student_id int ," +
                "course_name varchar(20)," +
                "teacher_name varchar(10)," +
                "score11 float, " +
                "score12 float, " +
                "score21 float, " +
                "score22 float, " +
                "score23 float, " +
                "score31 float, " +
                "score32 float, " +
                "score33 float, " +
                "score34 float, " +
                "score35 flaot, " +
                "score flaot, " +
                "FOREIGN KEY(course_name) REFERENCES course(course_name)," +
                "FOREIGN KEY(teacher_name) REFERENCES course(teacher_name)" +
                ")";
        //课程信息表
        String sql_course = "create table course(" +
                "teacher_name varchar(20) not null," +
                "course_name varchar(20) not null," +
                "course_weight SMALLINT ," +
                "course_time varchar(20)," +
                "course_period varchar(20)," +
                "primary key(teacher_name,course_name)," +
                "FOREIGN KEY(teacher_name) REFERENCES teacher(name))";
        db.execSQL(sql_course);
        db.execSQL(sql_student_course);

        /**资讯news**/
        String sql_news = "create table news(" +
                "news_id varchar(20) PRIMARY KEY," +
                "title varchar(20) not null,"+
                "content text,"+
                "new_date varcahr(20))";
        db.execSQL(sql_news);

        /**留言message**/
        String sql_liuyan = "create table message(" +
                "student_id int primary key not null," +
                "message text)";
        db.execSQL(sql_liuyan);


        /**各学院招生信息表about_college**/
        String sql_about_college = "create table about_college(" +
                "tuition int ," +
                "house int ," +
                "commodities int ," +
                "books int ," +
                "people int, " +
                "college_name varchar(20))";
        db.execSQL(sql_about_college);

        /**管理员表administractor**/
        String sql_admin = "create table administractor(" +
                " account varchar(20)," +
                " password varchar(20))";
        String sql_insert_admin = "insert into administractor values('DJH','e10adc3949ba59abbe56e057f20f883e')";

        db.execSQL(sql_admin);
        db.execSQL(sql_insert_admin);


        //个人资源配置表，比如更改图片之类的啦
        String personal_resource = "create table personal_resource(" +
                "id int primary key not null ," +
                "IMAGE blob)";

        db.execSQL(personal_resource);

        //触发器

        String personal_resource_trigger = "CREATE TRIGGER personal_resource_trigger AFTER INSERT ON student " +
                "BEGIN INSERT INTO personal_resource(id,IMAGE) VALUES (new.id,null);END;";
        String personal_resource_trigger1 = "CREATE TRIGGER personal_resource_trigger_t AFTER INSERT ON teacher " +
                "BEGIN INSERT INTO personal_resource(id,IMAGE) VALUES (new.teacher_id,null);END;";

        db.execSQL(personal_resource_trigger);
        db.execSQL(personal_resource_trigger1);

        //插入各学院招生信息
        String a1 = "insert into about_college(tuition,house,commodities,books,people,college_name) values(8000,1000,200,500,300,'计算机学院')";
        String a2 = "insert into about_college(tuition,house,commodities,books,people,college_name) values(8000,1000,200,500,300,'医学院')";
        String a3 = "insert into about_college(tuition,house,commodities,books,people,college_name) values(8000,1000,200,500,300,'环境学院')";
        String a4 = "insert into about_college(tuition,house,commodities,books,people,college_name) values(8000,1000,200,500,300,'土木水利学院')";
        String a5 = "insert into about_college(tuition,house,commodities,books,people,college_name) values(8000,1000,200,500,300,'法学院')";
        String a6= "insert into about_college(tuition,house,commodities,books,people,college_name) values(8000,1000,200,500,300,'美术学院')";
        String a7 = "insert into about_college(tuition,house,commodities,books,people,college_name) values(8000,1000,200,500,300,'管理学院')";
        String a8 = "insert into about_college(tuition,house,commodities,books,people,college_name) values(8000,1000,200,500,300,'药学院')";
        db.execSQL(a1);db.execSQL(a2);db.execSQL(a3);db.execSQL(a4);db.execSQL(a5);db.execSQL(a6);
        db.execSQL(a7);db.execSQL(a8);

        //插入资讯基本测试数据
        String sql_insert1 = "insert into course(teacher_name,course_name,course_weight,course_time,course_period) values('陈老师','电子线路基础',2,'周一上午','1')";
        String sql_insert2 = "insert into course(teacher_name,course_name,course_weight,course_time,course_period) values('马老师','大学英语4',4,'周五上午','1')";
        String sql_insert4 = "insert into news(news_id,title,content,new_date) values('1','111','11111111','2022.2.2')";
        String sql_insert3 = "insert into news(news_id,title,content,new_date) values('2','2222','2222','2022.2.2')";
        db.execSQL(sql_insert1);
        db.execSQL(sql_insert2);
        db.execSQL(sql_insert4);
        db.execSQL(sql_insert3);


        //插入学生基本测试数据
        String sql_p = "insert into student(id ,name ,sex,age ,banji , phone, college )" +
                " values(3180608001,'顾廷烨','男',19,'计算机科学1802',111111,'计算机学院')";

        String sql_p1 = "insert into student(id ,name ,sex,age ,banji , phone, college )" +
                " values(3180608002,'盛明兰','女',20,'计算机科学1802',111111,'计算机学院')";
        String sql_p2 = "insert into student(id ,name ,sex,age ,banji , phone, college )" +
                " values(3180608003,'齐衡','男',20,'物联网1801',111111,'计算机学院')";
        String sql_p3 = "insert into student(id ,name ,sex,age ,banji , phone, college )" +
                " values(3180608004,'盛墨兰','男',20,'通信1802',111111,'计算机学院')";
        String sql_p4 = "insert into student(id ,name ,sex,age ,banji , phone, college )" +
                " values(3180608005,'林噙霜','男',20,'通信1802',111111,'计算机学院')";

        String sql_tp1 = "insert into teacher(teacher_id ,name ,sex,age ,level , phone, college )" +
                " values(1111,'陈老师','男',30,'高级教师',222222,'计算机学院')";
        String sql_tp = "insert into teacher(teacher_id ,name ,sex,age ,level , phone, college )" +
                " values(1112,'马老师','男',34,'高级教师',222222,'计算机学院')";
        db.execSQL(sql_p);

        db.execSQL(sql_p1);
        db.execSQL(sql_p2);
        db.execSQL(sql_p3);
        db.execSQL(sql_p4);
        db.execSQL(sql_tp);
        db.execSQL(sql_tp1);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
