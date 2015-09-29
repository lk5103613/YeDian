package com.yedianchina.ui.nearby;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.dao.UserDao;
import com.yedianchina.po.UserPO;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.R;
import com.yedianchina.ui.YeDianChinaApplication;

public class NearbyUserMap extends CommonActivity {

	public class CurrentButton extends LinearLayout {

		public CurrentButton(Context context, String textResId) {

			super(context);

			this.setLayoutParams(new LinearLayout.LayoutParams(40, 20));

			mButtonImage = new ImageView(context);

			mButtonText = new TextView(context);

			mButtonImage.setPadding(0, 0, 0, 0);

			setText(textResId);
			setTextColor(0xFF000000);

			mButtonText.setPadding(0, 0, 0, 0);

			int w = getResources().getColor(R.color.white);
			setTextColor(w);

			// 设置本布局的属性
			setClickable(true); // 可点击
			setFocusable(true); // 可聚焦
			setBackgroundResource(android.R.drawable.btn_default); // 布局才用普通按钮的背景
			setOrientation(LinearLayout.VERTICAL); // 垂直布局

			addView(mButtonText);

			// 首先添加Image，然后才添加Text

			// 添加顺序将会影响布局效果
			addView(mButtonImage);

		}

		/*
		 * setImageResource方法
		 */

		public void setImageResource(int resId) {

			mButtonImage.setImageResource(resId);
		}

		/*
		 * setText方法
		 */

		public void setText(int resId) {

			mButtonText.setText(resId);
		}

		public void setText(CharSequence buttonText) {

			mButtonText.setText(buttonText);
		}

		/*
		 * setTextColor方法
		 */

		public void setTextColor(int color) {

			mButtonText.setTextColor(color);
		}

		private ImageView mButtonImage = null;

		private TextView mButtonText = null;

	}

	private double currentLongi;
	private double currentLanti;

