package com.yedianchina.ui.index;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.GetFromWXActivity;
import net.sourceforge.simcpux.SendToWXActivity;
import net.sourceforge.simcpux.ShowFromWXActivity;
import net.sourceforge.simcpux.Util;
import net.sourceforge.simcpux.uikit.CameraUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
import com.drocode.swithcer.GuideGallery;
import com.drocode.swithcer.ImageAdapter;
import com.easemob.chat.activity.MainActivity;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXAppExtendObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.yedianchina.dao.LoginUserDao;
import com.yedianchina.gallery.FocusAdapter;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.CitySelectUI;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.LoginUI;
import com.yedianchina.ui.MoreUI;
import com.yedianchina.ui.NearByMerchantDetailUI;
import com.yedianchina.ui.QianDaoUI;
import com.yedianchina.ui.R;
import com.yedianchina.ui.RecruitListUI;
import com.yedianchina.ui.SearchUI;
import com.yedianchina.ui.YeDianChinaApplication;
import com.yedianchina.ui.activity.ActivityDetailUI;
import com.yedianchina.ui.activity.NearbyActivityListUI;
import com.yedianchina.ui.i.I;
import com.yedianchina.ui.i.IMingpianUI;
import com.yedianchina.ui.job.JobsListUI;
import com.yedianchina.ui.nearby.NearbyModule;
import com.yedianchina.ui.rank.MerchantRankListUI;

@SuppressLint("NewApi")
public class IndexUI extends CommonActivity implements IWXAPIEventHandler{
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case 0x101: {
			final WXAppExtendObject appdata = new WXAppExtendObject();
			final String path = CameraUtil.getResultPhotoPath(this, data, SDCARD_ROOT + "/tencent/");
			appdata.filePath = path;
			appdata.extInfo = "this is ext info";

			final WXMediaMessage msg = new WXMediaMessage();
			msg.setThumbImage(Util.extractThumbNail(path, 150, 150, true));
			msg.title = "this is title";
			msg.description = "this is description";
			msg.mediaObject = appdata;
			
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = buildTransaction("appdata");
			req.message = msg;
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
			api.sendReq(req);
			
			finish();
			break;
		}
		default:
			break;
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setTitle("夜店中国")
					.setMessage("是否退出夜店中国？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									YeDianChinaApplication.getInstance().exit();

								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).show();
			return true;
		}
		return false;
	}

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {

				// progressDialog.dismiss();

				int what = msg.what;
				if (what == 1) {

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
	};

	private ProgressDialog progressDialog;

	FrameLayout mMapViewContainer = null;

	Button testUpdateButton = null;

	EditText indexText = null;

	int index = 0;
	LocationData locData = null;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// Toast.makeText(LocationOverlayDemo.this, "msg:" +msg.what,
			// Toast.LENGTH_SHORT).show();
		};
	};

	private Gallery gallery;
	private TextView titleText;
	private TextView contentText;
	private FocusAdapter adapter;
	private TextView focusPointImage;
	
	public List<String> urls;
	public GuideGallery images_ga;
	private int positon = 0;
	private Thread timeThread = null;
	public boolean timeFlag = true;
	private boolean isExit = false;

	Uri uri;
	Intent intent;
	int gallerypisition = 0;

	public void changePointView(int cur) {
		LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		View view = pointLinear.getChildAt(positon);
		View curView = pointLinear.getChildAt(cur);
		if (view != null && curView != null) {
			ImageView pointView = (ImageView) view;
			ImageView curPointView = (ImageView) curView;
			pointView.setBackgroundResource(R.drawable.feature_point);
			curPointView.setBackgroundResource(R.drawable.feature_point_cur);
			positon = cur;
		}
	}

	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			tin.getCityName("上海");
		}

	}

	static LocateIn tin;

	public static void setLocateIn(LocateIn in) {
		tin = in;
	}

	public interface LocateIn {
		public void getCityName(String name);
	}

	private OverlayThread overlayThread;
	private Context mContext;
	private BMapManager mMapManager = null;

	int flag = 0;

	TextView ershoushebei;
	
	private IWXAPI api;
	
	private static final int THUMB_SIZE = 150;

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

		setContentView(R.layout.home);
		////
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		
		paiduiTv = (TextView) this.findViewById(R.id.paiduiTv);
		if (paiduiTv != null) {
			paiduiTv.setBackgroundResource(R.drawable.paidui);
			paiduiTv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(IndexUI.this, PaiDuiListUI.class);

					IndexUI.this.startActivity(intent);

				}
			});
		}

		ershoushebei = (TextView) this.findViewById(R.id.ershoushebei);
		if (ershoushebei != null) {
			ershoushebei.setBackgroundResource(R.drawable.ershoushebei);
			ershoushebei.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(IndexUI.this, ErshoushebeiListUI.class);

					IndexUI.this.startActivity(intent);

				}
			});
		}
		// 服装06-22
		TextView module2 = (TextView) this.findViewById(R.id.module2);
		if (module2 != null) {
			module2.setBackgroundResource(R.drawable.fuzhuang);
			module2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(IndexUI.this, TrouseListUI.class);
					startActivity(intent);
				}
			});
		}
		// 培训06-22
		TextView module3 = (TextView) this.findViewById(R.id.module3);
		if (module3 != null) {
			module3.setBackgroundResource(R.drawable.module3);
			module3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(IndexUI.this, TrainingListUI.class);

					IndexUI.this.startActivity(intent);

				}
			});
		}
		//管理
		TextView module4 = (TextView) this.findViewById(R.id.module4);
		if (module4 != null) {
			module4.setBackgroundResource(R.drawable.module4);
			module4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(IndexUI.this, ManageListUI.class);

					IndexUI.this.startActivity(intent);

				}
			});
		}
		

		this.flag = 1;
		// --------------------------------------------------------------------------------
		mMapView = (MapView) this.findViewById(R.id.map_view);
		mMapView.setVisibility(View.INVISIBLE);

		mMapController = mMapView.getController();

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
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		mMapView.refresh();

		TextView qiandaoBtn = (TextView) this.findViewById(R.id.qiandaoBtn);
		if (qiandaoBtn != null) {
			qiandaoBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Log.e("qiandaoBtnS", "qiandaoBtn");
					
					new Thread() {
						public void run() {

							Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.logo200);
							String text ="哈我最新使用来夜店中国，里面信息量大 招聘 求职 交友什么都有，有空你也来玩～";
//							if (text == null || text.length() == 0) {
//								return;
//							}
							
						
							WXTextObject textObj = new WXTextObject();
							textObj.text = "哈我最新使用来夜店中国，里面信息量大 招聘 求职 交友什么都有，有空你也来玩～";

							Bitmap thumbBmp = Bitmap.createScaledBitmap(bm, THUMB_SIZE, THUMB_SIZE, true);
							

							WXMediaMessage msg = new WXMediaMessage();
							msg.mediaObject = textObj;
							msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // 设置缩略图
	 
							msg.title ="夜店中国";
							msg.description =text;

						
							SendMessageToWX.Req req = new SendMessageToWX.Req();
						
							req.transaction ="夜店中国";
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneTimeline;
							
							
							api.sendReq(req);
							
						}
					}.start();
					
					
					Intent intent = new Intent();
					intent.setClass(IndexUI.this, QianDaoUI.class);

					IndexUI.this.startActivity(intent);
					
					
					
