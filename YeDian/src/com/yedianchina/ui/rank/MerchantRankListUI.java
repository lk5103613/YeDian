package com.yedianchina.ui.rank;


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
import android.hardware.Camera.Area;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.control.XListView;
import com.yedianchina.dao.MerchantRankDao;
import com.yedianchina.po.MerchantPO;
import com.yedianchina.tools.PPCPopMenu;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.NearByMerchantDetailUI;
 
import com.yedianchina.ui.job.JobReqDetailUI;
import com.yedianchina.ui.job.JobsListUI;
 
import com.yedianchina.ui.R;

//夜店排名
public class MerchantRankListUI extends CommonActivity implements XListView.IXListViewListener{
	
	
	/////////////////////////////////////
	private PopupWindow popupWindow;
	private View parent;
	
	
	
	 

	public void openPopWindow(View v) {
		/**设置PopupWindow弹出后的位置*/
		popupWindow.showAtLocation(parent, Gravity.TOP, 0, 70);
	}
	
	
	
	
	 
	 
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
		TextView titleTv;//夜店名称
		TextView starTv;//
		TextView addrTv;
		ImageView logoImg;//夜店Logo
		TextView hotTv;
		TextView distanceTv;
		ImageView hg;//显示皇冠
		TextView paiming;//排名

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
				convertView = mInflater.inflate(R.layout.merchant_rank_cell, null);
				holder = new ViewHolder();
				holder.titleTv = (TextView) convertView
						.findViewById(R.id.titleTv);
				

				holder.starTv = (TextView) convertView.findViewById(R.id.starTv);
				holder.hotTv = (TextView) convertView.findViewById(R.id.hotTv);
				holder.logoImg = (ImageView) convertView
						.findViewById(R.id.logoImg);
				holder.addrTv=(TextView) convertView.findViewById(R.id.addrTv);
				holder.distanceTv = (TextView) convertView.findViewById(R.id.distanceTv);
				
				holder.hg = (ImageView) convertView
						.findViewById(R.id.hg);
				holder.paiming = (TextView) convertView
						.findViewById(R.id.paiming);
				
				
//				ImageView hg=(ImageView) convertView
//				.findViewById(R.id.hg);
//				if(position>1){
//					hg.setVisibility(View.INVISIBLE);
//				}
				
				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			//
			holder.logoImg.setBackgroundDrawable(new BitmapDrawable(readBitMap(MerchantRankListUI.this, R.drawable.tmp_merchant)));
			if(_list!=null&&_list.size()>0&&position==0){
			holder.hg.setBackgroundDrawable(new BitmapDrawable(readBitMap(MerchantRankListUI.this, R.drawable.hg)));
			}

			final MerchantPO m = (MerchantPO) _list.get(position);

			holder.titleTv.setText(m.getName());
			TextPaint paint = holder.titleTv.getPaint(); 
			paint.setFakeBoldText(true);
		 
			
			String url = m.getLogo();
			
			

			String desc="";
			if(desc!=null&&desc.length()>0){
			   holder.hotTv.setText("");
			}

			if (url != null && url.length() > 10) {
				String[] urlList = url.split(";");
				if (urlList != null && urlList.length > 0) {
					String url0 = urlList[0];
					if(url0!=null&&url0.length()>10){
					holder.logoImg.setTag(url0);
				
					
					 ImageLoader.getInstance().displayImage(url0,holder.logoImg);
					}
				}
			}else{
				holder.logoImg.setBackgroundResource(R.drawable.icon);
			}
			
			int star=m.getStar();
			if(star>0){
			   //holder.starTv.setText(merchantName);
			}
			String add_time=m.getAdd_time();
			if(add_time!=null){
				holder.distanceTv.setText("0.8公里");
			}
			int j=position+1;
			holder.paiming.setText(""+j);
			
			final Long  pk=m.getMerchant_id();
		      convertView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						intent.setClass(MerchantRankListUI.this, NearByMerchantDetailUI.class);
						 
						
						
						
						
					 
						 
						intent.putExtra("pk",pk);
						
