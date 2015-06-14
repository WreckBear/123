package com.example.dianming;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CallNameAll extends Activity {
	ListView listView;
	List<String> all;
	PersonDao per = new PersonDao(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callname1_layout);

		listView = (ListView) findViewById(R.id.mingdan_list);
		// ���������
		all = new ArrayList<String>();
		per.setTable("kecheng1");
		List<String[]> tmpList = per.getAllPerson();
		// for (String[] tmpAll : tmpList) {
		// HashMap hash = new HashMap<String, String>();
		// hash.put("ѧ��", tmpAll[0]);
		// hash.put("�༶", tmpAll[3]);
		// hash.put("����", tmpAll[1]);
		// all.add(hash);
		// }
		// SimpleAdapter simple = new SimpleAdapter(this, all,
		// R.layout.liststyle,
		// new String[] { "ѧ��", "����", "�༶" }, new int[] { R.id.t1,
		// R.id.t2, R.id.t3 });
		for (String[] tmpAll : tmpList) {
			String record = tmpAll[0] +"  "+ tmpAll[3] +"  "+ tmpAll[1];
			all.add(record);
		}
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice,all));
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String stuId = all.get(position).split("  ")[0];
				Log.v("zijidingyi", stuId);
				per.upDate(stuId);
			}
		});
	}

}