//					
//					Intent bintent = new Intent(IndexUI.this, SendToWXActivity.class);
//					startActivityForResult(bintent,1);  

				}
			});
		}

		// 点击搜索
		search_tv = (TextView) this.findViewById(R.id.search_tv);
		if (search_tv != null) {
			search_tv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(IndexUI.this, SearchUI.class);

					IndexUI.this.startActivity(intent);
				}
			});
		}
		// ///////
		selectCityIdBtn = (TextView) this
				.findViewById(R.id.selectCityIdBtn);
		if (selectCityIdBtn != null) {
			selectCityIdBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();

					intent.setClass(IndexUI.this, CitySelectUI.class);

					IndexUI.this.startActivity(intent);
				}
			});

		}

		// 夜店招聘
		recruitID = (TextView) this.findViewById(R.id.recruitID);
	 
		recruitID.setBackgroundDrawable(new BitmapDrawable(readBitMap(this.getApplicationContext(), R.drawable.recruit)));
		recruitID.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(IndexUI.this, RecruitListUI.class);

				IndexUI.this.startActivity(intent);

			}
		});
		// 夜店排名
		merchantRankTv = (TextView) this.findViewById(R.id.merchantRankTv);
		merchantRankTv.setBackgroundDrawable(new BitmapDrawable(readBitMap(this.getApplicationContext(), R.drawable.merchantrank)));
		merchantRankTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(IndexUI.this, MerchantRankListUI.class);

				IndexUI.this.startActivity(intent);
			}
		});
		// 附近夜店
		nearByMerchantTv = (TextView) this.findViewById(R.id.nearByMerchantTv);
	 
		nearByMerchantTv.setBackgroundDrawable(new BitmapDrawable(readBitMap(this.getApplicationContext(), R.drawable.nearby_merchant)));
		nearByMerchantTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(IndexUI.this, NearByMerchantListUI.class);

				IndexUI.this.startActivity(intent);

			}
		});
		// 夜店求职
		jobsTv = (TextView) this.findViewById(R.id.jobsTv);
		 
		jobsTv.setBackgroundDrawable(new BitmapDrawable(readBitMap(this.getApplicationContext(), R.drawable.jobs)));
		jobsTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(IndexUI.this, JobsListUI.class);

				IndexUI.this.startActivity(intent);
			}
		});
		// 附近活动
		nearByActivityTv = (TextView) this.findViewById(R.id.nearByActivityTv);
	 
		nearByActivityTv.setBackgroundDrawable(new BitmapDrawable(readBitMap(this.getApplicationContext(), R.drawable.nearbyactivity)));
		nearByActivityTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(IndexUI.this, NearbyActivityListUI.class);

				IndexUI.this.startActivity(intent);
			}
		});

		parentControl();
		SharedPreferences preferences = getSharedPreferences(CONSTANTS.YEDIANCHINA_USER_INFO,
				Activity.MODE_PRIVATE);
		final Long id = preferences.getLong("uid", 0);
		if (id != null && id > 0) {

			new Thread() {
				public void run() {

					advList = LoginUserDao.updateLastLogin(id);
					advHandler.sendEmptyMessage(1);
				}
			}.start();
		} else {
			new Thread() {
				public void run() {

					advList = LoginUserDao.updateLastLogin();
					advHandler.sendEmptyMessage(1);
				}
			}.start();
		}
		
		
		
		
		IntentFilter filter = new IntentFilter(CitySelectUI.action);
		registerReceiver(broadcastReceiver, filter);

	}
	TextView selectCityIdBtn;
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
		
			String selectCityFn = intent.getExtras().getString(
					"selectCityFn");
			String selectCity = intent.getExtras().getString(
					"selectCity");
			
			if ("1".equals(selectCityFn)) {
				  
				selectCityIdBtn.setText(selectCity+"▼");
				
			}else{
				selectCityIdBtn.setText("上海▼");
			}
		}
	};

	JSONArray advList;
	Handler advHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				init();
			}

		};
	};

	private void init() {
		try {
		
			int maxMemory = (int) Runtime.getRuntime().maxMemory();
			int cacheSize = maxMemory / 8;
			// 设置图片缓存大小为程序最大可用内存的1/8
			imagesCache = new LruCache<String, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return bitmap.getByteCount();
				}
			};

			List<String> imageUrls = new ArrayList<String>();
			for (int i = 0; i < advList.length(); i++) {
				JSONObject jo = advList.getJSONObject(i);
				String url = jo.getString("url");
				url = CONSTANTS.HOST + url;
				imageUrls.add(url);
			}

			images_ga = (GuideGallery) findViewById(R.id.image_wall_gallery);

			images_ga.setImageActivity(IndexUI.this);

			ImageAdapter imageAdapter = new ImageAdapter(imageUrls, this);
			images_ga.setAdapter(imageAdapter);

			LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
			pointLinear.removeAllViews();
			pointLinear.setBackgroundColor(Color.argb(200, 135, 135, 152));
			for (int i = 0; i < imageUrls.size(); i++) {
				ImageView pointView = new ImageView(this);
				if (i == 0) {
					pointView
							.setBackgroundResource(R.drawable.feature_point_cur);
				} else
					pointView.setBackgroundResource(R.drawable.feature_point);
				pointLinear.addView(pointView);
			}
			images_ga.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					try {
						System.out.println(arg2 + "images_ga");
						arg2 = arg2 % advList.length();
						JSONObject jo = advList.getJSONObject(arg2);

						int flag = jo.getInt("flag");
						if (flag == 1) {
							Intent intent = new Intent();
							Long pk = jo.getLong("target_id");
							intent.setClass(IndexUI.this,
									NearByMerchantDetailUI.class);
							intent.putExtra("pk", pk);
							IndexUI.this.startActivity(intent);
						}
						if (flag == 2) {

							Intent intent = new Intent();
							Long pk = jo.getLong("target_id");
							intent.setClass(IndexUI.this,
									ActivityDetailUI.class);
							intent.putExtra("pk", pk);
							IndexUI.this.startActivity(intent);

						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			if (flag == 1) {
				flag = 0;
				locData.latitude = location.getLatitude();

				locData.longitude = location.getLongitude();

				locData.accuracy = location.getRadius();
				locData.direction = location.getDerect();
				myLocationOverlay.setData(locData);
				mMapView.refresh();

				double latitude = locData.latitude;
				double longitude = locData.longitude;

				Log.e("latitude", "latitude=============" + latitude);
				Log.e("longitude", "longitude=============" + longitude);

				// latitude=latitude+0.0014;
				// longitude=longitude-0.0016;//加偏移 指向您在这里

				GeoPoint pt = new GeoPoint((int) (latitude * 1e6),
						(int) (longitude * 1e6));

				mMapController.animateTo(new GeoPoint(
						(int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6)), mHandler
						.obtainMessage(1));

				BDLocation tmpLocation = location;

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

	// 定位相关
	MyLocationOverlay myLocationOverlay = null;
	public MKMapViewListener mMapListener = null;
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	public NotifyLister mNotifyer = null;

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
		}
	}

	static MapView mMapView = null;
	private MapController mMapController = null;

	TextView search_tv;// 搜索
	TextView recruitID;
	TextView merchantRankTv;
	TextView nearByMerchantTv;

	TextView jobsTv;// 夜店求职
	TextView nearByActivityTv;// 附近活动
	TextView paiduiTv;// 明星派对

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
		 unregisterReceiver(broadcastReceiver);  

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

	private float density = 0;

	//
	/**
	 * 获取屏幕的密度
	 */
	private void getDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		density = metrics.density;
	}

	private void parentControl() {
		// 获取密度
		this.getDensity();

	 
		imageViewIndex = (ImageView) findViewById(R.id.menu_home_img);
		imageViewIndex.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 
				
			}
		});
	 

		imageViewType = (ImageView) findViewById(R.id.menu_brand_img);
		imageViewType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				startActivity(new Intent(getApplicationContext(),
						NearbyModule.class));
				IndexUI.this.finish();
				
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
				if(uid==null||uid==0){
					Toast.makeText(getApplicationContext(),
							"您尚未登录,请先登录", 1).show();
					startActivity(new Intent(getApplicationContext(), LoginUI.class));
					return;
				}
				
				

				String myNickname = preferences.getString(CONSTANTS.NICKNAME, "");
				if (myNickname == null || myNickname.length() == 0
						|| "".equals(myNickname)) {
					Toast.makeText(getApplicationContext(),
							"您的资料不完善，请去“我的中心－》右上角我的名片处完善个人资料 ", 1).show();
					
					startActivity(new Intent(getApplicationContext(), IMingpianUI.class));

				} else {
				 
					
				 
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
					IndexUI.this.finish();
				}
				
				

			
				
			}
		});
		

		imageViewMyLetao = (ImageView) findViewById(R.id.menu_shopping_cart_img);
		imageViewMyLetao.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 
			 
				
				startActivity(new Intent(getApplicationContext(),
						I.class));
				IndexUI.this.finish();
				
			 
				
			}
		});

		imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
		imageViewMore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 
				startActivity(new Intent(getApplicationContext(), MoreUI.class));
				IndexUI.this.finish();
			}
		});

	}
	//////////////////////////以下是微信分享///////////////////////////////////////////////////////////////////////

	 @Override
		public void onReq(BaseReq req) {
			switch (req.getType()) {
			case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
				goToGetMsg();		
				break;
			case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
				goToShowMsg((ShowMessageFromWX.Req) req);
				break;
			default:
				break;
			}
		}

		
		@Override
		public void onResp(BaseResp resp) {
			int result = 0;
			
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = R.string.errcode_success;
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = R.string.errcode_cancel;
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = R.string.errcode_deny;
				break;
			default:
				result = R.string.errcode_unknown;
				break;
			}
			
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		}
		
		private void goToGetMsg() {
			Intent intent = new Intent(this, GetFromWXActivity.class);
			intent.putExtras(getIntent());
			startActivity(intent);
			finish();
		}
		
		private void goToShowMsg(ShowMessageFromWX.Req showReq) {
			WXMediaMessage wxMsg = showReq.message;		
			WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
			
			StringBuffer msg = new StringBuffer(); 
			msg.append("description: ");
			msg.append(wxMsg.description);
			msg.append("\n");
			msg.append("extInfo: ");
			msg.append(obj.extInfo);
			msg.append("\n");
			msg.append("filePath: ");
			msg.append(obj.filePath);
			
			Intent intent = new Intent(this, ShowFromWXActivity.class);
			intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
			intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
			intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
			startActivity(intent);
			finish();
		}
}
