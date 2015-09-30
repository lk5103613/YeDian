package com.yedianchina.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.control.XListView;
import com.yedianchina.dao.AreaService;
import com.yedianchina.dao.RecruitDao;
import com.yedianchina.po.Area;
import com.yedianchina.po.JobsPO;
import com.yedianchina.po.RecruitPO;
import com.yedianchina.po.SexPO;
import com.yedianchina.tools.JobsPopMenu;
import com.yedianchina.tools.PPCPopMenu;
import com.yedianchina.tools.SexPopMenu;
import com.yedianchina.ui.recruit.PublishRecruitUI;
import com.yedianchina.ui.recruit.RecruitDetailUI;

//夜店招聘
public class RecruitListUI extends CommonActivity implements
		XListView.IXListViewListener {
	int recruit_type = 1;

	ImageView nearbyID;
	ImageView cityID;

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
		ImageView logoImg;
		TextView descTv;
		TextView addtimeTv;

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
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.post_cell, null);

				holder.titleTv = (TextView) convertView
						.findViewById(R.id.titleTv);

				holder.merchantNameTv = (TextView) convertView
						.findViewById(R.id.merchantNameTv);

				holder.logoImg = (ImageView) convertView
						.findViewById(R.id.logoImg);
				holder.addrTv = (TextView) convertView
						.findViewById(R.id.addrTv);
				holder.addtimeTv = (TextView) convertView
						.findViewById(R.id.addtimeTv);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.logoImg.setBackgroundResource(R.drawable.unloadimage);

			final RecruitPO m = (RecruitPO) _list.get(position);

			holder.titleTv.setText(m.getTitle());
			// holder.mpTv.setText(m.getLinkNumber());
			String url = m.getLogo();
			Log.e("LOGO", "url==" + url);

			String areaname = m.getAddr();
			if (areaname != null && areaname.length() > 0) {
				holder.addrTv.setText(areaname);
			}
			String desc = "";
			if (desc != null && desc.length() > 0) {
				holder.descTv.setText(desc);
			}

			if (url != null && url.length() > 10) {

				holder.logoImg.setTag(url);

				ImageLoader.getInstance().displayImage(url, holder.logoImg);

			} else {

			}

			String merchantName = m.getMp();
			if (merchantName != null && merchantName.length() > 0) {
				holder.merchantNameTv.setText(merchantName);
			}
			String add_time = m.getAdd_time();
			if (add_time != null) {
				holder.addtimeTv.setText(add_time);
			}
			// //
			final Long pk = m.getId();
			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					intent.setClass(RecruitListUI.this, RecruitDetailUI.class);

					intent.putExtra("pk", pk);

					Log.e("主键", "pk=" + pk);
					startActivity(intent);

				}
			});

			// ////

			return convertView;

		}
	}

	ImageLoader imageLoader;

	List<RecruitPO> list;
	int currentPage;
	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 2;
	private static final int WHAT_DID_MORE = 1;

	public XListView mListView;
	private CustomAdapter mAdapter;

	private void loadData() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				// currentPage++;
				currentPage = 1;
				list.clear();

				Map resultMap = RecruitDao.pageList(currentPage, recruit_type);
				List<RecruitPO> tmp = (List<RecruitPO>) resultMap.get("list");
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
				currentPage = 1;
				list.clear();
				Map resultMap = RecruitDao.pageList(currentPage, recruit_type);
				List<RecruitPO> tmp = (List<RecruitPO>) resultMap.get("list");
				if (tmp != null && tmp.size() > 0) {
					list.addAll(tmp);
				}
				Integer allCnt = (Integer) resultMap.get("allCnt");
				// //////////////////////////////

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
	public void onLoadMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				currentPage++;

				Map resultMap = RecruitDao.pageList(currentPage, recruit_type);

				List<RecruitPO> tmp = (List<RecruitPO>) resultMap.get("list");
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
	TextView district_btn;
	TextView job_btn;
	TextView sex_btn;

	PPCPopMenu baoxuan_popMenu;
	List<Area> areaList;

	List<JobsPO> jobsList;
	List<SexPO> sexList;

	JobsPopMenu jobsPopMenu;
	SexPopMenu sexPopMenu;
	private Handler loadAreaHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {

				// 地区选择
				district_btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						View contentView = getLayoutInflater().inflate(
								R.layout.recruit_list, null, true);
						baoxuan_popMenu = new PPCPopMenu(RecruitListUI.this);
						baoxuan_popMenu.addItems(areaList);
						baoxuan_popMenu
								.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> arg0, View arg1,
											int pos, long arg3) {
										Log.e("当前位置", "pos=" + pos);
										Area po = areaList.get(pos);
										String areaName = po.getAreaName();
										currentPage = 0;
										district_btn.setText("");
										district_btn.setText(areaName + " ▼");
										baoxuan_popMenu.dismiss();

										list.clear();
										loadData();

									}

								});
						baoxuan_popMenu.showAsDropDown(district_btnB);
					}
				});
				//
				job_btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						jobsList = new ArrayList<JobsPO>();
						// 1.
						JobsPO po = new JobsPO();
						po.setName("DJ公主");
						jobsList.add(po);
						
						po = new JobsPO();
						po.setName("DJ少爷");
						jobsList.add(po);