	Handler refreshHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {

				progressDialog.dismiss();

				int what = msg.what;
				if (what == 1) {
					// int x = 0;

					for (UserPO po : list) {

						final Long uid = po.getUid();

						MyImageButton button = null;
						button = new MyImageButton(NearbyUserMap.this,
								R.drawable.star3, "");

						double longi = Double.valueOf(po.getLongi());
						double lanti = Double.valueOf(po.getLanti());

						System.out
								.println("longi=" + longi + " lanti=" + lanti);

						GeoPoint pt = new GeoPoint((int) (lanti * 1E6),
								(int) (longi * 1E6));// 先是纬度、然后经度

						MapView.LayoutParams layoutParam = new MapView.LayoutParams(
								228 / 3, 259 / 3, pt,
								MapView.LayoutParams.BOTTOM_CENTER);

						button.setBackgroundResource(R.drawable.pic0526);
						button.setTag(po.getUid());

						mMapView.addView(button, layoutParam);
						String avatar = po.getAvatar();
						Log.e("头像", "avatar="+avatar);
						MyAvatarButton avatarBtn = new MyAvatarButton(
								NearbyUserMap.this, avatar);

						MapView.LayoutParams layoutAvatarParam = new MapView.LayoutParams(
								228 / 3 - 8, 259 / 3 - 8, pt,
								MapView.LayoutParams.BOTTOM_CENTER);

						button.addView(avatarBtn, layoutAvatarParam);
						//
						button.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								Intent intent = new Intent();
								intent.setClass(NearbyUserMap.this,
										NearbyUserUI.class);
								Bundle bundle = new Bundle();

								bundle.putLong("pk", uid);
								intent.putExtras(bundle);
								startActivity(intent);

							}
						});
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
	};

	private List<Long> btnList = new ArrayList<Long>();
	private List<UserPO> list = new ArrayList<UserPO>();
	int DRIVER_PERIOD;
	RelativeLayout index_rl;

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {

				// progressDialog.dismiss();

				int what = msg.what;
				if (what == 1) {
					// int x = 0;
					if (list != null && list.size() > 0) {
						for (UserPO po : list) {

							final Long uid = po.getUid();

							MyImageButton button = null;

							String avatar = po.getAvatar();
						
							button = new MyImageButton(NearbyUserMap.this,
									R.drawable.star3, avatar);

							double longi = Double.valueOf(po.getLongi());
							double lanti = Double.valueOf(po.getLanti());

							GeoPoint pt = new GeoPoint((int) (lanti * 1E6),
									(int) (longi * 1E6));// 先是纬度、然后经度

							MapView.LayoutParams layoutParam = new MapView.LayoutParams(
									160, 181, pt,
									MapView.LayoutParams.BOTTOM_CENTER);

							button.setBackgroundResource(R.drawable.pic0526);

							button.setTag(po.getUid());

							btnList.add(po.getUid());

							mMapView.addView(button, layoutParam);
							//

							//

							button.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									Intent intent = new Intent();
									intent.setClass(NearbyUserMap.this,
											NearbyUserUI.class);
									Bundle bundle = new Bundle();

									bundle.putLong("pk", uid);
									intent.putExtras(bundle);
									startActivity(intent);

								}
							});
						}
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
	};

	private ProgressDialog progressDialog;
	static MapView mMapView = null;
	private MapController mMapController = null;

	public MKMapViewListener mMapListener = null;
	FrameLayout mMapViewContainer = null;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	public NotifyLister mNotifyer = null;

	Button testUpdateButton = null;

	EditText indexText = null;
	MyLocationOverlay myLocationOverlay = null;
	int index = 0;
	LocationData locData = null;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// Toast.makeText(LocationOverlayDemo.this, "msg:" +msg.what,
			// Toast.LENGTH_SHORT).show();
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		YeDianChinaApplication app = (YeDianChinaApplication) this
				.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(this);
			app.mBMapManager.init(YeDianChinaApplication.strKey,
					new YeDianChinaApplication.MyGeneralListener());
		}
		setContentView(R.layout.nearby_user_map);
		
		TextView navigateTitle=(TextView)this.findViewById(R.id.NavigateTitle);
		if(navigateTitle!=null){
			navigateTitle.setText("附近用户");
		}
		//
		TextView qiandaoBtn=(TextView)this.findViewById(R.id.qiandaoBtn);
		if(qiandaoBtn!=null){
			qiandaoBtn.setVisibility(View.INVISIBLE);
		}
		
		
		

		mMapView = (MapView) findViewById(R.id.bmapView);

		mMapController = mMapView.getController();

		mTimer = new Timer(true);

		initMapView();
		mMapView.setBuiltInZoomControls(true); // 设置启用内置的缩放控件
		ZoomControls zoomControls = (ZoomControls) mMapView.getChildAt(2);
		mMapView.removeViewAt(2);
		zoomControls.setPadding(0, 120, 217, 5);

		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps

		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		option.disableCache(true);

		mLocClient.setLocOption(option);
		mLocClient.start();
		mMapView.getController().setZoom(16);
		mMapView.getController().enableClick(true);

		mMapView.setBuiltInZoomControls(true);
		mMapListener = new MKMapViewListener() {

			@Override
			public void onMapMoveFinish() {

			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {

				// String title = "";
				// if (mapPoiInfo != null) {
				// title = mapPoiInfo.strText;
				// Toast.makeText(IndexUI.this, title, Toast.LENGTH_SHORT)
				// .show();
				// }
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {

			}

			@Override
			public void onMapAnimationFinish() {

			}
		};
		mMapView.regMapViewListener(
				YeDianChinaApplication.getInstance().mBMapManager, mMapListener);
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();
		myLocationOverlay.setData(locData);
		if (mMapView != null) {
			mMapView.getOverlays().add(myLocationOverlay);
			myLocationOverlay.enableCompass();
			mMapView.refresh();
		}

		this.flag = 1;

		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					NearbyUserMap.this.finish();

				}
			});

		}
	 

		YeDianChinaApplication.getInstance().addActivity(this);

	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mTimerTask != null) {
			mTimerTask.cancel();
		}
		if (mTimer != null) {
			mTimer.cancel();
		}
		if (mLocClient != null)
			mLocClient.stop();
		mMapView.destroy();
		YeDianChinaApplication app = (YeDianChinaApplication) this
				.getApplication();
		if (app.mBMapManager != null) {
			try {
				app.mBMapManager.destroy();
				app.mBMapManager = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	public void testUpdateClick() {
		mLocClient.requestLocation();
	}

	private void initMapView() {
		mMapView.setLongClickable(true);

	}

	int flag = 0;
	CurrentButton yrhereBtn;
	// //
	private Timer mTimer;
	private MyTimerTask mTimerTask;

	class MyTimerTask extends TimerTask {
		@Override
		public void run() {

			refreshYRHereHandler.sendEmptyMessage(1);

		}

	}

	Handler refreshYRHereHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				// --------2014-02-05 18:00-----------
				if (yrhereBtn != null) {
					mMapView.removeView(yrhereBtn);
				}

				double latitude = locData.latitude;
				double longitude = locData.longitude;

				GeoPoint pt = new GeoPoint((int) (latitude * 1e6),
						(int) (longitude * 1e6));

				yrhereBtn = new CurrentButton(NearbyUserMap.this, "您在这里");

				yrhereBtn.setBackgroundResource(R.drawable.current_img);

				MapView.LayoutParams layoutParam = new MapView.LayoutParams(
						MapView.LayoutParams.WRAP_CONTENT,
						MapView.LayoutParams.WRAP_CONTENT, pt,
						MapView.LayoutParams.BOTTOM_CENTER);

				// -------------------
				mMapView.addView(yrhereBtn, layoutParam);
			}
		}
	};

	public void sendPosTimer() {
		if (mTimer != null) {
			if (mTimerTask != null) {
				mTimerTask.cancel(); // 将原任务从队列中移除
			}

			int period = 1000 * 60 * 10;
			if (DRIVER_PERIOD > 0) {
				period = DRIVER_PERIOD * 1000;
			}

			mTimerTask = new MyTimerTask(); // 新建一个任务
			mTimer.schedule(mTimerTask, period, period);
		}
	}

	
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			if (flag == 1) {
				locData.latitude = location.getLatitude();

				locData.longitude = location.getLongitude();

				locData.accuracy = location.getRadius();
				locData.direction = location.getDerect();
				myLocationOverlay.setData(locData);
				mMapView.refresh();
				sendPosTimer();

				double latitude = locData.latitude;
				double longitude = locData.longitude;

				// latitude=latitude+0.0014;
				// longitude=longitude-0.0016;//加偏移 指向您在这里

				GeoPoint pt = new GeoPoint((int) (latitude * 1e6),
						(int) (longitude * 1e6));

				mMapController.setCenter(pt);
				flag = 0;

				yrhereBtn = new CurrentButton(NearbyUserMap.this, "您在这里");

				yrhereBtn.setBackgroundResource(R.drawable.current_img);

				MapView.LayoutParams layoutParam = new MapView.LayoutParams(
						MapView.LayoutParams.WRAP_CONTENT,
						MapView.LayoutParams.WRAP_CONTENT, pt,
						MapView.LayoutParams.BOTTOM_CENTER);
				mMapView.addView(yrhereBtn, layoutParam);

				BDLocation tmpLocation = location;
				final double longi = tmpLocation.getLongitude();
				final double lanti = tmpLocation.getLatitude();

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {

							list = UserDao.pageMap(longi + "", lanti + "");

							DRIVER_PERIOD = 10;

							// System.out.print("司机数据总量" + list.size());
							Log.e("经度", "longi=========================="
									+ longi);
							Log.e("纬度", "lanti=========================="
									+ lanti);

							loadingHandler.sendEmptyMessage(1);

							// // this.progressDialog.show();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
				// 用户经纬度
				SharedPreferences locInfo = getSharedPreferences("loc",
						Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = locInfo.edit();
				editor.putString("longitude", location.getLongitude() + "");
				editor.putString("latitude", location.getLatitude() + "");
				editor.commit();// 一定要记得提交

			}

			//

		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
		}
	}

	public class MyImageButton extends LinearLayout {

		public MyImageButton(Context context, int imageResId, String textResId) {

			super(context);
			//

			this.setLayoutParams(new LinearLayout.LayoutParams(228 / 3 - 8, 259 / 3 - 8));

			mButtonImage = new ImageView(context);
			Log.e("美女地图头像:::", textResId);

			mButtonImage.setTag(textResId);

			
			
			 ImageLoader.getInstance().displayImage(textResId, mButtonImage);

			mButtonImage.setPadding(1, 1, 1, 1);

			addView(mButtonImage);

			// 设置本布局的属性
			setClickable(true); // 可点击
			setFocusable(true); // 可聚焦
			setBackgroundResource(android.R.drawable.btn_default); // 布局才用普通按钮的背景
			setOrientation(LinearLayout.VERTICAL); // 垂直布局

			// 首先添加Image，然后才添加Text

			// 添加顺序将会影响布局效果

		}

		// ----------------public method-----------------------------

		/*
		 * setImageResource方法
		 */

		public void setImageResource(int resId) {

			mButtonImage.setImageResource(resId);
		}

		/*
		 * setText方法
		 */

		public void setText(int resId) {

			// mButtonText.setText(resId);
		}

		public void setText(CharSequence buttonText) {

			// mButtonText.setText(buttonText);
		}

		/*
		 * setTextColor方法
		 */

		public void setTextColor(int color) {

			// mButtonText.setTextColor(color);
		}

		// ----------------private attribute-----------------------------

		private ImageView mButtonImage = null;

		private ImageLoader imageLoader = null;
	}

	// /////////////////////////////////////////////

	public class MyAvatarButton extends LinearLayout {

		public MyAvatarButton(Context context, String avatar) {

			super(context);

			this.setLayoutParams(new LinearLayout.LayoutParams(60, 30));

			mButtonImage = new ImageView(context);
			// avatar=CONSTANTS.HOST+avatar;

			mButtonImage.setTag(avatar);

						
			 ImageLoader.getInstance().displayImage(avatar, mButtonImage);

			mButtonImage.setPadding(0, 0, 0, 0);

			// this.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
			int w = getResources().getColor(R.color.white);
			setTextColor(w);

			// 设置本布局的属性
			setClickable(true); // 可点击
			setFocusable(true); // 可聚焦
			setBackgroundResource(android.R.drawable.btn_default); // 布局才用普通按钮的背景
			setOrientation(LinearLayout.VERTICAL); // 垂直布局

			// addView(mButtonText);

			// 首先添加Image，然后才添加Text

			// 添加顺序将会影响布局效果
			addView(mButtonImage);

		}

		// ----------------public method-----------------------------

		/*
		 * setImageResource方法
		 */

		public void setImageResource(int resId) {

			mButtonImage.setImageResource(resId);
		}

		/*
		 * setText方法
		 */

		public void setText(int resId) {

			// mButtonText.setText(resId);
		}

		public void setText(CharSequence buttonText) {

			// mButtonText.setText(buttonText);
		}

		/*
		 * setTextColor方法
		 */

		public void setTextColor(int color) {

			// mButtonText.setTextColor(color);
		}

		// ----------------private attribute-----------------------------

		private ImageView mButtonImage = null;

		private TextView mButtonText = null;
	}

}
