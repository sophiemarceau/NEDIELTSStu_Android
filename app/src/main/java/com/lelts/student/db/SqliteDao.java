package com.lelts.student.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lelts.students.bean.StuAnswerInfo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


@SuppressLint("SimpleDateFormat")
public class SqliteDao {
	private SQLiteDatabase mSqliteDatabase;
	private SqliteOpen mSqliteOpen;
	private Cursor mCursor;
	private List<?> mList;

	//Sqlitedatabase;
	public SQLiteDatabase getDB(Context context){
		mSqliteOpen=new SqliteOpen(context);
		mSqliteDatabase=mSqliteOpen.getWritableDatabase();
		return mSqliteDatabase;		
	}
	
	public Cursor getCursor(Context context){
		mSqliteDatabase=getDB(context);
		String sql="select * from students";
		mCursor=mSqliteDatabase.rawQuery(sql, null);
//		mList=new ArrayList<E>();
		return mCursor;
		}
	
	public void getAdd(Context context,String name,int age,String sex){
		mSqliteDatabase=getDB(context);
		String sql="insert into students (name,age,sex) values (?,?,?)";
		mSqliteDatabase.execSQL(sql, new Object[]{name,age,sex});
	}
	
	public Cursor getUpdate(Context context){
		mSqliteDatabase=getDB(context);
		
		return mCursor;
		}
	
	// 添加答案的方法  题号，答案， 时间
	public void InsertInfoAnswer(Context context,String s_id,String s_code,String s_answer) {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		mSqliteDatabase=getDB(context);
		ContentValues values = new ContentValues();
		values.put("s_id", s_id);
		values.put("s_code", s_code);
		values.put("s_answer", s_answer);
		values.put("s_time", format.format(new Date()));
		mSqliteDatabase.insert("Stu_Answer", null, values);
		mSqliteDatabase.close();
	}
	//更改答案的方法  根据题号  s_id  来更改答案
	
	public void UpdateInfoAnswer(Context context,String s_id, String s_answer) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		mSqliteDatabase=getDB(context);
		ContentValues values = new ContentValues();
		values.put("s_answer", s_answer);
		values.put("s_time", format.format(new Date()));
		mSqliteDatabase.update("Stu_Answer", values, "s_id=?", new String[] { s_id });
		mSqliteDatabase.close();
	}
	//查询所有的答案
	public List<StuAnswerInfo> GetallAnswer(Context context) {
		mSqliteDatabase=getDB(context);
		List<StuAnswerInfo> list = new ArrayList<StuAnswerInfo>();
		Cursor cursor = mSqliteDatabase.query(false, "Stu_Answer", null, null, null, null, null,
				null, null);
	
		StuAnswerInfo stuinfo = null;
		while (cursor.moveToNext()) {
			String s_answer = cursor.getString(cursor.getColumnIndex("s_answer"));
			String s_time = cursor.getString(cursor.getColumnIndex("s_time"));
			String s_code = cursor.getString(cursor.getColumnIndex("s_code"));
			String s_id = cursor.getString(cursor.getColumnIndex("s_id"));
		    stuinfo = new StuAnswerInfo(s_id, s_code, s_answer, s_time);
			list.add(stuinfo);
		}
	
		cursor.close();
		mSqliteDatabase.close();
		return list;

	}
	//按s_id 题号来查找

	public Boolean SelectBys_id(Context context,String s_id) {
		mSqliteDatabase=getDB(context);
		Cursor query = mSqliteDatabase.query("Stu_Answer", null, "s_id=?",
				new String[] { s_id }, null, null, null);
		if (query.moveToNext()) {
			return true;
		} else {
			return false;
		}

	}
	//根据s_id 来查找
	public String setAnswer(Context context,String s_id) {
		String s_answer = null;
		mSqliteDatabase=getDB(context);
		Cursor cursor = mSqliteDatabase.query("Stu_Answer", null, "s_id = ?",
				new String[] { s_id }, null, null, null);
		while (cursor.moveToNext()) {
			s_answer = cursor.getString(cursor.getColumnIndex("s_answer"));
		}
		cursor.close();
		mSqliteDatabase.close();
		return s_answer;

	}
	
	//根据s_id 来查找 时间
	public String getCostTime(Context context,String s_id) {
		String s_answer = null;
		mSqliteDatabase=getDB(context);
		Cursor cursor = mSqliteDatabase.query("Stu_Answer", null, "s_id = ?",
				new String[] { s_id }, null, null, null);
		while (cursor.moveToNext()) {
			s_answer = cursor.getString(cursor.getColumnIndex("s_answer"));
		}
		cursor.close();
		mSqliteDatabase.close();
		return s_answer;

	}
	
	//删除数据库
	public void deleteDatabase(Context context) {
		
		mSqliteDatabase=getDB(context);
		mSqliteDatabase.delete("Stu_Answer", null, null);
		mSqliteDatabase.close();
		}
	
}
