package com.yedianchina.ui.index;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.R;

public class SendMsg2MerchantUI extends CommonActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.sendmsg2merchant);
		
		
		
		TextView qiandaoBtn = (TextView) this.findViewById(R.id.qiandaoBtn);
		if(qiandaoBtn!=null){
			qiandaoBtn.setVisibility(View.INVISIBLE);
		}
		
		
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					SendMsg2MerchantUI.this.finish();

				}
			});

		}
		//save_btn
		
		TextView save_btn = (TextView) this.findViewById(R.id.save_btn);
		if(save_btn!=null){
			save_btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SendMsg2MerchantUI.this.finish();
					
				}
			});
			
		}
		
		
		
		
	}

}
