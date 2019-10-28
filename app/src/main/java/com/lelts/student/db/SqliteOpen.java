package com.lelts.student.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteOpen extends SQLiteOpenHelper {

	public SqliteOpen(Context context) {
		super(context, "newOriental", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 1:id
		// 
		// 3:autoincrement auto_increment
		String sql = "create table students(_id Integer primary key autoincrement,name varchar(20) not null,age int not null,sex varchar(10) not null);";
		// 执行Sql语句
		db.execSQL(sql);
		/**
		 * 创建 答题表   保存答题时间，答题题号，答题编号，答案
		 */
		db.execSQL("create table Stu_Answer(s_id varchar(20) ,s_code varchar(20) ,s_answer varchar(20),s_time varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
