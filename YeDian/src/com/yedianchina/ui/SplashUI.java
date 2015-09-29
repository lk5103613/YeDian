package com.yedianchina.ui;

import java.io.InputStream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.RelativeLayout;

import com.yedianchina.dao.VersionService;
import com.yedianchina.po.AppVersion;
import com.yedianchina.service.UpdateService;
import com.yedianchina.tools.Tools;
import com.yedianchina.ui.index.IndexUI;

public class SplashUI extends BaseSplashUI {
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inSampleSize = computeSampleSize(opt, -1, 128 * 128); // 计算出图片使用的inSampleSize
		opt.inJustDecodeBounds = false;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	protected void onResume() {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

	private final int SPLASH_DISPLAY_LENGHT = 2000;
	private YeDianChinaApplication myApplication;
	private int serverVersion;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		RelativeLayout  ui=(RelativeLayout)this.findViewById(R.id.ui);
		ui.setBackgroundDrawable(new BitmapDrawable(readBitMap(SplashUI.this, R.drawable.splash2)));
		
		

		boolean isConnect = Tools.isNetworkConnected(SplashUI.this);
		Log.e("SplashUI.isConnect", isConnect + "");

		if (!isConnect) {
			Log.e("SplashUI", "您当前没有连接网络，请先连接数据网络或者wifi");
			AlertDialog.Builder alert = new AlertDialog.Builder(SplashUI.this);
			alert.setTitle("信息提醒")
					.setMessage("您当前没有连接网络，请先连接数据网络或者wifi")
					.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									YeDianChinaApplication.getInstance().exit();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									YeDianChinaApplication.getInstance().exit();
								}
							});
			alert.create().show();
			//

		} else {

			new Thread() {
				@Override
				public void run() {
					try {

						AppVersion po = VersionService.getLatestVersion();
						Thread.sleep(1000);

						serverVersion = po.getVersion();
						if (po != null && po.getUrl() != null) {
							url = po.getUrl();
						}
						Message msg = new Message();
						msg.what = 1;
						verHandler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
		YeDianChinaApplication.getInstance().addActivity(this);

	}

	Handler verHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {

				int what = msg.what;
				if (what == 1) {
					//checkVersion(url);

					 Intent mainIntent = new Intent(SplashUI.this,
					 IndexUI.class);
					 SplashUI.this.startActivity(mainIntent);
					 SplashUI.this.finish();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {

		} catch (Exception e) {
			System.out.println("myDialog取消，失败！");
			// TODO: handle exception
			myDialog.dismiss();
		}
		super.onDestroy();
	}

	AlertDialog myDialog = null;

	/***
	 * 检查是否更新版本
	 */
	public void checkVersion(final String url) {
		myApplication = (YeDianChinaApplication) getApplication();

		if (myApplication.localVersion < this.serverVersion) {

			// 发现新版本，提示用户更新
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("软件升级")
					.setMessage("发现新版本,建议立即更新使用.")
					.setPositiveButton("更新",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									
									
									
									Intent updateIntent = new Intent(
											SplashUI.this, UpdateService.class);

									updateIntent.putExtra(
											"app_name",
											getResources().getString(
													R.string.app_name));
									updateIntent.putExtra("apk_download_url",
											url);

									startService(updateIntent);
									
									
									
									
									
									
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent mainIntent = new Intent(
											SplashUI.this, IndexUI.class);
									SplashUI.this.startActivity(mainIntent);
									SplashUI.this.finish();
								}
							});
			myDialog = alert.create();
			myDialog.show();

		} else {
			Intent mainIntent = new Intent(SplashUI.this, IndexUI.class);
			SplashUI.this.startActivity(mainIntent);
			SplashUI.this.finish();
		}
	}

}
