package com.weibo.sdk.android.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.WeiboSDK;
import com.sina.weibo.sdk.api.BaseResponse;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.sina.weibo.sdk.api.IWeiboHandler;
import com.yedianchina.ui.R;

public class RequestMessageActivity extends Activity implements
		OnClickListener, IWeiboHandler.Response {

	/** 微博OpenAPI访问入口 */
	IWeiboAPI mWeiboAPI = null;

	private TextView mWebpageContent;
	@SuppressWarnings("unused")
	private TextView mWebpageUrl;

	/** 分享音乐 */
	private TextView mMusicTitle;
	private ImageView mMusicImage;
	private TextView mMusicContent;
	@SuppressWarnings("unused")
	private TextView mMusicUrl;

	/** 分享视频 */
	private TextView mVideoTitle;
	private ImageView mVideoImage;
	private TextView mVideoContent;
	@SuppressWarnings("unused")
	private TextView mVideoUrl;

	@SuppressWarnings("unused")
	private TextView mVoiceUrl;

	/**
	 * CheckBox 和 RadioButton用于控制分享的内容， 用户可以同时分享文本、图片和其它媒体资源（网页、音乐、视频、声音中的一种）
	 */
	private CheckBox mTextCb;
	private CheckBox mImageCb;
	private RadioButton mWebpageRadio;
	private RadioButton mMusicRadio;
	private RadioButton mVideoRadio;
	private RadioButton mVoiceRadio;

	/** 分享按钮 */
	private Button mSharedBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reqmessage);
		initViews();

		// 创建微博对外接口实例
		mWeiboAPI = WeiboSDK.createWeiboAPI(this, ConstantS.APP_KEY);
		mWeiboAPI.responseListener(getIntent(), this);
		 
				System.out.println("Thread is running.");
				mWeiboAPI.registerApp();
				
		 

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mWeiboAPI.responseListener(intent, this);
	}

	/**
	 * 从本应用->微博->本应用 接收响应数据，该方法被调用。 注意：确保{@link #onCreate(Bundle)} 与
	 * {@link #onNewIntent(Intent)}中， 调用 mWeiboAPI.responseListener(intent,
	 * this)
	 */
	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_OK:
			Toast.makeText(this, "成功！！", Toast.LENGTH_LONG).show();
			break;
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_CANCEL:
			Toast.makeText(this, "用户取消！！", Toast.LENGTH_LONG).show();
			break;
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_FAIL:
			Toast.makeText(this, baseResp.errMsg + ":失败！！", Toast.LENGTH_LONG)
					.show();
			break;
		}
	}

	/**
	 * 用户点击分享按钮，唤起微博客户端进行分享。
	 */
	@Override
	public void onClick(View v) {
		if (R.id.sharedBtn == v.getId()) {
			mWeiboAPI.registerApp();
			
		}
	}

	private void initViews() {
		mTextCb = (CheckBox) findViewById(R.id.sharedTextCb);
		mImageCb = (CheckBox) findViewById(R.id.sharedImageCb);

		MyCheckedChangeListener listener = new MyCheckedChangeListener();
		mWebpageRadio = (RadioButton) findViewById(R.id.sharedWebpageCb);
		mWebpageRadio.setOnCheckedChangeListener(listener);
		mMusicRadio = (RadioButton) findViewById(R.id.sharedMusicCb);
		mMusicRadio.setOnCheckedChangeListener(listener);
		mVideoRadio = (RadioButton) findViewById(R.id.sharedVedioCb);
		mVideoRadio.setOnCheckedChangeListener(listener);
		mVoiceRadio = (RadioButton) findViewById(R.id.sharedVoiceCb);
		mVoiceRadio.setOnCheckedChangeListener(listener);

		mSharedBtn = (Button) findViewById(R.id.sharedBtn);
		mSharedBtn.setOnClickListener(this);

		mMusicTitle = (TextView) findViewById(R.id.music_title);
		mMusicImage = (ImageView) findViewById(R.id.music_image);
		mMusicContent = (TextView) findViewById(R.id.music_desc);
		mMusicUrl = (TextView) findViewById(R.id.music_url);

		mVideoTitle = (TextView) findViewById(R.id.video_title);
		mVideoImage = (ImageView) findViewById(R.id.video_image);
		mVideoContent = (TextView) findViewById(R.id.video_desc);
		mVideoUrl = (TextView) findViewById(R.id.video_url);

		mWebpageContent = (TextView) findViewById(R.id.webpage_desc);
		mWebpageUrl = (TextView) findViewById(R.id.webpage_url);

		mVoiceUrl = (TextView) findViewById(R.id.voice_url);

	}

	private class MyCheckedChangeListener implements
			android.widget.CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			mWebpageRadio.setChecked(false);
			mMusicRadio.setChecked(false);
			mVideoRadio.setChecked(false);
			mVoiceRadio.setChecked(false);

			buttonView.setChecked(isChecked);
		}
	}

	

}
