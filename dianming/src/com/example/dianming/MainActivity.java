package com.example.dianming;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

public class MainActivity extends TabActivity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.tabhost);
		
		Resources rs = getResources();
		TabHost tabhost = getTabHost();
		TabHost.TabSpec spc;
		Intent intent;
		
		intent = new Intent(this, CallNameActivity.class);
		spc = tabhost.newTabSpec("点名").setIndicator("点名").setContent(intent);
		tabhost.addTab(spc);
		
		intent = new Intent(this, CountActivity.class);
		spc = tabhost.newTabSpec("统计").setIndicator("统计").setContent(intent);
		tabhost.addTab(spc);
		
		intent = new Intent(this,MyInfoActivity.class);
		spc = tabhost.newTabSpec("我的").setIndicator("我的").setContent(intent);
		tabhost.addTab(spc);
		
		
		tabhost.setCurrentTab(2);
	}
	
}
//public class MainActivity extends Activity implements OnClickListener{
//
//	TextView textView,textView1,textView2,textView3,textView4,textView5;
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.start_layout);
//		
//		textView = (TextView) findViewById(R.id.btn1);
//		textView1 = (TextView) findViewById(R.id.btn2);
//		textView2= (TextView) findViewById(R.id.btn3);
//		textView3 = (TextView) findViewById(R.id.btn4);
//		textView4 = (TextView) findViewById(R.id.btn5);
//		textView5 = (TextView) findViewById(R.id.btn6);
//		
//		textView.setOnClickListener(this);
//		textView1.setOnClickListener(this);
//		textView2.setOnClickListener(this);
//		textView3.setOnClickListener(this);
//		textView4.setOnClickListener(this);
//		textView5.setOnClickListener(this);
//	}
//
//
//	@Override
//	public void onClick(View v) {
//		Intent intent = null;
//		switch (v.getId()) {
//		case R.id.btn1:
//			intent = new Intent(this,ImportActivity.class);
//			break;
//		case R.id.btn2:
//			intent = new Intent(this,CallNameActivity.class);
//			break;
//		case R.id.btn3:
//			intent = new Intent(this,CountActivity.class);
//			break;
//		case R.id.btn4:
//			intent = new Intent(this,CallNameRdmActivity.class);
//			break;
//		case R.id.btn5:
//			intent = new Intent(this,BlackListActivity.class);
//			break;
//		case R.id.btn6:
//			intent = new Intent(this,MoreActivity.class);
//			break;
//		default:
//			break;
//		}
//		this.startActivity(intent);
//	}

