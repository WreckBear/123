package com.example.dianming;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;

public class ImportActivity extends Activity implements OnCheckedChangeListener {
	MyHandler handler;
	Button importButton, deleteButton;
	TextView text;
	InputStream is = null;
	File file;
	RadioGroup rg;

	private static final int REQUEST_CHOOSER = 1234;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_layout);
		handler = new MyHandler();

		text = (TextView) findViewById(R.id.kechengshu);
		rg = (RadioGroup) findViewById(R.id.radioGroup);
		deleteButton = (Button) findViewById(R.id.deleteButton);
		// deleteButton.setEnabled(false);
		deleteButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ImportActivity.this.deleteDatabase("stu.db");
				deleteButton.setEnabled(false);
				importButton.setEnabled(true);
				handler.post(new Runnable() {

					public void run() {
						Toast.makeText(ImportActivity.this, "成功删除数据库",
								Toast.LENGTH_SHORT).show();
						haveCourse();
					}
				});
			}
		});
		importButton = (Button) findViewById(R.id.importButton);
		importButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent getContentIntent = FileUtils.createGetContentIntent();
				Intent intent = Intent.createChooser(getContentIntent,
						"Select a file");
				startActivityForResult(intent, REQUEST_CHOOSER);

			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		haveCourse();
	}

	private void haveCourse() {
		MyDBOpenHelper myHelper = new MyDBOpenHelper(this);
		SQLiteDatabase db = myHelper.getReadableDatabase();
		boolean result = false;
		Cursor cursor1 = null;
		try {
			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='userInfo' ";
			cursor1 = db.rawQuery(sql, null);
			if (cursor1.moveToNext()) {
				int count = cursor1.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
		}
		if (result) {
			Cursor cursor = db.rawQuery("select * from userInfo",
					new String[] {});
			cursor.moveToFirst();
			System.out.println("||||||||||||||||||||||||||||||||||||||||||");
			if (cursor.getCount() != 0) {
				text.setText("请选择当前课程");
				do {
					System.out
							.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					String tabName = cursor.getString(cursor
							.getColumnIndex("tabName"));
					CheckBox radio = new CheckBox(this);
					radio.setTextColor(Color.WHITE);
					radio.setText(tabName);
					rg.addView(radio);
					rg.setOnCheckedChangeListener(this);
				} while (cursor.moveToNext());
			} else {
				text.setText("当前无课程");
			}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CHOOSER:
			if (resultCode == RESULT_OK) {
				final Uri uri = data.getData();
				// Get the File path from the Uri
				String path = FileUtils.getPath(this, uri);
				// Alternatively, use FileUtils.getFile(Context, Uri)
				if (path != null && FileUtils.isLocal(path)) {
					file = new File(path);
				}
				Toast.makeText(this, "请稍后，正在读入文件", Toast.LENGTH_SHORT).show();
				try {
					is = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				new TaskThread().start();
				importButton.setEnabled(false);
			}
			break;
		}

	}

	/**
	 * 
	 * @return 返回一个List<String[]> list是行记录的集合，其中，一个子元素即是一行记录的数据，
	 *         一行记录用String[]来表示，行记录中其中有4个数组元素<学号，姓名，专业，班级>
	 *         记录的第一行是记录着<课程名称、开课学院、任课老师、学分>
	 */
	private List<String[]> getRecord() {
		// 课程名称、开课学院、任课老师、学分
		String courseName, college, teacher, grade, absent = "0";
		// 记录集合、一行记录
		List<String[]> list = new ArrayList<String[]>();

		// 获取excel文件对象
		Workbook book = null;
		try {
			book = Workbook.getWorkbook(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 获取工作薄
		Sheet sheet = book.getSheet(0);

		// 获取课程名称
		courseName = sheet.getCell(4, 2).getContents();
		// 获取开课学院
		college = sheet.getCell(0, 3).getContents();
		// 获取任课老师
		teacher = sheet.getCell(3, 3).getContents();
		// 获取学分
		grade = sheet.getCell(4, 3).getContents();
		// 将课程信息放进list中
		String[] info = { courseName, college, teacher, grade };
		list.add(info);

		// 获取记录---学号，姓名，专业，班级
		for (int i = 5; i < sheet.getRows(); i++) {
			String[] record = new String[5];
			// 获取行记录
			Cell[] cell = sheet.getRow(i);
			// 将2、3、4、5列记录记录下来
			for (int j = 1; j < 5; j++) {
				String tmp = cell[j].getContents();
				record[j - 1] = tmp;
			}
			record[4] = "0";
			// 如果是不是空行，则加入list中
			if (!"".equals(cell[0].getContents()))
				list.add(record);
		}
		return list;
	}

	/**
	 * 改线程的功能为：读取文件的信息，将信息填入数据库中
	 * 
	 * @author 小猪
	 * 
	 */

	class TaskThread extends Thread {
		public void run() {
			PersonDao person = new PersonDao(ImportActivity.this);
			person.newTable();
			List<String[]> list = getRecord();
			int i = 0;
			for (String[] hang : list) {
				if (i != 0)
					person.add(hang);
				i++;
			}
			Message mess = new Message();
			Bundle bu = new Bundle();
			bu.putBoolean("123", true);
			mess.setData(bu);
			handler.sendMessage(mess);
		}
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(ImportActivity.this, "导入成功", Toast.LENGTH_SHORT)
					.show();
			deleteButton.setEnabled(true);
			haveCourse();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		CheckBox che = (CheckBox) findViewById(checkedId);
		CurrentInfo.currentabName = che.getText().toString();
	}
}
