package com.yedianchina.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yedianchina.dao.CommentService;
//评价司机
public class CommentUI extends CommonActivity {
	double star;
	Long orderId;
	RatingBar    ratingBar;
	protected void onResume() {
		if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 }
		super.onResume();
	}
	private TextView  linkMp;
	private String validateMp;
	String content;
	private boolean updateCurrendData() {
	
		SharedPreferences userInfo = getSharedPreferences("yedianchina_user_info",
				Activity.MODE_PRIVATE);
		String customerMp = userInfo.getString("mp", "");
		
		return CommentService.saveComment(content, star, customerMp,orderId);

	}

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {

				
				int what = msg.what;
				if (what == 1) {
					CommentUI.this.finish();

				}
			} catch (Exception ex) {

			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comment_driver);
		
		orderId=this.getIntent().getExtras().getLong("id");
	 
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		//linkMp=(TextView)this.findViewById(R.id.linkMp);
		// 手机界面标题设置
		TextView titleTxtView = (TextView) findViewById(R.id.tvHeaderTitle);
		titleTxtView.setText("评价司机");
		
		ratingBar=(RatingBar)findViewById(R.id.ratingBar);
		
		
		// loginBtn
		TextView loginBtn = (TextView) findViewById(R.id.loginBtn);
		loginBtn.setVisibility(View.INVISIBLE);

		TextView back_btn = (TextView) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CommentUI.this.finish();
			}
		});
		SharedPreferences userInfo = getSharedPreferences("yedianchina_user_info", Activity.MODE_PRIVATE);  
		String _mp = userInfo.getString("mp", "");  
		if(_mp!=null&&_mp.length()==11){
			this.validateMp=_mp;
			
					
		}

		final EditText contentEt = (EditText) this.findViewById(R.id.contentEt);

		

		YeDianChinaApplication.getInstance().addActivity(this);
		//
		TextView loginTv=(TextView)this.findViewById(R.id.loginTv);
		loginTv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CommentUI.this.finish();
				
			}
		});
		
		TextView saveComment=(TextView)this.findViewById(R.id.saveComment);
		saveComment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				content=contentEt.getText().toString();
				
				star=ratingBar.getRating();
			 
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {

							updateCurrendData();
							loadingHandler.sendEmptyMessage(1);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
				
			}
		});

	}

}
