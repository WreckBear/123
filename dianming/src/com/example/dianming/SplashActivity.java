package com.example.dianming;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SplashActivity extends Activity {
	Button button;
	EditText edit1, edit2;
	Intent intent;
	Handler handler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_layout);
		
		CurrentInfo.currentabName = "kecheng1";
		
		handler = new Handler(){
			
			public void handleMessage(Message msg) {
				Toast.makeText(SplashActivity.this, "√‹¬Î¥ÌŒÛ£¨«Î÷ÿ ‘", Toast.LENGTH_SHORT).show();
			}
		};
		edit1 = (EditText) findViewById(R.id.userName);
		edit2 = (EditText) findViewById(R.id.passWord);
		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
//				if (GetDBDate.isTrue(edit1.getText().toString(), edit2.getText().toString())) {
					 intent = new Intent(SplashActivity.this,
							MainActivity.class);
					SplashActivity.this.startActivity(intent);
					finish();
//				}else{
//					handler.sendEmptyMessage(0);
//				}
			}
		});
	}
}
