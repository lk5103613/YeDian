package com.yedianchina.ui.group;


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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.control.XListView;
import com.yedianchina.dao.AreaService;
import com.yedianchina.dao.GroupDao;
import com.yedianchina.po.Area;
import com.yedianchina.po.GroupPO;
import com.yedianchina.po.SexPO;
import com.yedianchina.po.UserPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.tools.JobsPopMenu;
import com.yedianchina.tools.PPCPopMenu;
import com.yedianchina.tools.SexPopMenu;
import com.yedianchina.ui.CommonActivity;
 
import com.yedianchina.ui.R;
import com.yedianchina.ui.job.JobReqDetailUI;
import com.yedianchina.ui.job.JobsListUI;
 
import com.yedianchina.ui.nearby.NearbyModule;

//群组
public class GroupListUI extends CommonActivity implements XListView.IXListViewListener {

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
		TextView addrTv;
		ImageView avatarImg;
		TextView contentTv;
		TextView addtimeTv;
		TextView distanceAndTimeTv;
		TextView merchant_nameTv;

	}
	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
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
				convertView = mInflater.inflate(R.layout.group_cell,
						null);
				holder = new ViewHolder();
				holder.titleTv = (TextView) convertView
						.findViewById(R.id.titleTv);

				holder.merchantNameTv = (TextView) convertView
						.findViewById(R.id.merchantNameTv);

				holder.avatarImg = (ImageView) convertView
						.findViewById(R.id.avatarImg);
				holder.contentTv = (TextView) convertView
						.findViewById(R.id.contentTv);
				holder.addtimeTv = (TextView) convertView
						.findViewById(R.id.addtimeTv);
				holder.distanceAndTimeTv = (TextView) convertView
						.findViewById(R.id.distanceAndTimeTv);

				holder.merchant_nameTv = (TextView) convertView.findViewById(R.id.merchant_nameTv);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final GroupPO m = (GroupPO) _list.get(position);

			holder.titleTv.setText(m.getGroup_name());

			String juli = m.getDistance();
			if (juli == null || juli.length() == 0) {
				juli = "";
			}

			String last_login = m.getLast_login();
			if (last_login != null && last_login.length() >= 8) {

				holder.distanceAndTimeTv.setText(juli + "公里 | " + m.getMember_cnt()+"人");
			} else {
				holder.distanceAndTimeTv.setText(juli + "公里 | "+ m.getMember_cnt()+"人");
			}

			String url = m.getAvatar();// 用户头像
			
			String merchant_name=m.getMerchantName();

			holder.merchant_nameTv.setText(merchant_name);
			String desc =m.getGroup_desc();
			if (desc != null && desc.length() > 0) {
				holder.contentTv.setText(desc);
			}else{
				holder.contentTv.setText("");
			}

			if (url != null && url.length() > 10) {
				String[] urlList = url.split(";");
				if (urlList != null && urlList.length > 0) {
					String url0 = urlList[0];
					if (url0 != null && url0.length() > 10) {
						holder.avatarImg.setTag(url0);
						
						
						 ImageLoader.getInstance().displayImage(url0,holder.avatarImg);
					}
				}
			} else {
				holder.avatarImg.setBackgroundResource(R.drawable.icon);
			}
			
			  final Long  pk=m.getGroup_id();
			  final String distance = m.getDistance();
		      convertView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						intent.setClass(GroupListUI.this, GroupDetailUI.class);

					 
					 
						intent.putExtra("pk", pk);
						intent.putExtra("distance", distance);
						Log.e("主键", "pk=" + pk);
						startActivity(intent);
						
					}
				});

			return convertView;

		}
	}

	ImageLoader imageLoader;

	List<GroupPO> list;
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

				Map<String, Object> resultMap = GroupDao.pageList(currentPage,
						longitude, latitude);
				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				if(resultMap == null) {
					msg.what = 7;
					msg.sendToTarget();
					return;
				}
				List<GroupPO> tmp = (List<GroupPO>) resultMap.get("list");
				if (tmp != null && tmp.size() > 0) {
					list.addAll(tmp);
				}
				Integer allCnt = (Integer) resultMap.get("allCnt");

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

				Map<String, Object> resultMap = GroupDao.pageList(currentPage,
						longitude, latitude);

				List<GroupPO> tmp = (List<GroupPO>) resultMap.get("list");
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

