package com.yedianchina.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

import com.yedianchina.tools.CONSTANTS;

public class ServiceAgreementUI extends CommonActivity {
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		mWebView.setVisibility(View.GONE);//防止webview崩溃
		mWebView.destroy();
	}
	
	private WebView mWebView;
	
	
	protected void onResume() {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.service_agreement);

		TextView tvHeaderTitle = (TextView) this
				.findViewById(R.id.tvHeaderTitle);
		tvHeaderTitle.setText("夜店中国服务协议");
		

		
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setBackgroundColor(0);  
		
		mWebView.loadUrl(CONSTANTS.HOST+"index.php/AppConfig/serviceAggrement");
		
		

	 
		 
			

		 
				

		TextView back_btn = (TextView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ServiceAgreementUI.this.finish();

			}
		});
		
		final SharedPreferences preferences = getSharedPreferences(
				"yedianchina_user_info", Activity.MODE_PRIVATE);
		int alreadyRead = preferences.getInt("alreadyRead", 0);
		final TextView alreadyReadID = (TextView) this
				.findViewById(R.id.already_raead);
		if (alreadyRead == 0) {

			alreadyReadID.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					alreadyReadID.setVisibility(View.INVISIBLE);

					SharedPreferences.Editor editor = preferences.edit();
					editor.putInt("alreadyRead", 1);

					editor.commit(); // 一定要记得提交

				}
			});
		} else {
			alreadyReadID.setVisibility(View.INVISIBLE);
		}
		YeDianChinaApplication.getInstance().addActivity(this);

	}
}
