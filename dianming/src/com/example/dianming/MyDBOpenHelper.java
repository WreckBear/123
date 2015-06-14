package com.example.dianming;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper {

	private String CREATE_TABLE_USERINFO = "CREATE TABLE userInfo (tabName varchar(40));";
	/**
	 * 
	 * @param context	应用程序的上下文    
	 * @param name	数据库的名字
	 * @param factory	 查询数据库的游标工厂 一般情况下用sdk默认的    
	 * @param version	数据库的版本号 版本号必须要大于1      
	 */
	public MyDBOpenHelper(Context context) {
		super(context, "stu.db", null, 5);
	}

	public  boolean tabIsExist(SQLiteDatabase dbInfo,String tabName) {
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
					+ tabName.trim() + "' ";
			cursor = dbInfo.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
		} 
		return result;
	}
	
	/**
	 * 在mydbOpenHelper第一次被new 的时候 会执行 onCreate()
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		if (!tabIsExist(db,"kecheng1")) {
			db.execSQL(CREATE_TABLE_USERINFO);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		
	}

}
