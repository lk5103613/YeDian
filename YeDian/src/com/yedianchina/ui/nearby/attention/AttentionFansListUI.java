package com.yedianchina.ui.nearby.attention;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
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
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.control.XListView;
import com.yedianchina.dao.FansDao;
import com.yedianchina.po.Area;
import com.yedianchina.po.SexPO;
import com.yedianchina.po.UserPO;
import com.yedianchina.tools.JobsPopMenu;
import com.yedianchina.tools.PPCPopMenu;
import com.yedianchina.tools.SexPopMenu;
import com.yedianchina.ui.CommonActivity;

import com.yedianchina.ui.R;
import com.yedianchina.ui.group.GroupDetailUI;
import com.yedianchina.ui.group.GroupListUI;
 
import com.yedianchina.ui.nearby.NearbyUserUI;

//粉丝
public class AttentionFansListUI extends CommonActivity implements XListView.IXListViewListener{

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
		TextView lastLoginTv;
		TextView distanceTv;
		ImageView avatarImg;
		TextView descTv;
		TextView addtimeTv;
		TextView distanceAndTimeTv;
		TextView ageTv;

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
				convertView = mInflater.inflate(R.layout.nearby_fanse_cell,
						null);
				holder = new ViewHolder();
				holder.titleTv = (TextView) convertView
						.findViewById(R.id.titleTv);

				holder.lastLoginTv = (TextView) convertView
						.findViewById(R.id.lastLoginTv);
				
				holder.distanceTv = (TextView) convertView
						.findViewById(R.id.distanceTv);
				
				

				holder.avatarImg = (ImageView) convertView
						.findViewById(R.id.logoImg);
			 
					 
				holder.addtimeTv = (TextView) convertView
						.findViewById(R.id.addtimeTv);
				holder.distanceAndTimeTv = (TextView) convertView
						.findViewById(R.id.distanceAndTimeTv);

				holder.ageTv = (TextView) convertView.findViewById(R.id.ageTv);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final UserPO m = (UserPO) _list.get(position);

			holder.titleTv.setText(m.getNickname());

			String juli = m.getDistance();
			if (juli == null || juli.length() == 0) {
				juli = "";
			}

			String last_login = m.getLast_login();
			if (last_login != null && last_login.length() >= 8) {
                if(juli!=null){
				holder.lastLoginTv.setText(last_login);
                }
			} 
			//////
			String distance = m.getDistance();
			if (distance != null && distance.length() >0) {
                if(juli!=null){
				holder.distanceTv.setText(distance);
                }
			} 
			
			

			
			
			

			String url = m.getAvatar();// 用户头像

			int age = m.getAge();
			if (age > 0) {
				holder.ageTv.setText(" " + age);
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
						holder.avatarImg.setTag(url0);
					 
						
						 ImageLoader.getInstance().displayImage(url0, holder.avatarImg);
						
						
					}
				}
			} else {
				holder.avatarImg.setBackgroundResource(R.drawable.icon);
			}
			
			
			  final Long  pk=m.getUid();
			  final String _distance = m.getDistance();
		      convertView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						intent.setClass(AttentionFansListUI.this, NearbyUserUI.class);

					 
					 
						intent.putExtra("pk", pk);
						intent.putExtra("distance", _distance);
						Log.e("主键", "pk=" + pk);
						startActivity(intent);
						
					}
				});

			return convertView;

		}
	}

	ImageLoader imageLoader;

	List<UserPO> list;
	int currentPage;
	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 2;
	private static final int WHAT_DID_MORE = 1;

	public XListView mListView;
	private CustomAdapter mAdapter;

 

	String longitude;
	String latitude;

	private void loadData() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				currentPage=0;
				list.clear();

				SharedPreferences locInfo = getSharedPreferences("loc",
						Activity.MODE_PRIVATE);
				longitude = locInfo.getString("longitude", "");
				latitude = locInfo.getString("latitude", "");

				Log.e("", "longitude-----" + longitude);

				Map<String, Object> resultMap = FansDao.pageList(currentPage,
						longitude, latitude,uid);
				List<UserPO> tmp = (List<UserPO>) resultMap.get("list");
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

				SharedPreferences locInfo = getSharedPreferences("loc",
						Activity.MODE_PRIVATE);
				longitude = locInfo.getString("longitude", "");
				latitude = locInfo.getString("latitude", "");

				Map<String, Object> resultMap = FansDao.pageList(currentPage,
						longitude, latitude,uid);

				List<UserPO> tmp = (List<UserPO>) resultMap.get("list");
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
	
	public List<String> urls;
	TextView commentBtn;
	Long uid;
	TextView district_btn;
	TextView job_btn;
	TextView sex_btn;

	PPCPopMenu baoxuan_popMenu;
	List<Area> areaList;

	List<UserPO> userList;
	List<SexPO> sexList;

	JobsPopMenu jobsPopMenu;
	SexPopMenu sexPopMenu;
	private Handler loadAreaHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {

			}

		}
	};

	TextView backBtn;

	int screenWidth;

	TextView search_tv;
	TextView publishJobBtn;

	TextView personBtn;
	
	TextView groupBtn;
	// /////
	private LinearLayout searchLayout;

	private TextView quanbu, nvsheng, nansheng, juli, renqi, zaixian;

	 

 
	 

	 

 



	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.nearby_fans_list);

		// ////筛选
		ImageView backBtn=(ImageView)this.findViewById(R.id.backBtn);
		if(backBtn!=null){
			backBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AttentionFansListUI.this.finish();
					
				}
			});
		}

		

		TextView  navigateTitle=(TextView)this.findViewById(R.id.NavigateTitle);
		if(navigateTitle!=null){
			navigateTitle.setText("TA的粉丝");
		}
		
		TextView  qiandaoBtn=(TextView)this.findViewById(R.id.qiandaoBtn);
		if(qiandaoBtn!=null){
			qiandaoBtn.setVisibility(View.INVISIBLE);
		}
		
	

		
	

	

		list = new ArrayList<UserPO>();


	
		uid =this.getIntent().getExtras().getLong("pk",0);
		
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
		/*
		 * 1.使用PullDownView 2.设置OnPullDownListener 3.从mPullDownView里面获取ListView
		 */
	
		 
	
	}
	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

 

	
	private float density = 0;

	/**
	 * 获取屏幕的密度
	 */
	private void getDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		density = metrics.density;
	}

}

