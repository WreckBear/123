package com.example.dianming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CountActivity extends Activity {
	ListView listView;
	TextView text;
	LinearLayout linearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.count_layout);

		text = (TextView) findViewById(R.id.toast);
		linearLayout = (LinearLayout) findViewById(R.id.instudct);
		listView = (ListView) findViewById(R.id.count);
	}

	@Override
	protected void onStart() {
		super.onStart();
		System.out.println(">>>>>>>>>>>>>..");
		if (tabIsExist("kecheng1")) {
			linearLayout.setVisibility(View.VISIBLE);
			listView.setVisibility(View.VISIBLE);
			text.setVisibility(View.GONE);
			count();
		} else {
			linearLayout.setVisibility(View.GONE);
			listView.setVisibility(View.GONE);
			text.setVisibility(View.VISIBLE);
		}
	}

	public boolean tabIsExist(String tabName) {
		SQLiteDatabase dbInfo = new MyDBOpenHelper(this).getReadableDatabase();
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
			// TODO: handle exception
		} finally {
			dbInfo.close();
		}
		return result;
	}

	private void count() {
		// 存放数据用
		List<HashMap<String, String>> all = new ArrayList<HashMap<String, String>>();
		PersonDao per = new PersonDao(this);
		List<String[]> tmpList = per.getAllPerson();
		for (String[] tmpAll : tmpList) {
			HashMap hash = new HashMap<String, String>();
			hash.put("学号", tmpAll[0]);
			hash.put("班级", tmpAll[3]);
			hash.put("姓名", tmpAll[1]);
			hash.put("缺勤", tmpAll[4]);
			all.add(hash);
		}
		SimpleAdapter simple = new SimpleAdapter(this, all, R.layout.liststyle,
				new String[] { "学号", "班级", "姓名", "缺勤" }, new int[] { R.id.t1,
						R.id.t2, R.id.t3, R.id.t4 });
		listView.setAdapter(simple);

	}
}
