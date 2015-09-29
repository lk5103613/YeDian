package com.yedianchina.ui.more;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yedianchina.dao.FeedBackDao;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.R;

public class FeedBackUI extends CommonActivity {
	EditText contentEt;
	String content;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.feed_back);
        
    	TextView titleTxtView = (TextView) findViewById(R.id.NavigateTitle);
		titleTxtView.setText("意见反馈");
        
        ImageView  backBtn=(ImageView)this.findViewById(R.id.backBtn);
		if(backBtn!=null){
			backBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					FeedBackUI.this.finish();
					
				}
			});
		}
	 
		
		contentEt=(EditText)this.findViewById(R.id.contentEt);
		content=contentEt.getText().toString();
		TextView  saveBtn=(TextView)this.findViewById(R.id.saveBtn);
		if(saveBtn!=null){
			saveBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					new Thread() {
						@Override
						public void run() {
							FeedBackDao.saveFeedBack(content);
							Message msg=new Message();
							msg.what=1;
							verHandler.sendMessage(msg);
						}
					}.start();
					
				}
			});
		}
		
		// 取消按钮
				TextView qiandaoBtn = (TextView) this.findViewById(R.id.qiandaoBtn);
				if(qiandaoBtn!=null){
					qiandaoBtn.setVisibility(View.INVISIBLE);
				}
		
	}
	Handler  verHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {

				int what = msg.what;
				if (what == 1) {
					Toast.makeText(FeedBackUI.this, "发送成功", Toast.LENGTH_SHORT)
					.show();
					contentEt.setText("");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
	};

}
