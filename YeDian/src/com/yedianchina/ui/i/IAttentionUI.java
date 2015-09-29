package com.yedianchina.ui.i;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yedianchina.dao.UserDao;
import com.yedianchina.po.UserPO;
import com.yedianchina.ui.R;
import com.yedianchina.ui.nearby.NearByAttentionFragment;
import com.yedianchina.ui.nearby.NearByDengjiFragment;
import com.yedianchina.ui.nearby.NearByDynamicFragment;
import com.yedianchina.ui.nearby.NearByXiuChangFragment;
import com.yedianchina.ui.nearby.NearbyUserUI;
import com.yedianchina.ui.nearby.NearbyUserUI.MyOnClickListener;
import com.yedianchina.ui.recruit.FragmentViewPagerAdapter;

//关注
public class IAttentionUI extends FragmentActivity {

	TextView attentionMerchantBtn;
	TextView attentionPersonBtn;

	private ViewPager viewPager;
	public List<Fragment> fragments = new ArrayList<Fragment>();
	Long pk;

	private void initViewPager() {
		IAttentionMerchantFragment a = IAttentionMerchantFragment.initFm(
				IAttentionUI.this, pk);
		fragments.add(a);

		IAttentionUserFragment b = IAttentionUserFragment.initFm(IAttentionUI.this,pk);
		fragments.add(b);

		IAttentionMerchantFragment c = IAttentionMerchantFragment.initFm(
				IAttentionUI.this, pk);
		fragments.add(c);

		viewPager = (ViewPager) findViewById(R.id.vPager);
		FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(
				this.getSupportFragmentManager(), viewPager, fragments);
		adapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener() {
			@Override
			public void onExtraPageSelected(int i) {
				System.out.println("Extra...i: " + i);
				if (i == 0) {
					attentionMerchantBtn
							.setBackgroundResource(R.drawable.ktv_focus);

					attentionPersonBtn.setBackgroundResource(R.drawable.ktv);

				}
				if (i == 1) {
					attentionMerchantBtn.setBackgroundResource(R.drawable.ktv);

					attentionPersonBtn
							.setBackgroundResource(R.drawable.ktv_focus);

				}

			}
		});

		attentionMerchantBtn.setOnClickListener(new MyOnClickListener(0));
		attentionPersonBtn.setOnClickListener(new MyOnClickListener(1));

	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			if (index == 0) {
				attentionMerchantBtn
						.setBackgroundResource(R.drawable.ktv_focus);

				attentionPersonBtn.setBackgroundResource(R.drawable.ktv);

			}

			if (index == 1) {
				attentionMerchantBtn.setBackgroundResource(R.drawable.ktv);

				attentionPersonBtn.setBackgroundResource(R.drawable.ktv_focus);
			}

			viewPager.setCurrentItem(index);
		}
	};

	 
	public void updateCurrendData() {
		userPO = UserDao.loadUserInfo(userId);

	}

	private UserPO userPO;
	ImageView avatarTv;
	TextView accountTv;
	TextView nicknameTv;
	TextView sexTv;
	TextView birthdayTv;
	TextView heightTv;
	//
	TextView weightTv;
	TextView locTv;
	TextView hometownTv;
	TextView qmTv;
	Long userId;
	TextView sexIV;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.i_attention);
		
		attentionMerchantBtn=(TextView)this.findViewById(R.id.attentionMerchantBtn);
		attentionPersonBtn=(TextView)this.findViewById(R.id.attentionPersonBtn);

		
		initViewPager();

//		Bundle bundle = this.getIntent().getExtras();
//		final Long pk = bundle.getLong("pk");
//		this.userId = pk;
//		sexIV = (TextView) this.findViewById(R.id.sexIV);
//		avatarTv = (ImageView) this.findViewById(R.id.avatarTv);
//		accountTv = (TextView) this.findViewById(R.id.accountTv);
//		nicknameTv = (TextView) this.findViewById(R.id.nicknameTv);
//		sexTv = (TextView) this.findViewById(R.id.sexTv);
//		birthdayTv = (TextView) this.findViewById(R.id.birthdayTv);
//		heightTv = (TextView) this.findViewById(R.id.heightTv);
//
//		new Thread() {
//			public void run() {
//
//				updateCurrendData();
//
//				loadingHandler.sendEmptyMessage(1);
//
//			}
//		}.start();
		
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					IAttentionUI.this.finish();

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
