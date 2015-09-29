package com.yedianchina.ui.nearby;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.activity.ChatActivity;
import com.easemob.chat.activity.MainActivity;
import com.easemob.chat.domain.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.yedianchina.dao.AttentionDao;
import com.yedianchina.dao.UserDao;
import com.yedianchina.po.UserPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.LoginUI;
import com.yedianchina.ui.R;
import com.yedianchina.ui.YeDianChinaApplication;
import com.yedianchina.ui.recruit.FragmentViewPagerAdapter;

public class NearbyUserUI extends FragmentActivity {
	String distance;// 距离 从列表界面传递过来 因为计算太损耗性能 只要计算一次即可
	Handler gzHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;

			if (what == 1) {
				nearby_gz.setImageResource(R.drawable.nearby_gz_focus);
				gzHandler.removeMessages(1);
				Toast toast = Toast.makeText(getApplicationContext(), "关注成功",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			}
			if (what == 7) {

				Toast toast = Toast.makeText(getApplicationContext(),
						"您已经关注该用户，请勿重复关注", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

		};
	};
	ImageView nearby_gz;

	private ViewPager viewPager;
	public List<Fragment> fragments = new ArrayList<Fragment>();
	public String hello = "hello ";
	Long pk;
	private long userId;
	private UserPO userPO;

	public void updateCurrendData() {
		userPO = UserDao.loadUserInfo(userId);

	}

	private YeDianChinaApplication mApplication;
	private ProgressDialog progressDialog;
	TextView mingpianBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.nearby_user_fragment_activity);
		pk = this.getIntent().getExtras().getLong("pk");
		this.userId = pk;
		resources = getResources();
		mApplication = YeDianChinaApplication.getInstance();
		// mMsgDB = mApplication.getMessageDB();

		astroTv = (TextView) this.findViewById(R.id.astroTv);

		this.titleTxtView = (TextView) findViewById(R.id.tvHeaderTitle);

		userAvatar = (ImageView) this.findViewById(R.id.avatarTv);

		distanceTv = (TextView) this.findViewById(R.id.distanceTv);
		sexIV = (TextView) this.findViewById(R.id.sexIV);

		nearbyDynamicID = (ImageView) findViewById(R.id.nearbyDynamicID);// 动态
		nearbyXiuChangID = (ImageView) findViewById(R.id.nearbyXiuChangID);// 秀场
		nearbyAttentionID = (ImageView) this
				.findViewById(R.id.nearbyAttentionID);// 关注
		nearbyClassScoreID = (ImageView) this
				.findViewById(R.id.nearbyClassScoreID);// 等级

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

