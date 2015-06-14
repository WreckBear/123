package com.example.dianming;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyInfoActivity extends Activity{
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_layout);
		
		listView = (ListView)findViewById(R.id.allinfo);
		String[] item = new String[]{"个人信息","我的课程","关于"};
		listView.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1, item));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==1){
					Intent intent = new Intent(MyInfoActivity.this,ImportActivity.class);
					MyInfoActivity.this.startActivity(intent);
				}
				else if(position==2){
					Intent intent = new Intent(MyInfoActivity.this,MoreActivity.class);
					MyInfoActivity.this.startActivity(intent);
				}
			}
		});
	}
	
}