//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//
//		intent.setClass(GroupListUI.this, GroupDetailUI.class);
//
//		GroupPO po = this.list.get(position);
//		String distance = po.getDistance();
//		Long pk = po.getGroup_id();
//		intent.putExtra("pk", pk);
//		intent.putExtra("distance", distance);
//		Log.e("主键", "pk=" + pk);
//		startActivity(intent);
//	}

	// ////
	public HashMap<String, Bitmap> imagesCache = new HashMap<String, Bitmap>();// 图片缓存
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

	private void setOnclick1(final TextView textView) {
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setView1(textView);
			}
		});
	}

	private void setView1(TextView textView) {
		quanbu.setTextColor(getResources().getColor(android.R.color.black));
		nvsheng.setTextColor(getResources().getColor(android.R.color.black));
		nansheng.setTextColor(getResources().getColor(android.R.color.black));

		textView.setTextColor(getResources().getColor(R.color.search_text_rad));
	}

	private void setOnclick2(final TextView textView) {
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setView2(textView);
			}
		});
	}

	private void setView2(TextView textView) {
		juli.setTextColor(getResources().getColor(android.R.color.black));
		renqi.setTextColor(getResources().getColor(android.R.color.black));
		zaixian.setTextColor(getResources().getColor(android.R.color.black));

		textView.setTextColor(getResources().getColor(R.color.search_text_rad));
	}

	// ////

	 
	private PopupWindow popupWindow;
	private View parent;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.group_list);

		// ////筛选

		

		// //////////
		search_tv = (TextView) this.findViewById(R.id.search_tv);
		if (search_tv != null) {
			search_tv.setHint("请输入职位 等关键字");
		}
		personBtn = (TextView) this.findViewById(R.id.personBtn);
		personBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				personBtn.setTextColor(Color.parseColor("#FFFF0000"));// Red

			 
				groupBtn.setTextColor(Color.parseColor("#FF000000"));
				
				//
				 
				
				Intent intent = new Intent();
				intent.setClass(GroupListUI.this, NearbyModule.class);

				GroupListUI.this.startActivity(intent);
				GroupListUI.this.finish();
				

			}
		});

	 

		groupBtn = (TextView) this.findViewById(R.id.groupBtn);
		groupBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				groupBtn.setTextColor(Color.parseColor("#FFFF0000"));// Red

				personBtn.setTextColor(Color.parseColor("#FF000000"));
			 

			}
		});

		TextView mapBtn = (TextView) this.findViewById(R.id.backBtn);
		if (mapBtn != null) {
			mapBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					intent.setClass(GroupListUI.this, GroupDetailUI.class);
					startActivity(intent);

				}
			});

		}

		list = new ArrayList<GroupPO>();


		SharedPreferences preferences = getSharedPreferences(CONSTANTS.YEDIANCHINA_USER_INFO,
				Activity.MODE_PRIVATE);
		uid = preferences.getLong("uid", 0);

		district_btn = (TextView) this.findViewById(R.id.district_btn);
		job_btn = (TextView) this.findViewById(R.id.job_btn);
		sex_btn = (TextView) this.findViewById(R.id.sex_btn);

		job_btn = (TextView) this.findViewById(R.id.job_btn);
		new Thread() {
			public void run() {
				AreaService dao = new AreaService();
				areaList = dao.listArea("22");
				loadAreaHandler.sendEmptyMessage(1);

			}
		}.start();
		
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
	 
		// //////////////////////////////////////////////
		parentControl();
		// ///////////////
		 
		// //////////////////////////////////////////////
		/** PopupWindow的界面 */
		View contentView = getLayoutInflater()
				.inflate(R.layout.popwindow, null);

		contentView.findViewById(R.id.submit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (popupWindow.isShowing()) {
							popupWindow.dismiss();// 关闭
						}

					}
				});

		popupWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);// 取得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		/** 设置PopupWindow弹出和退出时候的动画效果 */
		// popupWindow.setAnimationStyle(R.style.animation);

		parent = this.findViewById(R.id.pull_down_view);

		final  LinearLayout  parent=(LinearLayout)this.findViewById(R.id.title);
		
		TextView selectBtn = (TextView) this.findViewById(R.id.publishJobBtn);
		selectBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//popupWindow.showAtLocation(parent, Gravity.TOP, 0, 160);
				//
				int[] location = new int[2];  
		        v.getLocationOnScreen(location);  
		        int h=parent.getHeight();
		        popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0], location[1]-popupWindow.getHeight()+h);  
				
				//

			}
		});
		
		 
		quanbu = (TextView) contentView.findViewById(R.id.quanbu);
		nvsheng = (TextView) contentView.findViewById(R.id.nvsheng);
		nansheng = (TextView) contentView.findViewById(R.id.nansheng);
		juli = (TextView) contentView.findViewById(R.id.juli);
		renqi = (TextView) contentView.findViewById(R.id.renqi);
		zaixian = (TextView) contentView.findViewById(R.id.zaixian);

		setOnclick1(quanbu);
		setOnclick1(nvsheng);
		setOnclick1(nansheng);
		setOnclick2(juli);
		setOnclick2(renqi);
		setOnclick2(zaixian);

	}

	int id;

	private void parentControl() {
		// 获取密度
		this.getDensity();

		// 底部菜单栏点击事件效果设置
		imageViewIndex = (ImageView) findViewById(R.id.menu_home_img);
		imageViewIndex.setOnClickListener(viewIndex);

		imageViewType = (ImageView) findViewById(R.id.menu_brand_img);
		imageViewType.setOnClickListener(viewType);

		imageViewShooping = (ImageView) findViewById(R.id.menu_shopping_cart_img);
		imageViewShooping.setOnClickListener(viewShooping);

		imageViewMyLetao = (ImageView) findViewById(R.id.menu_my_letao_img);
		imageViewMyLetao.setOnClickListener(viewMyLetao);

		imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
		imageViewMore.setOnClickListener(viewMore);

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

