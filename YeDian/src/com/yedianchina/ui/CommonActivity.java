package com.yedianchina.ui;

import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.activity.MainActivity;
import com.like.network.DataFetcher;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.log.Log;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.tools.Tools;
import com.yedianchina.ui.i.I;
import com.yedianchina.ui.i.IMingpianUI;
import com.yedianchina.ui.index.IndexUI;
import com.yedianchina.ui.nearby.NearbyModule;

/**
 * 继承此Activity类可以使用此类的方法 该类是提供整个项目中Activity所使用到的公共方法 该类中全属于公共方法.集成该类可以使用
 * 
 * @author 上海清水潭软件开发
 * @Time 2013年3月25日
 */
public class CommonActivity extends Activity implements Runnable {
	protected Context mContext;
	protected DataFetcher mDataFetcher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = this;
		mDataFetcher = DataFetcher.getInstance(mContext);
	}
	@Override
	protected void onResume() {
		super.onResume();
		// onresume时，取消notification显示
		EMChatManager.getInstance().activityResumed();
	}

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

	public LruCache<String, Bitmap> imagesCache;

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	/** ==============BEGAN菜单导航栏管理(ItemId)================= **/
	private static final int INDEX = 1;// 首页
	private static final int BOOK = 2;// 预约司机
	private static final int FEE = 3;// 价格表
	private static final int INVITEFRIENDS = 4;// 充值
	private static final int MORE = 5;// 更多
	private static final int EXIT = 6;
	public static boolean exit;
	public Intent intent = new Intent();
	/** ==============END菜单导航栏管理(ItemId)================= **/

	/** ==============BEGAN进度条管理(ProgressDialog)============= **/
	// public ProgressDialog progressDialog = null;
	public Handler handler = new Handler();
	private int i = 0;

	// 检查网络是否连接
	public void checkNetWork() {

		boolean isConnected = Tools.isNetworkConnected(this);
		if (!isConnected) {
			new AlertDialog.Builder(this).setTitle("提示")
					.setMessage("当前网络无法访问，请连接网络...")
					.setPositiveButton("确定", null).show();

		}
	}

	/** ==============END进度条管理(ProgressDialog)============= **/

	/** ==============BEGAN底部菜单栏管理(ImageView)============ **/
	public ImageView imageViewIndex = null;
	public TextView productListText = null;

	public ProductListIndex productListIndex = new ProductListIndex();

	class ProductListIndex implements OnClickListener {
		// 产品中心
		@Override
		public void onClick(View v) {

			// imageViewIndex.setImageResource(R.drawable.menu1_focus);
			intent.setClass(CommonActivity.this, IndexUI.class);
			intent.putExtra("selTab", "1");
			startActivityForResult(intent, 1);

		}

	}

	public TextView productTypeText = null;// menu_brand_text
	public ImageView imageViewType = null;
	public ImageView imageViewShooping = null;
	public TextView textViewShooping = null;
	public TextViewShooping textViewShoopingIndex = new TextViewShooping();
	public ImageView imageViewMyLetao = null;// 会员中心
	public TextView textICenter = null;// 会员中心
	public ImageView imageViewMore = null;

	/**
	 * 底部菜单 首页点击事件
	 */
	public ImageViewIndex viewIndex = new ImageViewIndex();
	/**
	 * 底部菜单 分类点击事件
	 */
	public ImageViewType viewType = new ImageViewType();
	/**
	 * 底部菜单 购物车点击事件
	 */
	public ImageViewShooping viewShooping = new ImageViewShooping();
	/**
	 * 底部菜单 会员中心
	 */
	public ImageViewMyLetao viewMyLetao = new ImageViewMyLetao();

	/**
	 * 底部菜单 更多点击事件
	 */
	public ImageViewMore viewMore = new ImageViewMore();
	/** ==============END底部菜单栏管理(ImageView)============ **/

	public ListView listViewAll = null;
	public TextView textViewTitle = null;

	/**
	 * 菜单栏设置
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, INDEX, 1, "首页");
		menu.add(0, BOOK, 1, "附近");
		menu.add(0, FEE, 1, "聊天");
		menu.add(0, INVITEFRIENDS, 1, "我的中心");
		menu.add(0, MORE, 1, "更多");
		menu.add(0, EXIT, 1, "退出");
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 底部菜单控件事件
	 */
	public void ButtomOnclickListenerAll() {
		this.imageViewIndex.setOnClickListener(new IndexOnclickListener());
	}

	/**
	 * 底部菜单首页控件事件 -------产品中心
	 */
	class IndexOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			intent.setClass(CommonActivity.this, IndexUI.class);
			startActivity(intent);
		}

	}

	/**
	 * 点击菜单栏中的按钮时触发该事件(onOptionsItemSelected方法)
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences preferences = getSharedPreferences(
				CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);
		Long uid = preferences.getLong("uid", 0);

		if (item.getItemId() == INDEX) {
			intent.setClass(this, IndexUI.class);
			startActivity(intent);
		} else if (item.getItemId() == BOOK) {
			intent.setClass(this, NearbyModule.class);

			startActivity(intent);
		} else if (item.getItemId() == FEE) {
			if (uid > 0) {

				String myNickname = preferences.getString(CONSTANTS.NICKNAME,
						"");
				if (myNickname == null || myNickname.length() == 0
						|| "".equals(myNickname)) {
					Toast.makeText(getApplicationContext(),
							"您的资料不完善，请去“我的中心－》右上角我的名片处完善个人资料 ", 1).show();
					intent.setClass(this, IMingpianUI.class);

				} else {

					intent.setClass(this, MainActivity.class);
				}
			} else {
				intent.setClass(this, LoginUI.class);
			}
			startActivity(intent);
		} else if (item.getItemId() == INVITEFRIENDS) {

			//

			if (uid > 0) {
				intent.setClass(this, I.class);
			} else {
				intent.setClass(this, LoginUI.class);
			}
			startActivity(intent);

		} else if (item.getItemId() == MORE) {
			intent.setClass(this, MoreUI.class);
			startActivity(intent);
		} else if (item.getItemId() == EXIT) {
			openQiutDialog();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 退出
	 */
	private void openQiutDialog() {
		new AlertDialog.Builder(this).setTitle("夜店中国").setMessage("是否退出夜店中国？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (true) {
							YeDianChinaApplication.getInstance().exit();
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
	}

	// 底部菜单栏点击事件效果设置
	class ImageViewIndex implements OnClickListener {
		// 首页
		@Override
		public void onClick(View v) {

			intent.setClass(CommonActivity.this, IndexUI.class);
			intent.putExtra("selTab", "1");
			startActivityForResult(intent, 1);

		}

	}

	class ImageViewType implements OnClickListener {
		// 类型(分类)
		@Override
		public void onClick(View v) {

			startActivity(new Intent(getApplicationContext(),
					NearbyModule.class));
			// startActivity(new
			// Intent(getApplicationContext(),MainActivity.class));

		}

	}

	class TextViewShooping implements OnClickListener {
		// 我的中心
		@Override
		public void onClick(View v) {
			SharedPreferences preferences = getSharedPreferences(
					"yedianchina_user_info", Activity.MODE_PRIVATE);
			Long uid = preferences.getLong("uid", 0);
			if (uid > 0) {
				startActivity(new Intent(getApplicationContext(), I.class));
			} else {
				startActivity(new Intent(getApplicationContext(), LoginUI.class));
			}

		}

	}

	class ImageViewShooping implements OnClickListener {
		// 我的中心
		@Override
		public void onClick(View v) {
			
			

			startActivity(new Intent(getApplicationContext(), I.class));

		}

	}

	class ImageViewMyLetao implements OnClickListener {
		
		@Override
		public void onClick(View v) {

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
				intent.setClass(getApplicationContext(), IMingpianUI.class);

			} else {

				intent.setClass(getApplicationContext(), MainActivity.class);
			}

			startActivity(intent);

		}
	}

	class ImageViewMore implements OnClickListener {
		// 更多信息
		@Override
		public void onClick(View v) {

			intent.setClass(CommonActivity.this, MoreUI.class);
			startActivity(intent);

		}

	}

	/**
	 * 调用该CommonActivity类的线程
	 */
	public void run() {

		// try {
		// //Thread.sleep(50);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// if (i > 5) {
		// if(progressDialog!=null&&progressDialog.isShowing()){
		// progressDialog.dismiss();
		// //handler.removeCallbacks(this);
		// }
		// } else {
		// i = i + 10;
		// handler.post(this);
		// }

	}

}
