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
						Toast.makeText(ImportActivity.this, "�ɹ�ɾ�����ݿ�",
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
				text.setText("��ѡ��ǰ�γ�");
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
				text.setText("��ǰ�޿γ�");
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
				Toast.makeText(this, "���Ժ����ڶ����ļ�", Toast.LENGTH_SHORT).show();
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
	 * @return ����һ��List<String[]> list���м�¼�ļ��ϣ����У�һ����Ԫ�ؼ���һ�м�¼�����ݣ�
	 *         һ�м�¼��String[]����ʾ���м�¼��������4������Ԫ��<ѧ�ţ�������רҵ���༶>
	 *         ��¼�ĵ�һ���Ǽ�¼��<�γ����ơ�����ѧԺ���ο���ʦ��ѧ��>
	 */
	private List<String[]> getRecord() {
		// �γ����ơ�����ѧԺ���ο���ʦ��ѧ��
		String courseName, college, teacher, grade, absent = "0";
		// ��¼���ϡ�һ�м�¼
		List<String[]> list = new ArrayList<String[]>();

		// ��ȡexcel�ļ�����
		Workbook book = null;
		try {
			book = Workbook.getWorkbook(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ��ȡ������
		Sheet sheet = book.getSheet(0);

		// ��ȡ�γ�����
		courseName = sheet.getCell(4, 2).getContents();
		// ��ȡ����ѧԺ
		college = sheet.getCell(0, 3).getContents();
		// ��ȡ�ο���ʦ
		teacher = sheet.getCell(3, 3).getContents();
		// ��ȡѧ��
		grade = sheet.getCell(4, 3).getContents();
		// ���γ���Ϣ�Ž�list��
		String[] info = { courseName, college, teacher, grade };
		list.add(info);

		// ��ȡ��¼---ѧ�ţ�������רҵ���༶
		for (int i = 5; i < sheet.getRows(); i++) {
			String[] record = new String[5];
			// ��ȡ�м�¼
			Cell[] cell = sheet.getRow(i);
			// ��2��3��4��5�м�¼��¼����
			for (int j = 1; j < 5; j++) {
				String tmp = cell[j].getContents();
				record[j - 1] = tmp;
			}
			record[4] = "0";
			// ����ǲ��ǿ��У������list��
			if (!"".equals(cell[0].getContents()))
				list.add(record);
		}
		return list;
	}

	/**
	 * ���̵߳Ĺ���Ϊ����ȡ�ļ�����Ϣ������Ϣ�������ݿ���
	 * 
	 * @author С��
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
			Toast.makeText(ImportActivity.this, "����ɹ�", Toast.LENGTH_SHORT)
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
