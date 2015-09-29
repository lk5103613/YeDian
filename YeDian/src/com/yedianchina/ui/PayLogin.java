package com.yedianchina.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yedianchina.dao.LoginUserDao;
import com.yedianchina.po.UserPO;
import com.yedianchina.ui.reg.RegUI;

public class PayLogin extends CommonActivity {
	protected void onResume() {
		if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 }
		super.onResume();
	}
	private float density = 0;
	private TextView loginBtn = null;// 登录
	private TextView regBtn = null; // 注册
	private TextView forgetPasswordTextView = null;// 忘记密码
	private EditText cellPhoneEditText = null;// 账号
	private EditText passWordEditText = null;// 密码
	private UserPO user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 去除手机界面默认标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		super.checkNetWork();// 检查网络是否连接

		// 手机界面标题设置
		super.textViewTitle = (TextView) findViewById(R.id.tvHeaderTitle);
		super.textViewTitle.setText("我的代驾");
		super.onCreate(savedInstanceState);

		//cellPhoneEditText = (EditText) findViewById(R.id.cell_phone);
		cellPhoneEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

		passWordEditText = (EditText) findViewById(R.id.pwd);
		loginBtn = (TextView) findViewById(R.id.loginTv);
		//regBtn = (TextView) findViewById(R.id.register);
		// forgetPasswordTextView = (TextView)
		// findViewById(R.id.forget_password);
		loginBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// 隐藏软键盘
				InputMethodManager imManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				IBinder mBinder = getCurrentFocus().getWindowToken();
				imManager.hideSoftInputFromWindow(mBinder,
						InputMethodManager.HIDE_NOT_ALWAYS);
				if (cellPhoneEditText.getText().toString() == null
						|| "".equals(cellPhoneEditText.getText().toString())) {
					Toast toast = Toast.makeText(PayLogin.this,
							"用户名不能为空,或者用户名错误", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				} else if (passWordEditText.getText().toString() == null
						|| "".equals(passWordEditText.getText().toString())) {
					Toast toast = Toast.makeText(PayLogin.this, "密码不能为空,或者密码错误",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}

				new Thread() {
					public void run() {
						LoginUserDao customerService = new LoginUserDao();
						String u = cellPhoneEditText.getText().toString();
						String p = passWordEditText.getText().toString();
					
						UserPO po = customerService.login(u, p);

						user = po;
						if (po != null) {
							
							loginHandler.sendEmptyMessage(1);
						} else {
							loginHandler.sendEmptyMessage(2);
						}

					}
				}.start();

			}
		});

		regBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(PayLogin.this, RegUI.class));

			}
		});

		

		TextView tvHeaderTitle = (TextView) this
				.findViewById(R.id.tvHeaderTitle);
		tvHeaderTitle.setText("用户登录");
		LinearLayout loginBtnLL = (LinearLayout) this
				.findViewById(R.id.loginBtnLL);
		loginBtnLL.setVisibility(View.INVISIBLE);

		YeDianChinaApplication.getInstance().addActivity(this);
		LinearLayout back_btn = (LinearLayout) this.findViewById(R.id.setting_title_btn);
		back_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				PayLogin.this.finish();

			}
		});
		TextView backBtn = (TextView) this.findViewById(R.id.back_btn);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				PayLogin.this.finish();

			}
		});
	
		

	}

	Handler loginHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
		
			if (what == 1) {
			
				loginHandler.removeMessages(1);
				SharedPreferences preferences = getSharedPreferences(
						"yedianchina_user_info", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("u", user.getMp());
				editor.putLong("uid", user.getUid());
				editor.commit(); // 一定要记得提交

				
				
				 
				PayLogin.this.finish();
				
				

			} else {

				Toast toast = Toast.makeText(getApplicationContext(),
						"手机号或密码错误", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

		};
	};

	/**
	 * 获取屏幕的密度
	 */
	private void getDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		density = metrics.density;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) { // 在指派Touch事件时拦截，由于安卓的Touch事件是自顶而下的，所以Activity是第一响应者
		if (ev.getAction() == MotionEvent.ACTION_DOWN) { // 类型为Down才处理，还有Move/Up之类的
			if (this.getCurrentFocus() != null) { // 获取当前焦点
				CloseSoftInput(getCurrentFocus());
			}
		}
		return super.dispatchTouchEvent(ev); // 继续指派Touch事件，如果这里不执行基类的dispatchTouchEvent，事件将不会继续往下传递
	}

	protected void CloseSoftInput(View view) { // 关闭软键盘
		if (view != null) {
			if (view.getWindowToken() != null) {
				InputMethodManager imm;
				imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

}
