package com.yedianchina.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.WeiboSDK;
import com.sina.weibo.sdk.api.BaseResponse;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.sina.weibo.sdk.api.IWeiboHandler;
import com.sina.weibo.sdk.api.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.weibo.sdk.android.demo.ConstantS;
import com.yedianchina.dao.AppConfigDao;

public class WBShareUI extends  Activity implements OnClickListener, IWeiboHandler.Response {
	IWeiboAPI weiboAPI;
	String shareMsg;
	Handler loginHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
		 
			if (what == 1) {
				initView();

			}

		};
	};
	private void initView() {

		EditText contentEt= (EditText) findViewById(R.id.contentEt);
		contentEt.setText(shareMsg);
 
		
		
		TextView tmp=(TextView)findViewById(R.id.loginBtn);
		tmp.setVisibility(View.INVISIBLE);
		
		TextView cancelSuggestTv=(TextView)findViewById(R.id.cancelSuggestTv);
		cancelSuggestTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				WBShareUI.this.finish();
			}
		});
		
		TextView btn=(TextView)findViewById(R.id.sendSuggest);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				reqTextMsg();		
				
					}
			
			
		});

		
		
		
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_to_wx);
		weiboAPI = WeiboSDK.createWeiboAPI(this, ConstantS.APP_KEY);
		weiboAPI.registerApp();
		
		weiboAPI.responseListener(getIntent(), this);
		new Thread() {
			public void run() {
				AppConfigDao  dao=new AppConfigDao();
				shareMsg=dao.getWechatInvite();
			 
					loginHandler.sendEmptyMessage(1);
				

			}
		}.start();
		
		
	}
	private void reqTextMsg() {
	   
        WeiboMessage weiboMessage = new WeiboMessage();
        weiboMessage.mediaObject = getTextObj();
        SendMessageToWeiboRequest req = new SendMessageToWeiboRequest();
        req.transaction = String.valueOf(System.currentTimeMillis());// 用transaction唯一标识一个请求
        req.message = weiboMessage;
        // 发送请求消息到微博
        weiboAPI.sendRequest(this, req);
    }
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = "HHHJk";
        return textObject;
    }
    @Override
    protected void onNewIntent( Intent intent ) {
        super.onNewIntent(intent);
        setIntent(intent);
        weiboAPI.responseListener(getIntent(), this);
    }
    @Override
    public void onResponse( BaseResponse baseResp ) {
        switch (baseResp.errCode) {
        case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_OK:
            Toast.makeText(this, "成功！！", Toast.LENGTH_LONG).show();
            break;
        case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_CANCEL:
            Toast.makeText(this, "用户取消！！", Toast.LENGTH_LONG).show();
            break;
        case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_FAIL:
            Toast.makeText(this, baseResp.errMsg + ":失败！！", Toast.LENGTH_LONG).show();
            break;
        }

    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
