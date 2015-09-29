package com.yedianchina.ui.nearby;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yedianchina.dao.UserDao;
import com.yedianchina.po.UserPO;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.R;

//附近->TA关注
public class NearbyAttentionUI extends CommonActivity {
	public void updateCurrendData() {
		userPO = UserDao.loadUserInfo(userId);

	}
	private UserPO userPO;
	ImageView  avatarTv;
	TextView  accountTv;
	TextView  nicknameTv;
	TextView  sexTv;
	TextView  birthdayTv;
	TextView  heightTv;
	//
	TextView  weightTv;
	TextView  locTv;
	TextView  hometownTv;
	TextView  qmTv;
	Long  userId;
	TextView sexIV;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.nearby_attention);
		
		TextView  nearbyClassScoreID=(TextView)this.findViewById(R.id.nearbyClassScoreID);
		if(nearbyClassScoreID!=null){
			nearbyClassScoreID.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(NearbyAttentionUI.this, NearbyDengji.class);
					 
					NearbyAttentionUI.this.startActivity(intent);
					
				}
			});
		}
		
		
		
		Bundle bundle = this.getIntent().getExtras();
		final Long pk = bundle.getLong("pk");
        this.userId = pk;
        sexIV = (TextView) this.findViewById(R.id.sexIV);
		avatarTv = (ImageView) this.findViewById(R.id.avatarTv);
		accountTv = (TextView) this.findViewById(R.id.accountTv);
		nicknameTv = (TextView) this.findViewById(R.id.nicknameTv);
		sexTv = (TextView) this.findViewById(R.id.sexTv);
	    birthdayTv = (TextView) this.findViewById(R.id.birthdayTv);
		heightTv = (TextView) this.findViewById(R.id.heightTv);
		
		new Thread() {
			public void run() {

				updateCurrendData();

				loadingHandler.sendEmptyMessage(1);

			}
		}.start();
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					NearbyAttentionUI.this.finish();

				}
			});
		}
		
	 
		TextView nearbyDynamicID=(TextView) this.findViewById(R.id.nearbyDynamicID);
		if(nearbyDynamicID!=null){
			nearbyDynamicID.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
				 
				
					
				}
			});
		}
		
		
		
	}
	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				
				int gender = userPO.getGender();
				if (gender == 1) {
					sexIV.setBackgroundResource(R.drawable.nearby_man);
				} else if (gender == 2) {
					sexIV.setBackgroundResource(R.drawable.nearby_girl);
				} else {
					sexIV.setBackgroundResource(R.drawable.sex);
				}

				int age = userPO.getAge();
				if (age > 0) {
					sexIV.setText(age + "");
				}

			}
		};
	};

}