					NearbyUserUI.this.finish();

				}
			});
		}

		// ///////////////////
		// 底部 关注
		nearby_gz = (ImageView) this.findViewById(R.id.nearby_gz);
		if (nearby_gz != null) {
			nearby_gz.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					SharedPreferences preferences = getSharedPreferences(
							"yedianchina_user_info", Activity.MODE_PRIVATE);
					final Long uid = preferences.getLong("uid", 0);
					Intent intent = new Intent();
					if (uid == 0) {
						intent.setClass(NearbyUserUI.this, LoginUI.class);

						startActivity(intent);

					} else {
						new Thread() {
							public void run() {
								int code = AttentionDao.nearbyBottomGz(uid,
										userId);
								gzHandler.sendEmptyMessage(code);

							}
						}.start();
					}

				}
			});
		}

		// 底部菜单-发消息
		nearby_sendmsg = (ImageView) this.findViewById(R.id.nearby_sendmsg);
		nearby_sendmsg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferences preferences = getSharedPreferences(
						CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);
				// 判断下：自己资料是否完善

				Long uid = preferences.getLong(CONSTANTS.UID, 0);

				String nickname = userPO.getNickname();
				if (nickname == null || nickname.length() == 0) {// 该用户资料没有完善
																	// 您不能与TA聊天
					Toast.makeText(getApplicationContext(),
							"对方资料尚未完善资料，无法与您聊天", 1).show();
					return;
				} else {
					if (uid == null || uid == 0) {
						Toast.makeText(getApplicationContext(), "您尚未登录,请先登录", 1)
								.show();
						return;
					}
					String myNickname = preferences.getString(
							CONSTANTS.NICKNAME, "");
					if (myNickname == null || myNickname.length() == 0
							|| "".equals(myNickname)) {
						Toast.makeText(getApplicationContext(),
								"您的资料不完善，请去“我的中心－》右上角我的名片处完善个人资料 ", 1).show();
						return;
					}
					Intent intent = new Intent(NearbyUserUI.this,
							ChatActivity.class);

					intent.putExtra("userId", userPO.getMp());
					startActivity(intent);

				}

			}
		});

		// 右上角名片
		mingpianBtn = (TextView) this.findViewById(R.id.mingpianBtn);// 名片
		if (mingpianBtn != null) {
			mingpianBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(NearbyUserUI.this, MingpianUI.class);

					intent.putExtra("pk", String.valueOf(pk));
					NearbyUserUI.this.startActivity(intent);

				}
			});
		}
		//
		nearbyDynamicID.setImageResource(R.drawable.nearby_user_dynamic20);

		nearbyXiuChangID.setImageResource(R.drawable.nearby_user_dynamic21);

		nearbyAttentionID.setImageResource(R.drawable.nearby_user_dynamic22);

		nearbyClassScoreID.setImageResource(R.drawable.nearby_user_dynamic23);

	}

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {

				int what = msg.what;
				if (what == 1) {
					setNearbyUserDetailData();
					InitViewPager();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private void setNearbyUserDetailData() {
		TextView tvHeaderTitle = (TextView) findViewById(R.id.NavigateTitle);
		if (tvHeaderTitle != null) {
			String nickName = userPO.getNickname();
			if (nickName != null && nickName.length() > 0) {
				tvHeaderTitle.setText(nickName);
			}
		}
		// 只有登录才可以
		SharedPreferences preferences = getSharedPreferences(
				CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);
		Long uid = preferences.getLong(CONSTANTS.UID, 0);

		if (uid != null && uid > 0) {

			Map<String, User> userlist = YeDianChinaApplication.getInstance()
					.getContactList();

			User user = new User();
			user.setUsername(userPO.getMp());

			userlist.put(userPO.getMp(), user);

			YeDianChinaApplication.getInstance().setContactList(userlist);
			// 存入db
			com.easemob.chat.db.UserDao dao = new com.easemob.chat.db.UserDao(
					NearbyUserUI.this);
			List<User> users = new ArrayList<User>(userlist.values());
			dao.saveContactList(users);
		}

		//

		String logo = userPO.getAvatar();
		if (logo != null && logo.length() > 5) {

			Log.e("头像", logo);
			userAvatar.setTag(logo);

			ImageLoader.getInstance().displayImage(logo, userAvatar);
		} else {
			userAvatar.setBackgroundResource(R.drawable.nearby_u2);
		}

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
		String astro = userPO.getAstro();

		astroTv.setText(astro);

	}

	TextView titleTxtView;
	ImageView userAvatar;
	TextView sexIV;
	TextView distanceTv;
	TextView astroTv;

	ImageView nearby_sendmsg;

	private void InitViewPager() {
		NearByDynamicFragment a = NearByDynamicFragment.initFm(
				NearbyUserUI.this, pk);
		fragments.add(a);

		NearByXiuChangFragment b = NearByXiuChangFragment.initFm(pk);
		fragments.add(b);

		NearByAttentionFragment c = NearByAttentionFragment.initFm(
				NearbyUserUI.this, pk);
		fragments.add(c);

		NearByDengjiFragment d = NearByDengjiFragment.initFm(pk);
		fragments.add(d);

		viewPager = (ViewPager) findViewById(R.id.vPager);
		FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(
				this.getSupportFragmentManager(), viewPager, fragments);
		adapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener() {
			@Override
			public void onExtraPageSelected(int i) {
				System.out.println("Extra...i: " + i);
				if (i == 0) {
					nearbyDynamicID
							.setImageResource(R.drawable.nearby_user_dynamic20);

					nearbyXiuChangID
							.setImageResource(R.drawable.nearby_user_dynamic21);

					nearbyAttentionID
							.setImageResource(R.drawable.nearby_user_dynamic22);

					nearbyClassScoreID
							.setImageResource(R.drawable.nearby_user_dynamic23);
				}
				if (i == 1) {
					nearbyDynamicID.setImageResource(R.drawable.dynamic_normal);

					nearbyXiuChangID
							.setImageResource(R.drawable.nearby_user_dynamic21_focus);

					nearbyAttentionID
							.setImageResource(R.drawable.nearby_user_dynamic22);

					nearbyClassScoreID
							.setImageResource(R.drawable.nearby_user_dynamic23);
				}

				if (i == 2) {
					nearbyDynamicID.setImageResource(R.drawable.dynamic_normal);

					nearbyXiuChangID
							.setImageResource(R.drawable.nearby_user_dynamic21);

					nearbyAttentionID
							.setImageResource(R.drawable.nearby_user_dynamic22_focus);

					nearbyClassScoreID
							.setImageResource(R.drawable.nearby_user_dynamic23);
				}
				if (i == 3) {
					nearbyDynamicID.setImageResource(R.drawable.dynamic_normal);

					nearbyXiuChangID
							.setImageResource(R.drawable.nearby_user_dynamic21);

					nearbyAttentionID
							.setImageResource(R.drawable.nearby_user_dynamic22);

					nearbyClassScoreID
							.setImageResource(R.drawable.nearby_user_dynamic23_focus);
				}

			}
		});

		nearbyDynamicID.setOnClickListener(new MyOnClickListener(0));
		nearbyXiuChangID.setOnClickListener(new MyOnClickListener(1));

		nearbyAttentionID.setOnClickListener(new MyOnClickListener(2));
		nearbyClassScoreID.setOnClickListener(new MyOnClickListener(3));
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			if (index == 0) {
				nearbyDynamicID
						.setImageResource(R.drawable.nearby_user_dynamic20);

				nearbyXiuChangID
						.setImageResource(R.drawable.nearby_user_dynamic21);

				nearbyAttentionID
						.setImageResource(R.drawable.nearby_user_dynamic22);

				nearbyClassScoreID
						.setImageResource(R.drawable.nearby_user_dynamic23);
			}

			if (index == 1) {
				nearbyDynamicID.setImageResource(R.drawable.dynamic_normal);

				nearbyXiuChangID
						.setImageResource(R.drawable.nearby_user_dynamic21);

				nearbyAttentionID
						.setImageResource(R.drawable.nearby_user_dynamic22);

				nearbyClassScoreID
						.setImageResource(R.drawable.nearby_user_dynamic23);
			}

			if (index == 2) {
				nearbyDynamicID.setImageResource(R.drawable.dynamic_normal);

				nearbyXiuChangID
						.setImageResource(R.drawable.nearby_user_dynamic21);

				nearbyAttentionID
						.setImageResource(R.drawable.nearby_user_dynamic22_focus);

				nearbyClassScoreID
						.setImageResource(R.drawable.nearby_user_dynamic23);
			}
			if (index == 3) {
				nearbyDynamicID.setImageResource(R.drawable.dynamic_normal);

				nearbyXiuChangID
						.setImageResource(R.drawable.nearby_user_dynamic21);

				nearbyAttentionID
						.setImageResource(R.drawable.nearby_user_dynamic22);

				nearbyClassScoreID
						.setImageResource(R.drawable.nearby_user_dynamic23_focus);
			}

			viewPager.setCurrentItem(index);
		}
	};

	private Resources resources;
	private int currIndex = 0;
	private ImageView nearbyDynamicID, nearbyXiuChangID, nearbyAttentionID,
			nearbyClassScoreID;

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			Log.e("onPageSelected.arg0==" + arg0, "" + arg0);
			Animation animation = null;
			switch (arg0) {
			case 0:

				nearbyDynamicID
						.setImageResource(R.drawable.nearby_user_dynamic20);

				nearbyXiuChangID
						.setImageResource(R.drawable.nearby_user_dynamic21);

				nearbyAttentionID
						.setImageResource(R.drawable.nearby_user_dynamic22);

				nearbyClassScoreID
						.setImageResource(R.drawable.nearby_user_dynamic23);

				viewPager.setCurrentItem(0);

				break;
			case 1:

				nearbyDynamicID.setImageResource(R.drawable.dynamic_normal);

				nearbyXiuChangID
						.setImageResource(R.drawable.nearby_user_dynamic21);

				nearbyAttentionID
						.setImageResource(R.drawable.nearby_user_dynamic22);

				nearbyClassScoreID
						.setImageResource(R.drawable.nearby_user_dynamic23);
				viewPager.setCurrentItem(1);
				break;
			case 2:

				nearbyDynamicID.setImageResource(R.drawable.dynamic_normal);

				nearbyXiuChangID
						.setImageResource(R.drawable.nearby_user_dynamic21);

				nearbyAttentionID
						.setImageResource(R.drawable.nearby_user_dynamic22_focus);

				nearbyClassScoreID
						.setImageResource(R.drawable.nearby_user_dynamic23);
				viewPager.setCurrentItem(2);
				break;

			case 3:

				nearbyDynamicID.setImageResource(R.drawable.dynamic_normal);

				nearbyXiuChangID
						.setImageResource(R.drawable.nearby_user_dynamic21);

				nearbyAttentionID
						.setImageResource(R.drawable.nearby_user_dynamic22);

				nearbyClassScoreID
						.setImageResource(R.drawable.nearby_user_dynamic23_focus);
				viewPager.setCurrentItem(3);
				break;
			}

			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

}
