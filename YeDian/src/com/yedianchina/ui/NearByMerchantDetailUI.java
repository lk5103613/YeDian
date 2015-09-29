package com.yedianchina.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.control.XListView;
import com.yedianchina.dao.CommentService;
import com.yedianchina.dao.MerchantDao;
import com.yedianchina.po.MerchantPO;
import com.yedianchina.tools.CONSTANTS;

import com.yedianchina.ui.index.SendMsg2MerchantUI;

@SuppressLint("NewApi")
public class NearByMerchantDetailUI extends Activity implements
		XListView.IXListViewListener {

	// http://www.cnblogs.com/winxiang/archive/2012/09/24/2700100.html
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

	private ProgressDialog progressDialog;
	TextView qiandaoBtn;

	protected void onResume() {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}

	public static HashMap<String, Bitmap> imagesCache = new HashMap<String, Bitmap>(); // 图片缓存

	private TextView workYearTextView = null;// 开车工龄

	private TextView callBtn = null;// 打电话
	ArrayList<HashMap<String, Object>> list;
	private MerchantPO merchantPO;
	private long merchantId;

	public void updateCurrendData() {
		this.merchantPO = MerchantDao.loadMerchantInfo(merchantId);

	}

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				setMerchantDetailData();

			}
		};
	};
	TextView titleTxtView;
	ImageView merchantLogo;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == 8) {
			initView();
		}
	}

	TextView addrTv;
	LinearLayout linkLL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.nearby_merchant_detail);
		addrTv = (TextView) this.findViewById(R.id.addrTv);

		telTv = (TextView) this.findViewById(R.id.telTv);

		linkLL = (LinearLayout) this.findViewById(R.id.linkLL);

		qiandaoBtn = (TextView) this.findViewById(R.id.qiandaoBtn);// 签到
		if (qiandaoBtn != null) {
			qiandaoBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {

				}
			});
		}
		list = new ArrayList<HashMap<String, Object>>();

		this.titleTxtView = (TextView) findViewById(R.id.tvHeaderTitle);

		Bundle bundle = this.getIntent().getExtras();
		Long pk = bundle.getLong("pk");
		this.merchantId = pk;
		// Files.mkdir(this);
		// Bitmap image = BitmapFactory.decodeResource(getResources(),
		// R.drawable.logo);
		// imagesCache.put("background_non_load", image); // 设置缓存中默认的图片
		initView();

		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					NearByMerchantDetailUI.this.finish();

				}
			});

		}

		merchantLogo = (ImageView) this.findViewById(R.id.merchantLogo);

		new Thread() {
			public void run() {

				updateCurrendData();

				loadingHandler.sendEmptyMessage(1);

			}
		}.start();

		//
		TextView star = (TextView) this.findViewById(R.id.star);
		if (star != null) {
			star.setBackgroundResource(R.drawable.star1);
		}

		TextView tvHeaderTitle = (TextView) this
				.findViewById(R.id.NavigateTitle);
		if (tvHeaderTitle != null) {
			tvHeaderTitle.setText("夜店详情");
		}

		TextView regBtn = (TextView) this.findViewById(R.id.mingpianBtn);
		if (regBtn != null) {
			regBtn.setVisibility(View.INVISIBLE);
		}

		// /////////////////
		ImageView work_year = (ImageView) this.findViewById(R.id.work_year);
		work_year.setBackgroundDrawable(new BitmapDrawable(readBitMap(
				this.getApplicationContext(), R.drawable.star_tmp)));
		// ////
		ImageView red_line = (ImageView) this.findViewById(R.id.red_line);
		red_line.setBackgroundDrawable(new BitmapDrawable(readBitMap(
				this.getApplicationContext(), R.drawable.aoxian)));

		// ///////
		ImageView send_msgTv = (ImageView) this.findViewById(R.id.send_msgTv);
		send_msgTv.setBackgroundDrawable(new BitmapDrawable(readBitMap(
				this.getApplicationContext(), R.drawable.send_msg)));
		// SendMsg2MerchantUI
		send_msgTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				System.out.println("-------发私信");
				Intent intent = new Intent();
				intent.setClass(NearByMerchantDetailUI.this,
						SendMsg2MerchantUI.class);

				NearByMerchantDetailUI.this.startActivity(intent);

			}
		});

		//
		ImageView i_already_comeTv = (ImageView) this
				.findViewById(R.id.i_already_comeTv);
		i_already_comeTv.setBackgroundDrawable(new BitmapDrawable(readBitMap(
				this.getApplicationContext(), R.drawable.gz)));
		i_already_comeTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), "关注成功!", 1).show();

			}
		});

		ImageView yedian_xiuTv = (ImageView) this
				.findViewById(R.id.yedian_xiuTv);
		yedian_xiuTv.setBackgroundDrawable(new BitmapDrawable(readBitMap(
				this.getApplicationContext(), R.drawable.yedian_xiu)));

		// LinearLayout
		// qiandao_frame=(LinearLayout)this.findViewById(R.id.qiandao_frame);
		// qiandao_frame.setBackgroundDrawable(new
		// BitmapDrawable(readBitMap(this.getApplicationContext(),
		// R.drawable.qiandao0627)));
		// //
		// LinearLayout
		// link_frame=(LinearLayout)this.findViewById(R.id.link_frame);
		// link_frame.setBackgroundDrawable(new
		// BitmapDrawable(readBitMap(this.getApplicationContext(),
		// R.drawable.tel_ddress)));

		ImageView telImgTv = (ImageView) this.findViewById(R.id.telImgTv);
		telImgTv.setBackgroundDrawable(new BitmapDrawable(readBitMap(
				this.getApplicationContext(), R.drawable.tel)));

		LinearLayout xfjjframe = (LinearLayout) this
				.findViewById(R.id.xfjjframe);
		// xfjjframe.setBackgroundDrawable(new
		// BitmapDrawable(readBitMap(this.getApplicationContext(),
		// R.drawable.xfjj)));

		//
		ImageView nearby_img = (ImageView) this.findViewById(R.id.nearby_img);
		nearby_img.setBackgroundDrawable(new BitmapDrawable(readBitMap(
				this.getApplicationContext(), R.drawable.nearby)));

		// /////字体加粗
		TextView telPreTv = (TextView) this.findViewById(R.id.telPreTv);
		TextPaint paint = telPreTv.getPaint();
		paint.setFakeBoldText(true);
		//

		TextView merchant_introPreTv = (TextView) this
				.findViewById(R.id.merchant_introPreTv);
		paint = merchant_introPreTv.getPaint();
		paint.setFakeBoldText(true);
		// xfjjPreTv

		TextView xfjjPreTv = (TextView) this.findViewById(R.id.xfjjPreTv);
		paint = xfjjPreTv.getPaint();
		paint.setFakeBoldText(true);

	}

	int allCnt;

	private void loadData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				CommentService dao = new CommentService();
				currentPage++;
				list.clear();

				Map resultMap = dao.findMerchantCommentList(merchantId,
						currentPage);

				ArrayList<HashMap<String, Object>> tmp = null;

				if (resultMap != null) {
					tmp = (ArrayList<HashMap<String, Object>>) resultMap
							.get("list");
				}

				if (tmp != null && tmp.size() > 0) {
					list.addAll(tmp);
				}

				if (resultMap != null) {
					allCnt = (Integer) resultMap.get("allCnt");
				}

				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.what = WHAT_DID_LOAD_DATA;
				msg.sendToTarget();

			}
		}).start();
	}

	private Handler callHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 1) {
				progressDialog.dismiss();
				Intent phoneIntent = new Intent("android.intent.action.CALL",

				Uri.parse("tel:" + inputStr));
				startActivity(phoneIntent);
			}
		}
	};

	private Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 7:
				/*
				 * TextView t = (TextView) mPullDownView
				 * .findViewById(R.id.pulldown_footer_text);
				 * 
				 * ProgressBar pulldown_footer_loading = (ProgressBar)
				 * mPullDownView .findViewById(R.id.pulldown_footer_loading); if
				 * (pulldown_footer_loading != null) {
				 * pulldown_footer_loading.setVisibility(View.INVISIBLE); } if
				 * (t != null) { t.setText("已经是最后一页"); }
				 */

				if (list != null && list.size() > 0) {
					// mStrings.addAll(strings);
					mAdapter.changeData(list);
				}
				System.out.println("laile7" + list.size());
				// 诉它数据加载完毕;
				mListView.stopRefresh();
				mListView.stopLoadMore();

				break;
			case WHAT_DID_LOAD_DATA: {
				System.out.println("WHAT_DID_REFRESH" + list.size());
				mListView.stopRefresh();
				Log.e("", "WHAT_DID_LOAD_DATA------" + list.size());
				if (list != null && list.size() > 0) {
					// mStrings.addAll(strings);
					mAdapter.changeData(list);
				}

				// 诉它数据加载完毕;
				// mListView.setPullLoadEnable(false);
				break;
			}
			case WHAT_DID_REFRESH: {
				// String body = (String) msg.obj;
				// mStrings.add(0, body);
				if (list != null && list.size() > 0) {
					mAdapter.changeData(list);
				}

				// 告诉它更新完�?
				// mPullDownView.notifyDidRefresh();
				break;
			}

			case WHAT_DID_MORE: {
				// String body = (String) msg.obj;
				// mStrings.add(body);
				if (list != null && list.size() > 0) {
					mAdapter.changeData(list);
				}
				// 告诉它获取更多完�?
				// mPullDownView.notifyDidMore();
				break;
			}
			}

		}

	};

	public XListView mListView;
	private CustomAdapter mAdapter;
	int currentPage;
	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 2;
	private static final int WHAT_DID_MORE = 1;

	// ---------------------------------------------
	public class CustomAdapter extends BaseAdapter {

		private ArrayList<HashMap<String, Object>> _list;
		private LayoutInflater mInflater;

		public class ViewHolder {
			TextView addTime;
			TextView mp;
			ImageView star;
			TextView content;
			TextView drivingLicenceTv;
			TextView statusTv;
			TextView starTv;
			TextView driveCntTv;
		}

		public CustomAdapter(Context context,
				ArrayList<HashMap<String, Object>> list) {
			mInflater = LayoutInflater.from(context);
			_list = list;

		}

		@Override
		public int getCount() {
			return _list.size();
		}

		@Override
		public Object getItem(int position) {
			return _list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void changeData(ArrayList<HashMap<String, Object>> list) {
			this._list = list;
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_items, null);
				holder = new ViewHolder();
				holder.star = (ImageView) convertView.findViewById(R.id.star);
				holder.mp = (TextView) convertView.findViewById(R.id.mp);

				holder.addTime = (TextView) convertView
						.findViewById(R.id.addTime);
				holder.content = (TextView) convertView
						.findViewById(R.id.content);
				holder.statusTv = (TextView) convertView
						.findViewById(R.id.statusTv);
				holder.starTv = (TextView) convertView
						.findViewById(R.id.starTv);
				holder.driveCntTv = (TextView) convertView
						.findViewById(R.id.driveCntTv);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			HashMap<String, Object> m = (HashMap<String, Object>) _list
					.get(position);

			String content = (String) m.get("content");
			String mp = (String) m.get("mp");
			String addTime = (String) m.get("addTime");
			holder.addTime.setText(addTime);

			holder.content.setText(content);
			holder.mp.setText(mp);

			//
			Integer star = (Integer) m.get("star");
			if (star != null) {
				if (star == 1) {
					holder.star.setBackgroundResource(R.drawable.star1);
				}
				if (star == 2) {
					holder.star.setBackgroundResource(R.drawable.star2);
				}
				if (star == 3) {
					holder.star.setBackgroundResource(R.drawable.star3);
				}
				if (star == 4) {
					holder.star.setBackgroundResource(R.drawable.star4);
				}
				if (star == 5) {
					holder.star.setBackgroundResource(R.drawable.star5);
				}
			}

			return convertView;

		}
	}

	SimpleAdapter listItemAdapter;

	@Override
	protected void onDestroy() {

		super.onDestroy();

	}

	private String mp;

	private void initView() {
		SharedPreferences preferences = getSharedPreferences(
				CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);
		String _mp = preferences.getString("mp", "");
		if (_mp != null && _mp.length() == 11) {
			this.mp = _mp;
		}
	}

	String inputStr;

	TextView telTv;

	/**
	 * 加载(绑定)所有控件
	 * 
	 * @Author TanRuixiang
	 */
	private void setMerchantDetailData() {
		TextView merchantnameTv = (TextView) findViewById(R.id.merchantname);
		String merchantName = merchantPO.getName();
		if (merchantName != null && merchantName.length() > 0) {
			merchantnameTv.setText(merchantName);
		} else {
			merchantnameTv.setText("");
		}

		String addr = merchantPO.getAddr();
		if (addr != null && addr.length() > 2) {
			addrTv.setText(addr);
		}

		TextView merchant_introTv = (TextView) findViewById(R.id.merchant_introTv);
		String content = merchantPO.getDesc();
		if (content != null && content.length() > 0) {

			merchant_introTv.setText(Html.fromHtml(content));
		}

		if (telTv != null) {
			String tel = merchantPO.getTel();
			String mp = merchantPO.getMp();
			if (tel != null && tel.length() >= 3) {
				telTv.setText(tel);
			} else if (mp != null && mp.length() >= 8) {
				telTv.setText(mp);
			}

			linkLL.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					//
					inputStr = merchantPO.getTel();

					if (inputStr != null && inputStr.length() > 0
							&& inputStr.trim().length() != 0) {

						//

						AlertDialog.Builder alert = new AlertDialog.Builder(
								NearByMerchantDetailUI.this);
						alert.setTitle("拨打电话确认")
								.setMessage("您确认拨打该夜店电话吗？")
								.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {

												dialog.dismiss();

												// progressDialog =
												// ProgressDialog
												// .show(NearByMerchantDetailUI.this,
												// "夜店中国",
												// "正在处理中，请稍后....",
												// true);
												// progressDialog.show();

											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();

											}
										});
						alert.create().show();

					}

				}
			});
		}

		String logo = merchantPO.getLogo();
		if (logo != null && logo.length() > 5) {

			merchantLogo.setTag(merchantPO.getLogo());

			ImageLoader.getInstance().displayImage(logo, merchantLogo);

		} else {
			merchantLogo.setBackgroundResource(R.drawable.tmp_m);
		}

	}

	@Override
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// try {
				// Thread.sleep(2000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				Message msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
				msg.what = WHAT_DID_REFRESH;
				msg.sendToTarget();
			}
		}).start();

	}

	@Override
	public void onLoadMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				CommentService dao = new CommentService();
				currentPage++;

				Map resultMap = dao.findMerchantCommentList(merchantId,
						currentPage);
				ArrayList<HashMap<String, Object>> tmp = (ArrayList<HashMap<String, Object>>) resultMap
						.get("list");
				allCnt = (Integer) resultMap.get("allCnt");
				list.addAll(tmp);

				Message msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
				msg.obj = "After more " + System.currentTimeMillis();
				if (tmp == null || tmp.size() == 0 || list.size() == allCnt) {
					msg.what = 7;
				}
				msg.sendToTarget();
			}
		}).start();

	}

}
