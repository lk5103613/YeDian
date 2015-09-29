package com.yedianchina.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



import com.easemob.EMCallBack;
import com.easemob.chat.Constant;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.activity.LoginActivity;
import com.easemob.chat.activity.MainActivity;

import com.easemob.chat.db.UserDao;
import com.easemob.chat.domain.User;
import com.easemob.util.HanziToPinyin;
import com.sina.weibo.sdk.log.Log;
import com.yedianchina.dao.LoginUserDao;
import com.yedianchina.po.UserPO;
import com.yedianchina.tools.CONSTANTS;

import com.yedianchina.ui.reg.RegUI;

public class LoginUI extends CommonActivity{
	
	CheckBox checkBox;
	protected void onResume() {
		
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
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
	
	TextView regLinkBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.login);
		
		
		initData();
		
		regLinkBtn=(TextView)this.findViewById(R.id.regLinkBtn);
		if(regLinkBtn!=null){
			regLinkBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(LoginUI.this, RegUI.class);
					
					LoginUI.this.startActivity(intent);
					LoginUI.this.finish();
				}
			});
		}
		//////
		
		TextView regLinkTv=(TextView)this.findViewById(R.id.regLinkTv);
		if(regLinkTv!=null){
			regLinkTv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(LoginUI.this, RegUI.class);
					
					LoginUI.this.startActivity(intent);
					LoginUI.this.finish();
				}
			});
		}
		
		
		

		// 手机界面标题设置
		TextView navigateTitle = (TextView) findViewById(R.id.NavigateTitle);
		if(navigateTitle!=null){
			navigateTitle.setText("登录");
		}
		checkBox=(CheckBox)this.findViewById(R.id.checkBox);
		checkBox.setChecked(true);//默认记住密码
		
		
		super.onCreate(savedInstanceState);

		cellPhoneEditText = (EditText) findViewById(R.id.u);
		//cellPhoneEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

		passWordEditText = (EditText) findViewById(R.id.pwd);
		loginBtn = (TextView) findViewById(R.id.login);
	

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
					Toast toast = Toast.makeText(LoginUI.this,
							"用户名不能为空,或者用户名错误", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				} else if (passWordEditText.getText().toString() == null
						|| "".equals(passWordEditText.getText().toString())) {
					Toast toast = Toast.makeText(LoginUI.this, "密码不能为空,或者密码错误",
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
						

						user= customerService.login(u, p);

						 
					
						if (user != null) {
							

							loginHandler.sendEmptyMessage(1);
						} else {
							loginHandler.sendEmptyMessage(2);
						}

					}
				}.start();

			}
		});

 

		 
		
		
		LinearLayout loginBtnLL = (LinearLayout) this
				.findViewById(R.id.loginBtnLL);
		if (loginBtnLL != null) {
			loginBtnLL.setVisibility(View.INVISIBLE);
		}

		YeDianChinaApplication.getInstance().addActivity(this);
		//
		
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					LoginUI.this.finish();

				}
			});
		}
		//

	}

	Handler loginHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;

			if (what == 1) {

				loginHandler.removeMessages(1);
				//如果用户资料已经完善过  特别是 昵称  性别等 则连接聊天服务器
				String nickname=user.getNickname();
				int  userType=user.getUserType();
				
				
				
				SharedPreferences preferences = getSharedPreferences(
						CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString(CONSTANTS.USERNAME, user.getMp());
				editor.putLong(CONSTANTS.UID, user.getUid());
				final String  pwd=passWordEditText.getText().toString();
				editor.putInt("userType", user.getUserType());//'1:个人用户    2：商家',
				editor.commit(); // 一定要记得提交
				
				Log.e("登录", "nickname=="+nickname+"   userType="+userType);
				
				if(userType==1){
					if(nickname!=null&&nickname.length()>0){
						//////////////////////////////////////////////////////////////////////////
					
					
					
					
					
						editor.putString(CONSTANTS.NICKNAME, nickname);
						
						editor.commit(); // 一定要记得提交
						
						
						//progressShow=true;
						final ProgressDialog pd = new ProgressDialog(LoginUI.this);
						pd.setCancelable(true);
						pd.setOnCancelListener(new OnCancelListener() {
							
							@Override
							public void onCancel(DialogInterface dialog) {
								//progressShow=false;
							}
						});
						pd.setMessage("正在登陆...");
						pd.show();
						// 调用sdk登陆方法登陆聊天服务器
						EMChatManager.getInstance().login(user.getMp(), pwd, new EMCallBack() {

							@Override
							public void onSuccess() {
							
								// 登陆成功，保存用户名密码
								YeDianChinaApplication.getInstance().setUserName(user.getMp());
								YeDianChinaApplication.getInstance().setPassword(pwd);
								runOnUiThread(new Runnable() {
									public void run() {
										pd.setMessage("正在获取好友和群聊列表...");
									}
								});
								try {
									// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
									List<String> usernames = EMChatManager.getInstance().getContactUserNames();
									Map<String, User> userlist = new HashMap<String, User>();
									for (String username : usernames) {
										User user = new User();
										user.setUsername(username);
										setUserHearder(username, user);
										userlist.put(username, user);
									}
									// 添加user"申请与通知"
									User newFriends = new User();
									newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
									newFriends.setNick("申请与通知");
									newFriends.setHeader("");
									userlist.put(Constant.NEW_FRIENDS_USERNAME,newFriends);
									//添加"群聊"
									User groupUser = new User();
									groupUser.setUsername(Constant.GROUP_USERNAME);
									groupUser.setNick("群聊");
									groupUser.setHeader("");
									userlist.put(Constant.GROUP_USERNAME, groupUser);
									
									//存入内存
									YeDianChinaApplication.getInstance().setContactList(userlist);
									// 存入db
									UserDao dao = new UserDao(LoginUI.this);
									List<User> users = new ArrayList<User>(userlist.values());
									dao.saveContactList(users);

									// 获取群聊列表,sdk会把群组存入到EMGroupManager和db中
									EMGroupManager.getInstance().getGroupsFromServer();
								} catch (Exception e) {
								}
								
								
								if(!LoginUI.this.isFinishing())
								pd.dismiss();
								// 进入主页面
								startActivity(new Intent(LoginUI.this, MainActivity.class));
								finish();
							}

							@Override
							public void onProgress(int progress, String status) {

							}

							@Override
							public void onError(int code, final String message) {
							
								runOnUiThread(new Runnable() {
									public void run() {
										pd.dismiss();
										if(message.indexOf("not support the capital letters")!=-1)
										{
											Toast.makeText(getApplicationContext(), "用户名不支持大写字母", 0).show();
										}else{
											Toast.makeText(getApplicationContext(), "登录失败: " + message, 0).show();
										}
										
										

									}
								});
							}
						});

						
					
						
						
						////////////////////////////////////////////////////////////////////////
						
					}
				}
				
				
				
				
//				if (!NetUtil.isNetConnected(this)) {
//					T.showLong(this, R.string.net_error_tip);
//					return;
//				}
				//String nick =user.getNickname();
			
			
			
				
				
				


				//LoginUI.this.finish();

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
	
	
	
///////////////////////////////////////////
	private void initData() {
		
		mApplication = YeDianChinaApplication.getInstance();
	
		
	}
	

	
	private static final int LOGIN_OUT_TIME = 0;
	YeDianChinaApplication mApplication;
	
	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}
	
	
	
	
}
