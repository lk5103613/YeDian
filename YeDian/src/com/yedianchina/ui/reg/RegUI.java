package com.yedianchina.ui.reg;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.yedianchina.dao.LoginUserDao;
import com.yedianchina.po.MerchantPO;
import com.yedianchina.po.RecruitPO;
import com.yedianchina.po.UserPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.R;
import com.yedianchina.ui.YeDianChinaApplication;

public class RegUI extends Activity {
	UserPO po;
	EditText mpTv;
	EditText codeTv;
	TextView reg_commit;

	TextView manTxt;
	TextView manImg;

	TextView girlTxt;
	TextView girlImg;

	TextView merchantTxt;// 是否商家
	TextView merchantImg;
	TextView merchantTv;

	TextView personalImg;
	TextView personalTxt;
	TextView personalTv;

	TextView pwdTv;
	TextView pwd2Tv;
	Long pk;

	int userType = 1;
	int gender = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.reg_mp);

		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					RegUI.this.finish();

				}
			});
		}
		// //////////////////////////////////////
		mpTv = (EditText) this.findViewById(R.id.mpTv);

		pwdTv = (EditText) this.findViewById(R.id.pwdTv);
		pwd2Tv = (EditText) this.findViewById(R.id.pwd2Tv);

		// 男
		manTxt = (TextView) this.findViewById(R.id.manTxt);
		manImg = (TextView) this.findViewById(R.id.manImg);
		// 女
		girlTxt = (TextView) this.findViewById(R.id.girlTxt);
		girlImg = (TextView) this.findViewById(R.id.girlImg);

		// 商家
		merchantTxt = (TextView) this.findViewById(R.id.merchantTxt);
		merchantImg = (TextView) this.findViewById(R.id.merchantImg);
		merchantTv = (TextView) this.findViewById(R.id.merchantTv);

		// 个人
		personalTxt = (TextView) this.findViewById(R.id.personalTxt);
		personalImg = (TextView) this.findViewById(R.id.personalImg);
		personalTv = (TextView) this.findViewById(R.id.personalTv);

		// 男
		manTxt = (TextView) this.findViewById(R.id.manTxt);
		manImg = (TextView) this.findViewById(R.id.manImg);
		// 女
		girlTxt = (TextView) this.findViewById(R.id.girlTxt);
		girlImg = (TextView) this.findViewById(R.id.girlImg);

		// 商家
		merchantTxt = (TextView) this.findViewById(R.id.merchantTxt);

		merchantImg = (TextView) this.findViewById(R.id.merchantImg);

		// 个人
		personalTxt = (TextView) this.findViewById(R.id.personalTxt);
		personalImg = (TextView) this.findViewById(R.id.personalImg);

		final LinearLayout sexyLL = (LinearLayout) this
				.findViewById(R.id.sexyLL);

		// //////---------------/////////////

		if (merchantTv != null) {
			merchantTv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					merchantImg.setBackgroundResource(R.drawable.red_dot);
					personalImg.setBackgroundResource(R.drawable.gray_dot);
					userType = 2;

					sexyLL.setVisibility(View.INVISIBLE);

				}
			});
		}

		if (merchantTxt != null) {
			merchantTxt.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					merchantImg.setBackgroundResource(R.drawable.red_dot);
					personalImg.setBackgroundResource(R.drawable.gray_dot);
					userType = 2;

					sexyLL.setVisibility(View.INVISIBLE);

				}
			});
		}
		if (merchantImg != null) {
			merchantImg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					merchantImg.setBackgroundResource(R.drawable.red_dot);
					personalImg.setBackgroundResource(R.drawable.gray_dot);
					userType = 2;
					sexyLL.setVisibility(View.INVISIBLE);
				}
			});
		}

		//
		if (personalTxt != null) {
			personalTxt.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					merchantImg.setBackgroundResource(R.drawable.gray_dot);
					personalImg.setBackgroundResource(R.drawable.red_dot);
					userType = 1;
					sexyLL.setVisibility(View.VISIBLE);

				}
			});
		}
		if (personalImg != null) {
			personalImg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					merchantImg.setBackgroundResource(R.drawable.gray_dot);
					personalImg.setBackgroundResource(R.drawable.red_dot);
					userType = 1;
					sexyLL.setVisibility(View.VISIBLE);
				}
			});
		}

		if (personalTv != null) {
			personalTv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					merchantImg.setBackgroundResource(R.drawable.gray_dot);
					personalImg.setBackgroundResource(R.drawable.red_dot);
					userType = 1;
					sexyLL.setVisibility(View.VISIBLE);
				}
			});
		}

		// 以下设置性别
		// 点击设置男人
		TextView manTv = (TextView) this.findViewById(R.id.manTv);
		if (manTv != null) {
			manTv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					manImg.setBackgroundResource(R.drawable.red_dot);
					girlImg.setBackgroundResource(R.drawable.gray_dot);
					gender = 1;
				}
			});
		}

		TextView manImg1 = (TextView) this.findViewById(R.id.manImg1);
		if (manImg1 != null) {
			manImg1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					manImg.setBackgroundResource(R.drawable.red_dot);
					girlImg.setBackgroundResource(R.drawable.gray_dot);
					gender = 1;
				}
			});
		}

		TextView manImg2 = (TextView) this.findViewById(R.id.manImg2);
		if (manImg2 != null) {
			manImg1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					manImg.setBackgroundResource(R.drawable.red_dot);
					girlImg.setBackgroundResource(R.drawable.gray_dot);
					gender = 1;
				}
			});
		}

		if (manTxt != null) {
			manTxt.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					manImg.setBackgroundResource(R.drawable.red_dot);
					girlImg.setBackgroundResource(R.drawable.gray_dot);
					gender = 1;

				}
			});
		}
		if (manImg != null) {
			manImg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					manImg.setBackgroundResource(R.drawable.red_dot);
					girlImg.setBackgroundResource(R.drawable.gray_dot);
					gender = 1;
				}
			});
		}

		// 点击设置女人
		if (girlTxt != null) {
			girlTxt.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					manImg.setBackgroundResource(R.drawable.gray_dot);
					girlImg.setBackgroundResource(R.drawable.red_dot);
					gender = 2;

				}
			});
		}
		if (girlImg != null) {
			girlImg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					manImg.setBackgroundResource(R.drawable.gray_dot);
					girlImg.setBackgroundResource(R.drawable.red_dot);
					gender = 2;
				}
			});
		}
		//
		TextView girlTv = (TextView) this.findViewById(R.id.girlTv);
		if (girlTv != null) {
			girlTv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					manImg.setBackgroundResource(R.drawable.gray_dot);
					girlImg.setBackgroundResource(R.drawable.red_dot);
					gender = 2;
				}
			});
		}

		reg_commit = (TextView) this.findViewById(R.id.reg_commit);
		if (reg_commit != null) {
			reg_commit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mpTv.getText().toString().length() == 0
							|| mpTv.getText().toString() == null
							|| "".equals(mpTv.getText().toString())) {
						Toast toast = Toast.makeText(RegUI.this, "帐号格式不对",
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					} else if (pwdTv.getText().toString() == null
							|| "".equals(pwdTv.getText().toString())) {
						Toast toast = Toast.makeText(RegUI.this, "密码格式不对",
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					} else if (pwd2Tv.getText().toString() == null
							|| "".equals(pwd2Tv.getText().toString())) {
						Toast toast = Toast.makeText(RegUI.this, "确认密码格式不对",
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					} else if (!pwdTv.getText().toString()
							.equals(pwd2Tv.getText().toString())) {
						Toast toast = Toast.makeText(RegUI.this, "确认密码和密码不一致",
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					} else {
						// Toast toast = Toast.makeText(this, "注册成功",
						// Toast.LENGTH_SHORT);
						// toast.setGravity(Gravity.CENTER, 0, 0);
						// toast.show();
						// startActivity(new Intent(this,MainActivity.class));
						progressDialog = ProgressDialog.show(RegUI.this,
								"夜店中国", "正在处理中，请稍后....", true);
						progressDialog.show();
						new Thread() {
							public void run() {
								LoginUserDao userDao = new LoginUserDao();
								String u = mpTv.getText().toString();
								String p = pwdTv.getText().toString();
								po = userDao.regMp(u, p, "" + userType, gender
										+ "");
								if (po != null) {

									if (po.getCode() == 1) {
										regHandler.sendEmptyMessage(1);
									}
									if (po.getCode() == 7) {
										regHandler.sendEmptyMessage(7);
									}

								} else {
									regHandler.sendEmptyMessage(2);
								}

							}
						}.start();
					}

				}
			});
		}
		//
		TextView qiandaoBtn=(TextView)this.findViewById(R.id.qiandaoBtn);
		if(qiandaoBtn!=null){
			qiandaoBtn.setVisibility(View.INVISIBLE);
		}

	}

	ProgressDialog progressDialog = null;
	Handler regHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			progressDialog.dismiss();
			if (what == 1) {
				regHandler.removeMessages(1);
				SharedPreferences preferences = getSharedPreferences(
						CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString(CONSTANTS.USERNAME, po.getMp());
				editor.putLong(CONSTANTS.UID, po.getUid());
				editor.putInt(CONSTANTS.USERTYPE, po.getUserType());
				String pwd = pwdTv.getText().toString();
				editor.putString(CONSTANTS.PWD, pwd);// 用于登录聊天服务器
				editor.commit();// 一定要记得提交
				// ///////////////////////////////

				reg2ChatServerHandler.sendEmptyMessage(1);

				// ///////////////////////////////
				RegUI.this.finish();
			}

			else if (what == 7) {
				// 登录失败
				Toast toast = Toast.makeText(RegUI.this, "您输入的手机号码已经被注册!",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			} else {
				// 登录失败
				Toast toast = Toast.makeText(RegUI.this, "注册失败",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

		};
	};
	
	@Override
	protected void onDestroy() {
	    pd.dismiss();
		super.onDestroy();
	}

	ProgressDialog pd=null;
	Handler reg2ChatServerHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				pd = new ProgressDialog(RegUI.this);
				pd.setMessage("正在处理中,请稍后...");
				pd.show();
				new Thread(new Runnable() {
					public void run() {
						try {

							// 调用sdk注册方法
							final String username = mpTv.getText().toString();
							String pwd = pwdTv.getText().toString();
							Log.e("EMChatManager.getInstance().createAccountOnServer",
									"username=" + username + " pwd=" + pwd);
							EMChatManager.getInstance().createAccountOnServer(
									username, pwd);
							runOnUiThread(new Runnable() {
								public void run() {
									if (!RegUI.this.isFinishing())
										pd.dismiss();
									// 保存用户名
									YeDianChinaApplication.getInstance()
											.setUserName(username);
									// Toast.makeText(getApplicationContext(),
									// "注册成功", 0).show();
									RegUI.this.finish();
								}
							});
						} catch (final Exception e) {
							runOnUiThread(new Runnable() {
								public void run() {
									if (!RegUI.this.isFinishing())
										pd.dismiss();
									if (e != null && e.getMessage() != null) {
										String errorMsg = e.getMessage();
										if (errorMsg
												.indexOf("EMNetworkUnconnectedException") != -1) {
											Toast.makeText(RegUI.this,
													"网络异常，请检查网络！", 0).show();
										} else if (errorMsg.indexOf("conflict") != -1) {
											Toast.makeText(RegUI.this,
													"用户已存在！", 0).show();
										} else if (errorMsg
												.indexOf("not support the capital letters") != -1) {
											Toast.makeText(RegUI.this,
													"用户名不支持大写字母！", 0).show();
										} else {
											Toast.makeText(RegUI.this,
													"注册失败: " + e.getMessage(),
													1).show();
										}

									} else {
										Toast.makeText(RegUI.this,
												"注册失败: 未知异常", 1).show();

									}
								}
							});
						}
					}
				}).start();

			}
		};
	};

}