						Log.e("主键", "pk="+pk);
						startActivity(intent);
						
					}
				});

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
				list.clear();

				Map resultMap = MerchantRankDao.pageList(currentPage);
				boolean b=resultMap==null;
				Log.e("BUG", b+"");
				
				List<MerchantPO> tmp=(List<MerchantPO>)resultMap.get("list");
				if (tmp != null && tmp.size() > 0) {
					list.addAll(tmp);
				}
				Integer allCnt=(Integer)resultMap.get("allCnt");

				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.what = WHAT_DID_LOAD_DATA;
				if (tmp == null || tmp.size() == 0||allCnt==list.size()) {
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

				Map resultMap =MerchantRankDao.pageList(currentPage);
				
				List<MerchantPO> tmp=(List<MerchantPO>)resultMap.get("list");
				Integer allCnt=(Integer)resultMap.get("allCnt");
				list.addAll(tmp);

			
				Message msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
				msg.obj = "After more " + System.currentTimeMillis();
				if (tmp == null || tmp.size() == 0||allCnt==list.size()) {
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
	TextView district_btn;
	PPCPopMenu baoxuan_popMenu;
	List<Area> areaList;
	private Handler loadAreaHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				district_btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						View contentView = getLayoutInflater().inflate(
								R.layout.merchant_rank_list, null, true);
//						baoxuan_popMenu = new PPCPopMenu(PostList.this);
//						baoxuan_popMenu.addItems(areaList);
//						baoxuan_popMenu
//								.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
//
//									@Override
//									public void onItemClick(
//											AdapterView<?> arg0, View arg1,
//											int pos, long arg3) {
//										Log.e("当前位置", "pos=" + pos);
//										Area po=areaList.get(pos);
//										
//										currentPage=0;
//										district_btn.setText("");
//										district_btn.setText(areaName+" ▼");
//										baoxuan_popMenu.dismiss();
//									
//										list.clear();
//										loadData();
//
//									}
//
//								});
//						baoxuan_popMenu.showAsDropDown(contentView);
					}
				});
			}

		}
	};

	TextView  backBtn;
	TextView todayBtn;
	TextView weekBtn;
	TextView monthBtn;
	int  showType=1;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.merchant_rank_list);
		
		TextView  tvHeaderTitle=(TextView)this.findViewById(R.id.tvHeaderTitle);
		if(tvHeaderTitle!=null){
			tvHeaderTitle.setText("夜店排名");
		}

		list = new ArrayList<MerchantPO>();
		
	
		SharedPreferences preferences = getSharedPreferences("userInfo",
				Activity.MODE_PRIVATE);
		uid = preferences.getLong("uid", 0);

		

//		district_btn = (TextView) this.findViewById(R.id.district_btn);
//		new Thread() {
//			public void run() {
////				AreaService dao = new AreaService();
////				areaList = dao.listArea("1");
////				loadAreaHandler.sendEmptyMessage(1);
//
//			}
//		}.start();

		/*
		 * 1.使用PullDownView 2.设置OnPullDownListener 3.从mPullDownView里面获取ListView
		 */
		
		initView();
	

	 

		loadData();
		
		 
		////////////////////////////////////////////////
		/**PopupWindow的界面*/
		View contentView = getLayoutInflater()
				.inflate(R.layout.popwindow, null);
		
		contentView.findViewById(R.id.submit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();//关闭
				}
				
				
			}
		});
		
		
		popupWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);// 取得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		/**设置PopupWindow弹出和退出时候的动画效果*/
		//popupWindow.setAnimationStyle(R.style.animation);
		
		parent = this.findViewById(R.id.pull_down_view);
		
		TextView selectBtn=(TextView)this.findViewById(R.id.selectBtn);
		selectBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				popupWindow.showAtLocation(parent, Gravity.TOP, 0, 160);
				

			 
				
				
				
				
			}
		});
		
		
		ImageView backBtn=(ImageView)this.findViewById(R.id.backBtn);
		if(backBtn!=null){
			backBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					MerchantRankListUI.this.finish();
					
				}
			});
			
		}
		////////////////////
		
		this.todayBtn = (TextView) this.findViewById(R.id.todayBtn);

		this.weekBtn = (TextView) this.findViewById(R.id.weekBtn);
		this.monthBtn = (TextView) this.findViewById(R.id.monthBtn);
		
		todayBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
		weekBtn.setTextColor(Color.parseColor("#ff404040"));
		monthBtn.setTextColor(Color.parseColor("#ff404040"));

		todayBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
			
				todayBtn.setBackgroundResource(R.drawable.jb_focus);
				
				

				weekBtn.setBackgroundResource(R.drawable.ktv);
			
				
				monthBtn.setBackgroundResource(R.drawable.yzh);
				
				todayBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
				weekBtn.setTextColor(Color.parseColor("#ff404040"));
				monthBtn.setTextColor(Color.parseColor("#ff404040"));
				
				showType=1;
				
				loadData();

			}
		});
		//
		weekBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
                todayBtn.setBackgroundResource(R.drawable.jb);
				
				

				weekBtn.setBackgroundResource(R.drawable.ktv_focus);
			
				
				monthBtn.setBackgroundResource(R.drawable.yzh);
				
				todayBtn.setTextColor(Color.parseColor("#ff404040"));
				weekBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
				monthBtn.setTextColor(Color.parseColor("#ff404040"));
				
				showType=2;
				
				loadData();

			}
		});
		//
		
		
		
		
		monthBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				  todayBtn.setBackgroundResource(R.drawable.jb);
					
					

					weekBtn.setBackgroundResource(R.drawable.ktv);
				
					
					monthBtn.setBackgroundResource(R.drawable.yzh_focus);
					
					todayBtn.setTextColor(Color.parseColor("#ff404040"));
					weekBtn.setTextColor(Color.parseColor("#ff404040"));
					monthBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
					
					showType=3;
				
				loadData();

			}
		});
	 
		
		
		
		
		

	}
	private void initView() {
		 

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
		 
	}
	private final class ItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();//关闭
			}
		}
	}
	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	int id;

}



