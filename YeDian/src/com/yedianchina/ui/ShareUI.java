package com.yedianchina.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareUI extends CommonActivity {
	CheckBox checkBox;

	protected void onResume() {

		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

	private TextView weibo = null;// 微博
	private TextView qq = null; // QQ空间
	private TextView wechat = null;
	private TextView friends_quan = null;

	private TextView mail = null;
	private TextView msg = null;
	private TextView copy_zy = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.share);
		super.checkNetWork();// 检查网络是否连接

		// 手机界面标题设置
		TextView navigateTitle = (TextView) findViewById(R.id.NavigateTitle);
		if (navigateTitle != null) {
			navigateTitle.setText("分享");
		}

		super.onCreate(savedInstanceState);

		weibo = (TextView) findViewById(R.id.weibo);
		weibo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
			}
		});
		
		
		qq = (TextView) findViewById(R.id.qq);
		qq.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
			}
		});


		wechat = (TextView) findViewById(R.id.wechat);
		wechat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
			}
		});


		friends_quan = (TextView) findViewById(R.id.friends_quan);
		friends_quan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
			}
		});


		mail = (TextView) findViewById(R.id.mail);
		mail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
			}
		});
		
		msg = (TextView) findViewById(R.id.msg);
		msg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
			}
		});
		copy_zy = (TextView) findViewById(R.id.copy_zy);
		copy_zy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
			}
		});

		TextView qiandaoBtn = (TextView) findViewById(R.id.qiandaoBtn);
		if (qiandaoBtn != null) {
			qiandaoBtn.setVisibility(View.INVISIBLE);
		}
		
		
		ImageView backBtn = (ImageView) findViewById(R.id.backBtn);
		if (backBtn != null) {
			ShareUI.this.finish();
		}
		
		TextView cancel_share = (TextView) findViewById(R.id.cancel_share);//取消分享
		if (cancel_share != null) {
			ShareUI.this.finish();
		}
		
		

	}

}
