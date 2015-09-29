package com.yedianchina.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;


//http://bbs.csdn.net/topics/380084274
public class CameraUI extends Activity {
	
	Button paizhao;
	
	Button exitBtn0;

	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_dialog_from_settings);
		// dialog=new MyDialog(this);
		layout = (LinearLayout) findViewById(R.id.exit_layout2);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		Button cancelBtn = (Button) this.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(-1, intent);
				CameraUI.this.finish();

			}
		});
		
		

		
		paizhao= (Button) this.findViewById(R.id.paizhao);
		paizhao.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				 
				 
				setResult(101, intent);
				CameraUI.this.finish();


			}
		});
		//
		exitBtn0= (Button) this.findViewById(R.id.exitBtn0);
		exitBtn0.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				 
				 
				setResult(888, intent);
				CameraUI.this.finish();


			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// finish();
		return true;
	}

	public void exitbutton1(View v) {
		this.finish();
	}

	public void exitbutton0(View v) {
		// this.finish();
		// CameraUI.this.finish();//关闭Main 这个Activity
	}

}
