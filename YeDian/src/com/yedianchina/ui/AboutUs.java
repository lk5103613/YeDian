package com.yedianchina.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

import com.yedianchina.tools.CONSTANTS;

public class AboutUs extends CommonActivity {
	
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
		setContentView(R.layout.about_us);

		TextView tvHeaderTitle = (TextView) this
				.findViewById(R.id.tvHeaderTitle);
		tvHeaderTitle.setText("关于夜店中国");
		

		
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setBackgroundColor(0);  
		
		mWebView.loadUrl(CONSTANTS.HOST+"index.php/AppConfig/aboutUs");
		
		

	 
		 
			

		 
				

		TextView back_btn = (TextView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AboutUs.this.finish();

			}
		});
		YeDianChinaApplication.getInstance().addActivity(this);

	}
}
