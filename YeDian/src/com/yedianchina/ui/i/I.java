package com.yedianchina.ui.i;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.activity.MainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.LoginUI;
import com.yedianchina.ui.MoreUI;
import com.yedianchina.ui.R;
import com.yedianchina.ui.YeDianChinaApplication;
import com.yedianchina.ui.index.IndexUI;
import com.yedianchina.ui.index.RecruitListActivity;
import com.yedianchina.ui.nearby.NearbyModule;

public class I extends CommonActivity {

	public Bitmap getRoundCornerBitmap(Bitmap bitmap, float roundPX) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Log.e("", "width=" + width);

		Bitmap bitmap2 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap2);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);

		paint.setColor(color);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		canvas.drawBitmap(bitmap, rect, rect, paint);

		return bitmap2;
	}

	protected void onResume() {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

	private Double balance;

	private float density = 0;

	private void getDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		density = metrics.density;
	}

	/**
	 * 退出登陆
	 */
	private TextView exitImageView = null;

	private void parentControl() {
		// 获取密度
		this.getDensity();

		imageViewIndex = (ImageView) findViewById(R.id.menu_home_img);
		imageViewIndex.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivity(new Intent(getApplicationContext(), IndexUI.class));
				I.this.finish();

			}
		});

		imageViewType = (ImageView) findViewById(R.id.menu_brand_img);
		imageViewType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(),
						NearbyModule.class));
				I.this.finish();
			}
		});

		imageViewShooping = (ImageView) findViewById(R.id.menu_my_letao_img);
		imageViewShooping.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Log.e("ImageViewMyLetao", "聊天---");
				SharedPreferences preferences = getSharedPreferences(
						CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);
				Long uid = preferences.getLong(CONSTANTS.UID, 0);
				if (uid == null || uid == 0) {
					Toast.makeText(getApplicationContext(), "您尚未登录,请先登录", 1)
							.show();
					startActivity(new Intent(getApplicationContext(),
							LoginUI.class));
					return;
				}

				String myNickname = preferences.getString(CONSTANTS.NICKNAME,
						"");
				if (myNickname == null || myNickname.length() == 0
						|| "".equals(myNickname)) {
					Toast.makeText(getApplicationContext(),
							"您的资料不完善，请去“我的中心－》右上角我的名片处完善个人资料 ", 1).show();

					startActivity(new Intent(getApplicationContext(),
							IMingpianUI.class));

				} else {

					startActivity(new Intent(getApplicationContext(),
							MainActivity.class));
					I.this.finish();
				}

			}
		});

		imageViewMyLetao = (ImageView) findViewById(R.id.menu_shopping_cart_img);
		imageViewMyLetao.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// startActivity(new Intent(getApplicationContext(),
				// I.class));
				// I.this.finish();

			}
		});

		imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
		imageViewMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), MoreUI.class));
				I.this.finish();
			}
		});

	}

	public void toMyPublish(View v) {
		Intent intent = new Intent(this, RecruitListActivity.class);
		startActivity(intent);
	}
	
	ImageView avatar0612 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.i);
		TextView _textViewTitle = (TextView) findViewById(R.id.NavigateTitle);
		_textViewTitle.setText("我的中心");

		TextView mingpianBtn = (TextView) this.findViewById(R.id.mingpianBtn);
		if (mingpianBtn != null) {
			mingpianBtn.setVisibility(View.INVISIBLE);
		}

		LinearLayout mingpianLL = (LinearLayout) this
				.findViewById(R.id.mingpianLL);
		if (mingpianLL != null) {
			mingpianLL.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					SharedPreferences preferences = getSharedPreferences(
							CONSTANTS.YEDIANCHINA_USER_INFO,
							Activity.MODE_PRIVATE);

					int userType = preferences.getInt(CONSTANTS.USERTYPE, 1);// '1:个人用户
																				// 2：商家',
					if (userType == 1) {
						intent.setClass(I.this, IMingpianUI.class);
					}
					if (userType == 2) {
						intent.setClass(I.this, IMerchantMingPianUI.class);
					}

					I.this.startActivity(intent);

				}
			});
		}
		// ////////////

		LinearLayout myAttentionLL = (LinearLayout) this
				.findViewById(R.id.myAttentionLL);
		if (myAttentionLL != null) {
			myAttentionLL.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();

					intent.setClass(I.this, IAttentionUI.class);

					I.this.startActivity(intent);

				}
			});
		}

		// ///////////////////
		ImageView userAvatar = (ImageView) this.findViewById(R.id.userAvatar);
		if (userAvatar != null) {
			userAvatar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(I.this, IMainUI.class);

					I.this.startActivity(intent);

				}
			});
		}

		// this.avatar0612=(ImageView)this.findViewById(R.id.avatar0612);
		// InputStream is =
		// getResources().openRawResource(R.drawable.avatar0612);
		//
		// Bitmap mBitmap = BitmapFactory.decodeStream(is);
		// mBitmap=getRoundCornerBitmap(mBitmap, 790.0f);
		// this.avatar0612.setImageBitmap(mBitmap);

		// LinearLayout iMingPianLL = (LinearLayout) this
		// .findViewById(R.id.iMingPianLL);
		// iMingPianLL.setClickable(true);
		// iMingPianLL.setFocusable(true);
		// iMingPianLL.setFocusableInTouchMode(true);
		// if (iMingPianLL != null) {
		// iMingPianLL.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// Intent intent = new Intent();
		// intent.setClass(I.this, IMingpianUI.class);
		//
		// I.this.startActivity(intent);
		//
		// }
		// });
		// }

		LinearLayout tmpLL = (LinearLayout) this
				.findViewById(R.id.setting_title_btn);
		if (tmpLL != null) {
			tmpLL.setVisibility(View.INVISIBLE);
		}

		// super.progressDialog = ProgressDialog.show(this, "哈根达斯", "数据获取中....",
		// true);
		// super.progressDialog.show();
		// 通过线程来循环调用进度条
		// super.handler.post(this);

		// bottomMenuOnClick();

		// 退出登陆

		// 退出按钮 暂时隐藏
		// exitImageView.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// SharedPreferences preferences = getSharedPreferences(
		// "yedianchina_user_info", Activity.MODE_PRIVATE);
		// SharedPreferences.Editor editor = preferences.edit();
		// editor.putString("u", "");
		// editor.putLong("uid", 0);
		// editor.commit();// 一定要记得提交
		// //
		//
		// intent.setClass(getApplicationContext(), MoreUI.class);
		// startActivity(intent);
		//
		// }
		// });

		SharedPreferences preferences = getSharedPreferences(
				CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);
		String u = preferences.getString("u", "");
		if (u == null || u.length() == 0 || u == "") {

			Intent intent = new Intent();
			intent.setClass(I.this, LoginUI.class);
			intent.putExtra("flag", "IUI");
			// progressDialog.dismiss();
			I.this.startActivity(intent);
			I.this.finish();
		}

		//
		final Long uid = preferences.getLong("uid", 0);

		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// try {
		// LoginUserDao s = new LoginUserDao();
		// UserPO user = s.loadUserInfo(1L);
		// balance=user.getBalance();
		// loadingHandler.sendEmptyMessage(1);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }).start();

		// String url0="http://192.168.1.101/user_upload/1380866982_5440.jpg";
		// if (url0 != null && url0.length() > 10) {
		// RoundImageLoader imageLoader;
		// avatar0612.setTag(url0);
		// imageLoader = new RoundImageLoader(I.this);
		// imageLoader.DisplayImage(url0, I.this,
		// avatar0612);
		// }

		parentControl();

		// TextView logoutTv=(TextView)this.findViewById(R.id.logoutTv);
		// if(logoutTv!=null){
		// logoutTv.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		//
		// SharedPreferences preferences = getSharedPreferences(
		// CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);
		// SharedPreferences.Editor editor = preferences.edit();
		// editor.remove(CONSTANTS.USERNAME);
		// editor.remove(CONSTANTS.UID);
		//
		// editor.remove("userType");
		//
		//
		// editor.commit(); // 一定要记得提交
		// try{
		// EMChatManager.getInstance().logout();
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		//
		// startActivity(new Intent(I.this, IndexUI.class));
		// I.this.finish();
		//
		// }
		// });
		// }

		//

		YeDianChinaApplication.getInstance().addActivity(this);

	}

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {

				// progressDialog.dismiss();

				int what = msg.what;
				if (what == 1) {
					String url0 = CONSTANTS.HOST
							+ "user_upload/1380866982_5440.jpg";
					if (url0 != null && url0.length() > 10) {

						avatar0612.setTag(url0);

						ImageLoader.getInstance()
								.displayImage(url0, avatar0612);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 退出事件相应
	 * 
	 * @author Administrator
	 * 
	 */
	class exitImageViewOnClickListener implements
			android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			//
			SharedPreferences preferences = getSharedPreferences(
					CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("u", "");
			editor.putLong("uid", 0);
			editor.commit();// 一定要记得提交
			//

			// intent.setClass(getApplicationContext(), MoreUI.class);
			// startActivity(intent);

			Intent intent = new Intent();
			intent.setClass(I.this, MoreUI.class);

			// progressDialog.dismiss();
			I.this.startActivity(intent);
			I.this.finish();
		}

	}

	private static final int ADDRESS = 3;
	private static final int COLLECT = 1;
	private static final int FOVAORABLE = 2;
	private static final int MYORDER = 0;
	private static final int BILL_UI = 4;

}