//						po = new JobsPO();
//						po.setName("DANCER");
//						jobsList.add(po);

						po = new JobsPO();
						po.setName("服务员");
						jobsList.add(po);

						po = new JobsPO();
						po.setName("男模");
						jobsList.add(po);

						po = new JobsPO();
						po.setName("女模");
						jobsList.add(po);

						int offset = (int) (screenWidth * 0.33);

						// View contentView = getLayoutInflater().inflate(
						// R.layout.recruit_list, null, true);
						jobsPopMenu = new JobsPopMenu(RecruitListUI.this,
								offset, yOffSet);
						jobsPopMenu.addItems(jobsList);
						jobsPopMenu
								.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> arg0, View arg1,
											int pos, long arg3) {
										Log.e("当前位置", "pos=" + pos);
										JobsPO po = jobsList.get(pos);
										String name = po.getName();
										currentPage = 0;
										job_btn.setText("");

										job_btn.setText(name + " ▼");
										jobsPopMenu.dismiss();

										list.clear();
										loadData();

									}

								});

						// /////////////

						jobsPopMenu.showAsDropDown(parent, v, yOffSet);
						// //

						// ///
					}
				});
				// ///
				sex_btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						sexList = new ArrayList<SexPO>();
						// 1.
						SexPO po = new SexPO();
						po.setSexName("女");
						sexList.add(po);

						po = new SexPO();
						po.setSexName("男");
						sexList.add(po);

						int offset = screenWidth * 2 / 3 - 5;
						View contentView = getLayoutInflater().inflate(
								R.layout.recruit_list, null, true);
						sexPopMenu = new SexPopMenu(RecruitListUI.this, offset);
						sexPopMenu.addItems(sexList);
						sexPopMenu
								.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> arg0, View arg1,
											int pos, long arg3) {
										Log.e("当前位置", "pos=" + pos);
										SexPO po = sexList.get(pos);
										String name = po.getSexName();
										currentPage = 0;
										sex_btn.setText("");
										sex_btn.setText(name + " ▼");
										sexPopMenu.dismiss();

										list.clear();
										loadData();

									}

								});

						sexPopMenu.showAsDropDown(sex_btnB);
					}
				});

			}

		}
	};

	ImageView backBtn;

	int screenWidth;

	TextView jiubaBtn;
	TextView ktvBtn;
	TextView yezonghuiBtn;
	View parent;
	View district_btnB;
	View sex_btnB;

	int yOffSet = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.recruit_list);
		int[] location = new int[2];

		RelativeLayout titleRL = (RelativeLayout) this.findViewById(R.id.title);
		titleRL.getLocationOnScreen(location);
		int y1 = titleRL.getHeight();

		LinearLayout tool_bar1 = (LinearLayout) this
				.findViewById(R.id.tool_bar1);
		tool_bar1.getLocationOnScreen(location);
		int y2 = tool_bar1.getHeight();

		LinearLayout tool_bar = (LinearLayout) this.findViewById(R.id.tool_bar);
		tool_bar.getLocationOnScreen(location);
		int y3 = tool_bar.getHeight();

		// ///////

		yOffSet = y1 + y2 + y3;

		Log.e("yOffSet", "yOffSet=" + yOffSet);

		recruit_type = 1;
		parent = this.findViewById(R.id.job_btnB);
		district_btnB = this.findViewById(R.id.district_btnB);

		sex_btnB = this.findViewById(R.id.sex_btnB);

		jiubaBtn = (TextView) this.findViewById(R.id.jiubaBtn);

		ktvBtn = (TextView) this.findViewById(R.id.ktvBtn);
		yezonghuiBtn = (TextView) this.findViewById(R.id.yezonghuiBtn);
		// ////////////////////////
		jiubaBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
				RecruitListUI.this, R.drawable.jb_focus)));
		ktvBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
				RecruitListUI.this, R.drawable.ktv)));
		yezonghuiBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
				RecruitListUI.this, R.drawable.yzh)));

		// //////////////////////

		jiubaBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
		ktvBtn.setTextColor(Color.parseColor("#ff404040"));
		yezonghuiBtn.setTextColor(Color.parseColor("#ff404040"));

		jiubaBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if(recruit_type == 1)
					return;
				jiubaBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
						RecruitListUI.this, R.drawable.jb_focus)));

				ktvBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
						RecruitListUI.this, R.drawable.ktv)));

				yezonghuiBtn.setBackgroundDrawable(new BitmapDrawable(
						readBitMap(RecruitListUI.this, R.drawable.yzh)));

				jiubaBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
				ktvBtn.setTextColor(Color.parseColor("#ff404040"));
				yezonghuiBtn.setTextColor(Color.parseColor("#ff404040"));

				recruit_type = 1;

				list.clear();
				mAdapter.notifyDataSetChanged();
				loadData();

			}
		});
		//
		ktvBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if(recruit_type == 2)
					return;
				jiubaBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
						RecruitListUI.this, R.drawable.jb)));

				ktvBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
						RecruitListUI.this, R.drawable.ktv_focus)));

				yezonghuiBtn.setBackgroundDrawable(new BitmapDrawable(
						readBitMap(RecruitListUI.this, R.drawable.yzh)));

				ktvBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
				jiubaBtn.setTextColor(Color.parseColor("#ff404040"));
				yezonghuiBtn.setTextColor(Color.parseColor("#ff404040"));

				recruit_type = 2;
				list.clear();
				mAdapter.notifyDataSetChanged();
				loadData();

			}
		});
		//
		yezonghuiBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(recruit_type == 3)
					return;
				//
				jiubaBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
						RecruitListUI.this, R.drawable.jb)));

				ktvBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
						RecruitListUI.this, R.drawable.ktv)));

				yezonghuiBtn.setBackgroundDrawable(new BitmapDrawable(
						readBitMap(RecruitListUI.this, R.drawable.yzh_focus)));

				ktvBtn.setTextColor(Color.parseColor("#ff404040"));
				jiubaBtn.setTextColor(Color.parseColor("#ff404040"));
				yezonghuiBtn.setTextColor(Color.parseColor("#FFFFFFFF"));

				recruit_type = 3;
				list.clear();
				mAdapter.notifyDataSetChanged();
				loadData();

			}
		});
		//

		// screenWidth = tool_bar.getWidth();// 屏幕宽度

		list = new ArrayList<RecruitPO>();

		initView();

		backBtn = (ImageView) this.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				RecruitListUI.this.finish();

			}
		});

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

		loadData();

		// //////////////////////////////////////////////
		TextView publishRecruitBtn = (TextView) this
				.findViewById(R.id.publishRecruitBtn);
		if (publishRecruitBtn != null) {
			publishRecruitBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					Intent mainIntent = new Intent(RecruitListUI.this,
							PublishRecruitUI.class);
					RecruitListUI.this.startActivity(mainIntent);

				}
			});

		}

		IntentFilter filter = new IntentFilter(PublishRecruitUI.action);
		registerReceiver(broadcastReceiver, filter);

	}

	private void initView() {

		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		// mListView.setOnItemClickListener(this);
		mAdapter = new CustomAdapter(this, list);

		mListView.setAdapter(mAdapter);
		mListView.setDivider(getResources().getDrawable(R.drawable.aoxian));

	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String publishrecruit = intent.getExtras().getString(
					"publishrecruit");
			if ("1".equals(publishrecruit)) {
				loadData();
			}
		}
	};

	protected void onDestroy() {

		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}

	int id;

}
