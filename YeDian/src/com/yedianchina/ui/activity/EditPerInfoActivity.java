package com.yedianchina.ui.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.gson.Gson;
import com.like.entity.RecruitInfo;
import com.yedianchina.ui.R;
import com.yedianchina.ui.index.BaseActivity;

public class EditPerInfoActivity extends BaseActivity {
	
	private RecruitInfo mInfo;
	private EditText mTxtName;
	private EditText mTxtMp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_per_info);
		
		mTxtName = (EditText) findViewById(R.id.name);
		mTxtMp = (EditText) findViewById(R.id.mp);
		
		String json = getIntent().getStringExtra("info");
		System.out.println(json);
		mInfo = new Gson().fromJson(json, RecruitInfo.class);
		
		mTxtName.setText(mInfo.title);
		mTxtMp.setText("123123");
	}

}
