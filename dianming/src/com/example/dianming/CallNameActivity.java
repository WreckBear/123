package com.example.dianming;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class CallNameActivity extends Activity implements OnClickListener{
	TextView banji,suiji;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newcall);
		
		banji = (TextView)findViewById(R.id.banjidianming);
		suiji = (TextView)findViewById(R.id.suijidianming);
		banji.setOnClickListener(this);
		suiji.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		if(v.getId()==R.id.banjidianming){
			intent = new Intent(CallNameActivity.this,CallNameAll.class);
		}else{
			intent = new Intent(CallNameActivity.this,CallNameRdmActivity.class);
		}
		CallNameActivity.this.startActivity(intent);
	}
	
}
