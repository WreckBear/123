package com.example.dianming;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PersonDao {

	static int tabNum = 0;
	private String tableName;
	private static final String TAG = "PersonDao";
	private MyDBOpenHelper dbOpenHelper;
	
	// ��personDao��new������ʱ�����ɳ�ʼ��
	public PersonDao(Context context) {
		dbOpenHelper = new MyDBOpenHelper(context);
	}

	public boolean tabIsExist(String tabName) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		boolean result = false;
		if (tabName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
					+ tabName + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} 
		db.close();
		return result;
	}

	public void setTable(String tabName) {
		this.tableName = tableName;
	}
	
	/**
	 * ����
	 * 
	 * @param tableName
	 *            ����
	 */
	public void newTable() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("CREATE TABLE "
				+ ("kecheng"+(tabNum + 1) + "")
				+ "(stuId varchar(40),name varchar(20),magor varchar(40),calss varchar(40),truancyNum varchar(5));");
		db.execSQL("insert into " + "userInfo"
				+ " (tabName) values (?)", new String[]{"kecheng"+(tabNum + 1) + ""});
		db.close();
		// �ر����ݿ⣬�ͷ����ݿ������
	}

	// ��ɾ�Ĳ�
	/**
	 * �����ݿ������һ������
	 * 
	 */
	public void add(String[] s) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL(
					"insert into "
							+ CurrentInfo.currentabName
							+ " (stuId,name,magor,calss,truancyNum) values (?,?,?,?,?)",
					s);
		}
		db.close();
	}

	/**
	 * �������ݿ�Ĳ���
	 * 
	 */
	public boolean find(String name) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		boolean result = false;
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + tableName
					+ " where name=?", new String[] { name });
			if (cursor.moveToFirst()) {
				int index = cursor.getColumnIndex("phone");// �õ�phone�ֶ��ڵڼ���
				String phone = cursor.getString(index);
				Log.i(TAG, "phone=" + phone);
				result = true;
			}
			cursor.close();
			result = false;
		}
		db.close();
		return result;
	}

	/**
	 * ɾ�����ݿ��е�����
	 */

	public void delete(String name) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from " + tableName + " where name=?",
					new Object[] { name });
		}
	db.close();
	}

	/**
	 * ����һ������
	 */
	public void upDate(String stuId) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			// Cursor cursor = db.rawQuery("select truancyNum from " + tableName
			// + " where stuId=?", new String[] { stuId });
			Cursor cursor = db.query(CurrentInfo.currentabName, new String[] { "truancyNum" },
					"stuId=?", new String[] { stuId }, null, null, null);
			cursor.moveToFirst();
			String truancyNums = cursor.getString(cursor
					.getColumnIndex("truancyNum"));
			int truancyNum = Integer.parseInt(truancyNums) + 1;
			// db.execSQL("update " + tableName
			// + " set truancyNum = ? where stuId=?", new Object[] {
			// truancyNum, stuId });
			ContentValues cv = new ContentValues();
			cv.put("truancyNum", truancyNum + "");
			db.update(CurrentInfo.currentabName, cv, "stuId=?", new String[] { stuId });
			cursor.close();
		}
		db.close();
	}

	/**
	 * ����ȫ��
	 * 
	 */

	public List<String[]> getAllPerson() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		List<String[]> stus = null;
		if (db.isOpen()) {
			stus = new ArrayList<String[]>();
			Cursor cursor = db
					.rawQuery("select * from " + CurrentInfo.currentabName + "", null);
			cursor.moveToFirst();
			do {
				String[] stu = new String[5];
				stu[0] = cursor.getString(cursor.getColumnIndex("stuId"));
				stu[1] = cursor.getString(cursor.getColumnIndex("name"));
				stu[2] = cursor.getString(cursor.getColumnIndex("magor"));
				stu[3] = cursor.getString(cursor.getColumnIndex("calss"));
				stu[4] = cursor.getString(cursor.getColumnIndex("truancyNum"));
				stus.add(stu);
			} while (cursor.moveToNext());
			cursor.close();
		}
		db.close();
		return stus;
	}
}
