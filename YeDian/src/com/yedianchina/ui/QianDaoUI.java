package com.yedianchina.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class QianDaoUI extends Activity{  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
		
		setContentView(R.layout.qiandao);
		TextView textViewTitle = (TextView) findViewById(R.id.tvHeaderTitle);
		textViewTitle.setText("签到");
		//
		TextView qiandaoBtn=(TextView)this.findViewById(R.id.qiandaoBtn);
		if(qiandaoBtn!=null){
			qiandaoBtn.setVisibility(View.INVISIBLE);
		}
		
		
		//跳过
		TextView tiaoguo_btn=(TextView)this.findViewById(R.id.tiaoguo_btn);
		if(tiaoguo_btn!=null){
			tiaoguo_btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					QianDaoUI.this.finish();
					
				}
			});
			
		}
		
		TextView qiandao_btn=(TextView)this.findViewById(R.id.qiandao_btn);
		if(qiandao_btn!=null){
			qiandao_btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					QianDaoUI.this.finish();
					
				}
			});
			
		}
		
		
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					QianDaoUI.this.finish();

				}
			});
		}
		
	}

}
