package com.yedianchina.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yedianchina.dao.ActivityDao;
import com.yedianchina.ui.R;

public class BaomingUI extends Activity {

	EditText userCntEt;
	EditText nameEt;
	EditText linkMpEt;
	EditText memoEt;

	TextView commitBtn;

	Long activityId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.baoming);

		userCntEt = (EditText) this.findViewById(R.id.userCntEt);
		nameEt = (EditText) this.findViewById(R.id.nameEt);
		linkMpEt = (EditText) this.findViewById(R.id.linkMpEt);

		memoEt = (EditText) this.findViewById(R.id.memoEt);

		activityId = this.getIntent().getExtras().getLong("pk");

		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					BaomingUI.this.finish();

				}
			});

		}

		TextView qiandaoBtn = (TextView) this.findViewById(R.id.qiandaoBtn);
		if (qiandaoBtn != null) {
			qiandaoBtn.setVisibility(View.INVISIBLE);
		}

		commitBtn = (TextView) this.findViewById(R.id.commitBtn);
		commitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String mp = linkMpEt.getText().toString();
				if (mp == null || mp.length() == 0 || "请输入联系电话".equals(mp)) {
					Toast toast = Toast.makeText(BaomingUI.this,
							"密码不能为空,或者密码错误", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				} else {

					new Thread() {
						public void run() {

							ActivityDao.saveBaoming(userCntEt.getText()
									.toString(), nameEt.getText().toString(),
									linkMpEt.getText().toString(), activityId,
									memoEt.getText().toString());
							loadingHandler.sendEmptyMessage(1);

						}
					}.start();
				}

			}
		});
	}

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {

				Toast toast = Toast.makeText(BaomingUI.this, "报名成功",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

				new Thread() {
					public void run() {
						try {

							sleep(1000);
							closeHandler.sendEmptyMessage(1);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}.start();

			} else {
				Toast toast = Toast.makeText(BaomingUI.this, "报名失败",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

				new Thread() {
					public void run() {
						try {

							sleep(1000);
							closeHandler.sendEmptyMessage(1);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}.start();
			}
		}
	};

	Handler closeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				BaomingUI.this.finish();
			}
		}
	};

}
