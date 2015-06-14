package com.example.dianming;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class CallNameRdmActivity extends Activity {
	Button nextButton;
	List<String[]> list;
	ListView listView;
	int max;
	int stuNum = 10;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callname2_layout);

		listView  = (ListView) findViewById(R.id.date);
		nextButton = (Button) findViewById(R.id.next);
		PersonDao per = new PersonDao(this);
		list = per.getAllPerson();
		max = list.size();
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showName();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu set = menu.addSubMenu("…Ë÷√");
		return true;
	}

	private void showName() {
		String[] toAdd = new String[stuNum];
		for (int i = 0; i < stuNum; i++) {
			String[] finalValue = list.get((int) (Math.random() * max));
			toAdd[i] = finalValue[0] + " " + finalValue[3] + " "
					+ finalValue[1];
		}
		listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, toAdd));
	}
}