package com.yedianchina.ui.index;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.like.network.DataFetcher;

public class BaseActivity extends Activity {
	
	protected Context mContext;
	protected DataFetcher mDataFetcher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mDataFetcher = DataFetcher.getInstance(mContext);
	}
	
	public void back(View v) {
		this.finish();
	}

}
