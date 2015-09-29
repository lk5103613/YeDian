package com.yedianchina.ui;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchUI extends CommonActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		setContentView(R.layout.search);
		super.checkNetWork();// 检查网络是否连接
		
		ImageView backBtn=(ImageView)this.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SearchUI.this.finish();
				
			}
		});
		
		
		TextView  qiandaoBtn=(TextView)this.findViewById(R.id.qiandaoBtn);
		if(qiandaoBtn!=null){
			qiandaoBtn.setVisibility(View.INVISIBLE);
		}
		
	}
	TextView backBtn;

}
