package com.yedianchina.ui.nearby;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.dao.UserDao;
import com.yedianchina.po.UserPO;
import com.yedianchina.ui.R;

public class MingpianUI extends Activity {

	private String pk;
	private UserPO userPO;
	ImageView avatarTv;
	TextView accountTv;
	TextView nicknameTv;
	TextView sexTv;
	TextView birthdayTv;
	TextView heightTv;

	TextView weightTv;
	TextView locTv;
	TextView hometownTv;
	TextView qmTv;

	TextView titleTv;
	TextView city_nameTv;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.nearby_mingpian);
		avatarTv = (ImageView) this.findViewById(R.id.avatarTv);
		accountTv = (TextView) this.findViewById(R.id.accountTv);
		nicknameTv = (TextView) this.findViewById(R.id.nicknameTv);
		sexTv = (TextView) this.findViewById(R.id.sexTv);
		birthdayTv = (TextView) this.findViewById(R.id.birthdayTv);
		heightTv = (TextView) this.findViewById(R.id.heightTv);
		//
		weightTv = (TextView) this.findViewById(R.id.weightTv);
		locTv = (TextView) this.findViewById(R.id.locTv);
		hometownTv = (TextView) this.findViewById(R.id.hometownTv);
		qmTv = (TextView) this.findViewById(R.id.qmTv);

		city_nameTv = (TextView) this.findViewById(R.id.locTv);

		pk = this.getIntent().getExtras().getString("pk");

		titleTv = (TextView) this.findViewById(R.id.NavigateTitle);

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
					MingpianUI.this.finish();

				}
			});
		}
		// //////////////
		TextView accountPreTv = (TextView) this.findViewById(R.id.accountPreTv);
		if (accountPreTv != null) {
			TextPaint paint = accountPreTv.getPaint();
			paint.setFakeBoldText(true);
		}
		//
		TextView nicknamePreTv = (TextView) this
				.findViewById(R.id.nicknamePreTv);
		if (nicknamePreTv != null) {
			TextPaint paint = nicknamePreTv.getPaint();
			paint.setFakeBoldText(true);
		}
		//
		TextView sexPreTv = (TextView) this.findViewById(R.id.sexPreTv);
		if (sexPreTv != null) {
			TextPaint paint = sexPreTv.getPaint();
			paint.setFakeBoldText(true);
		}
		//
		TextView birthdayPreTv = (TextView) this
				.findViewById(R.id.birthdayPreTv);
		if (birthdayPreTv != null) {
			TextPaint paint = birthdayPreTv.getPaint();
			paint.setFakeBoldText(true);
		}
		//
		TextView heightPreTv = (TextView) this.findViewById(R.id.heightPreTv);
		if (heightPreTv != null) {
			TextPaint paint = heightPreTv.getPaint();
			paint.setFakeBoldText(true);
		}
		//
		TextView hx6LLPreTv = (TextView) this.findViewById(R.id.hx6LLPreTv);
		if (hx6LLPreTv != null) {
			TextPaint paint = hx6LLPreTv.getPaint();
			paint.setFakeBoldText(true);
		}
		//
		TextView locPreTv = (TextView) this.findViewById(R.id.locPreTv);
		if (locPreTv != null) {
			TextPaint paint = locPreTv.getPaint();
			paint.setFakeBoldText(true);
		}
		//
		TextView lgxPreTv = (TextView) this.findViewById(R.id.lgxPreTv);
		if (lgxPreTv != null) {
			TextPaint paint = lgxPreTv.getPaint();
			paint.setFakeBoldText(true);
		}
		//
		TextView qmPreTv = (TextView) this.findViewById(R.id.qmPreTv);
		if (qmPreTv != null) {
			TextPaint paint = qmPreTv.getPaint();
			paint.setFakeBoldText(true);
		}
		//
		TextView hometownPreTv = (TextView) this
				.findViewById(R.id.hometownPreTv);
		if (hometownPreTv != null) {
			TextPaint paint = hometownPreTv.getPaint();
			paint.setFakeBoldText(true);
		}

		TextView descPreTv = (TextView) this.findViewById(R.id.descPreTv);
		if (descPreTv != null) {
			TextPaint paint = descPreTv.getPaint();
			paint.setFakeBoldText(true);
		}

	}

	public void updateCurrendData() {
		userPO = UserDao.loadUserInfo(new Long(pk));

	}

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				// Log.e("userPO", userPO.getNickname());
				setNearbyMingpianData();

			}
		};
	};

	void setNearbyMingpianData() {

		String cityname = userPO.getCity_name();

		city_nameTv.setText(cityname);

		String logo = userPO.getAvatar();
		if (logo != null && logo.length() > 5) {

			Log.e("头像", logo);
			avatarTv.setTag(logo);

			ImageLoader.getInstance().displayImage(logo, avatarTv);
		} else {
			avatarTv.setBackgroundResource(R.drawable.nearby_u2);
		}

		accountTv.setText("" + pk);

		String nickname = userPO.getNickname();
		if (nickname != null && nickname.length() > 0) {
			nicknameTv.setText(nickname);
			titleTv.setText(nickname + "的名片");
		} else {
			titleTv.setText("名片");
		}

		int gender = userPO.getGender();
		if (gender == 1) {
			sexTv.setText("男");
		}
		if (gender == 2) {
			sexTv.setText("女");
		}

		// TextView birthdayTv;
		String height = userPO.getHeight();
		if (height != null && height.length() > 0) {
			heightTv.setText("");
		}
		//

		String weight = userPO.getWeight();
		if (weight != null && weight.length() > 0) {
			weightTv.setText("" + weight);
		}

		// TextView locTv;

		String home_town = userPO.getHome_town();
		if (home_town != null && home_town.length() > 0) {
			hometownTv.setText(home_town);
		}
		String city_name = userPO.getCity_name();
		if (city_name != null && city_name.length() > 0) {
			locTv.setText(home_town);
		}

		String qm = userPO.getQm();
		if (qm != null && qm.length() > 0) {
			qmTv.setText(qm);
		}
	}

}
