package com.yedianchina.ui.index;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.control.XListView;
import com.yedianchina.dao.NearbyMerchantDao;
import com.yedianchina.po.Area;
import com.yedianchina.po.MerchantPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.tools.PPCPopMenu;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.NearByMerchantDetailUI;

import com.yedianchina.ui.R;
import com.yedianchina.ui.activity.NearbyActivityListUI;
import com.yedianchina.ui.i.IAttentionMerchantFragment.CustomAdapter;


//附近夜店
public class NearByMerchantListUI extends CommonActivity implements
XListView.IXListViewListener{

	int STEP = 0;
	int areaId;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("onActivityResult", "" + requestCode);
		if (resultCode == 802) {// 刷新评论
			new Thread() {

			}.start();

		}
	}

	private class ViewHolder {
		TextView titleTv;
		TextView merchantNameTv;
		TextView addressTv;
		ImageView logoImg;
		TextView descTv;
		ImageView paiming;
		TextView starTv;

	}

	class CustomAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private List _list;
		int count;

		@Override
		public int getCount() {

			return _list.size();
		}

		@Override
		public Object getItem(int position) {

			return position;
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		public CustomAdapter(Context context, List list) {
			mInflater = LayoutInflater.from(context);
			_list = list;

		}
		public void changeData(List list) {
			this._list = list;
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.nearby_merchant_cell2,
						null);
				holder = new ViewHolder();
				holder.titleTv = (TextView) convertView
						.findViewById(R.id.titleTv);

				holder.merchantNameTv = (TextView) convertView
						.findViewById(R.id.merchantNameTv);

				holder.logoImg = (ImageView) convertView
						.findViewById(R.id.logoImg);
				holder.addressTv = (TextView) convertView
						.findViewById(R.id.addressTv);
				holder.starTv = (TextView) convertView
						.findViewById(R.id.starTv);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.logoImg.setBackgroundDrawable(new BitmapDrawable(readBitMap(NearByMerchantListUI.this, R.drawable.tmp_merchant)));
			

			final MerchantPO m = (MerchantPO) _list.get(position);

			holder.titleTv.setText(m.getName());
			// holder.mpTv.setText(m.getLinkNumber());
			String url = m.getLogo();

			String address = m.getAddr();
			if (address != null && address.length() > 0) {
				//holder.addressTv.setText(address);
			}
			String desc = "";
			if (desc != null && desc.length() > 0) {
				holder.descTv.setText("");
			}

			if (url != null && url.length() > 10) {
				String[] urlList = url.split(";");
				if (urlList != null && urlList.length > 0) {
					String url0 = urlList[0];
					if (url0 != null && url0.length() > 10) {

						holder.logoImg.setTag(url0);
						 ImageLoader.getInstance().displayImage(url0,holder.logoImg);
					}
				}
			} else {
				//holder.logoImg.setBackgroundResource(R.drawable.icon);
			}
			
			
		 
			//holder.starTv.setBackgroundDrawable(new BitmapDrawable(readBitMap(NearByMerchantListUI.this, R.drawable.star4)));
			
			
			String flag=m.getFlag();
			if("1".equals(flag)){
				
			}else{
				//holder.paiming.setVisibility(View.INVISIBLE);
			}

			return convertView;

		}
	}

	ImageLoader imageLoader;

	List<MerchantPO> list;
	int currentPage;
	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 2;
	private static final int WHAT_DID_MORE = 1;

	private XListView mListView;
	private CustomAdapter mAdapter;

 

	private void loadData() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				currentPage++;

				Map resultMap = NearbyMerchantDao.pageList(currentPage);
				List<MerchantPO> tmp = (List<MerchantPO>) resultMap.get("list");
				if (tmp != null && tmp.size() > 0) {
					list.addAll(tmp);
				}
				Integer allCnt = (Integer) resultMap.get("allCnt");

				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.what = WHAT_DID_LOAD_DATA;
				if (tmp == null || tmp.size() == 0 || allCnt == list.size()) {
					msg.what = 7;
				}
				msg.sendToTarget();

			}
		}).start();
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

				currentPage++;

				Map resultMap = NearbyMerchantDao.pageList(currentPage);

				List<MerchantPO> tmp = (List<MerchantPO>) resultMap.get("list");
				Integer allCnt = (Integer) resultMap.get("allCnt");
				list.addAll(tmp);

				Message msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
				msg.obj = "After more " + System.currentTimeMillis();
				if (tmp == null || tmp.size() == 0 || allCnt == list.size()) {
					msg.what = 7;
				}
				msg.sendToTarget();
			}
		}).start();
	}

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
	public Intent intent = new Intent();

	
	// ////
	public HashMap<String, Bitmap> imagesCache = new HashMap<String, Bitmap>();// 图片缓存
	public List<String> urls;
	TextView commentBtn;
	Long uid;
	TextView distanceBtn;
	TextView starBtn;
	
	
	PPCPopMenu baoxuan_popMenu;
	List<Area> areaList;
	
	
//	private Handler loadAreaHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			if (msg.what == 1) {
//				district_btn.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//						
//					}
//				});
//			}
//
//		}
//	};

	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.nearby_merchant_list);
		
	 
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					NearByMerchantListUI.this.finish();

				}
			});
		}
		

		list = new ArrayList<MerchantPO>();

	

		SharedPreferences preferences = getSharedPreferences(CONSTANTS.YEDIANCHINA_USER_INFO,
				Activity.MODE_PRIVATE);
		uid = preferences.getLong("uid", 0);

		distanceBtn = (TextView) this.findViewById(R.id.distanceBtn);
		if(distanceBtn!=null){
			
			distanceBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(this.getApplicationContext(), R.drawable.left_focus)));
			
			distanceBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
				 
					distanceBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(NearByMerchantListUI.this, R.drawable.left_focus)));
					
					distanceBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
					
					 
					
					starBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(NearByMerchantListUI.this, R.drawable.right)));
					
					
					starBtn.setTextColor(Color.parseColor("#ff404040"));
					
					
					
				}
			});
		}
		
		
		
		starBtn = (TextView) this.findViewById(R.id.starBtn);
		
		if(starBtn!=null){
			starBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(NearByMerchantListUI.this, R.drawable.right)));
			
			starBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
				 
					distanceBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(NearByMerchantListUI.this, R.drawable.left)));
					
					distanceBtn.setTextColor(Color.parseColor("#ff404040"));
					
					
				 
					starBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(NearByMerchantListUI.this, R.drawable.right_focus)));
					
					starBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
					
					
					
					
					
                  
					
					
					
					
					
				}
			});
		}
		
		
		
		
//		new Thread() {
//			public void run() {
//				AreaService dao = new AreaService();
//				areaList = dao.listArea("1");
//				loadAreaHandler.sendEmptyMessage(1);
//
//			}
//		}.start();

		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		//mListView.setOnItemClickListener(this);
		mAdapter = new CustomAdapter(this, list);
	 
		mListView.setAdapter(mAdapter);
		mListView.setDivider(getResources().getDrawable(R.drawable.aoxian));
		
		loadData();
		// TextView tvHeaderTitle = (TextView) this
		// .findViewById(R.id.tvHeaderTitle);
		// tvHeaderTitle.setText("附近夜店");
		// //////////////////////////////////////////////

	}
	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	int id;

}
