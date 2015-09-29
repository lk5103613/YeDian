package com.yedianchina.ui.nearby;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.Toast;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;

import com.easemob.chat.EMConversation;

import com.easemob.chat.EMMessage;
import com.easemob.chat.activity.MainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.control.XListView;
import com.yedianchina.dao.AreaService;
import com.yedianchina.dao.UserDao;
import com.yedianchina.po.Area;
import com.yedianchina.po.SexPO;
import com.yedianchina.po.UserPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.tools.JobsPopMenu;
import com.yedianchina.tools.PPCPopMenu;
import com.yedianchina.tools.SexPopMenu;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.LoginUI;
import com.yedianchina.ui.MoreUI;
 
import com.yedianchina.ui.YeDianChinaApplication;
 
import com.yedianchina.ui.R;
import com.yedianchina.ui.group.GroupListUI;
import com.yedianchina.ui.i.I;
import com.yedianchina.ui.i.IMingpianUI;
import com.yedianchina.ui.index.IndexUI;
 
 

//附近模块
public class NearbyModule extends CommonActivity implements XListView.IXListViewListener{
	
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

	int STEP = 0;
	int areaId;

	int gender = -1;

	int sortBy = 0;

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
				convertView = mInflater.inflate(R.layout.nearby_module_cell,
						null);
				holder = new ViewHolder();
				holder.titleTv = (TextView) convertView
						.findViewById(R.id.titleTv);

				holder.merchantNameTv = (TextView) convertView
						.findViewById(R.id.merchantNameTv);

				holder.avatarImg = (ImageView) convertView
						.findViewById(R.id.avatarImg);
				holder.addrTv = (TextView) convertView
						.findViewById(R.id.addrTv);
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
			
			TextPaint paint = holder.titleTv.getPaint(); 
			paint.setFakeBoldText(true);

			String juli = m.getDistance();
			if (juli == null || juli.length() == 0) {
				juli = "";
			}

			String last_login = m.getLast_login();
			if (last_login != null && last_login.length() >= 8) {

				holder.distanceAndTimeTv.setText(juli + "公里");
			} else {
				holder.distanceAndTimeTv.setText(juli + "公里");
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

						ImageLoader.getInstance().displayImage(url0,
								holder.avatarImg);

					}
				}
			} else {
				holder.avatarImg.setBackgroundResource(R.drawable.icon);
			}
			
			 final Long  pk=m.getUid();
			 final String distance = m.getDistance();
		      convertView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {

					  	intent.setClass(NearbyModule.this, NearbyUserUI.class);

						 
							 
						 
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

				currentPage = 0;
				list.clear();

				SharedPreferences locInfo = getSharedPreferences("loc",
						Activity.MODE_PRIVATE);
				longitude = locInfo.getString("longitude", "");
				latitude = locInfo.getString("latitude", "");

				Log.e("", "longitude-----" + longitude);
				Map<String, Object> resultMap = UserDao.pageList(currentPage,
						longitude, latitude, gender, sortBy);

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
				currentPage = 0;
				list.clear();

				SharedPreferences locInfo = getSharedPreferences("loc",
						Activity.MODE_PRIVATE);
				longitude = locInfo.getString("longitude", "");
				latitude = locInfo.getString("latitude", "");

				Log.e("", "longitude-----" + longitude);
				Map<String, Object> resultMap = UserDao.pageList(currentPage,
						longitude, latitude, gender, sortBy);

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

	HashMap params = new HashMap();

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

				Map<String, Object> resultMap = UserDao.pageList(currentPage,
						longitude, latitude, gender, sortBy);

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
				if (textView == quanbu) {
					gender=-1;
				}
				if (textView == nvsheng) {
					gender=2;
				}
				if (textView == nansheng) {
					gender=1;

				}

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
		setContentView(R.layout.nearby_module);

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

			}
		});

		groupBtn = (TextView) this.findViewById(R.id.groupBtn);
		groupBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				groupBtn.setTextColor(Color.parseColor("#FFFF0000"));// Red

				personBtn.setTextColor(Color.parseColor("#FF000000"));

				Intent intent = new Intent();
				intent.setClass(NearbyModule.this, GroupListUI.class);

				NearbyModule.this.startActivity(intent);
				NearbyModule.this.finish();

			}
		});

		TextView mapBtn = (TextView) this.findViewById(R.id.backBtn);
		if (mapBtn != null) {
			mapBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					intent.setClass(NearbyModule.this, NearbyUserMap.class);
					startActivity(intent);

				}
			});

		}

		list = new ArrayList<UserPO>();

		SharedPreferences preferences = getSharedPreferences("userInfo",
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

		loadData();
		/*
		 * 1.使用PullDownView 2.设置OnPullDownListener 3.从mPullDownView里面获取ListView
		 */
	 
		
		////
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
						
						
						
						currentPage = 0;
						list.clear();

						SharedPreferences locInfo = getSharedPreferences(
								"loc", Activity.MODE_PRIVATE);
						longitude = locInfo.getString("longitude", "");
						latitude = locInfo.getString("latitude", "");

						Log.e("", "longitude-----" + longitude);
						Map<String, Object> resultMap = UserDao.pageList(
								currentPage, longitude, latitude, gender,
								sortBy);

						List<UserPO> tmp = (List<UserPO>) resultMap
								.get("list");
						if (tmp != null && tmp.size() > 0) {
							list.addAll(tmp);
						}
						Integer allCnt = (Integer) resultMap.get("allCnt");

						Message msg = mUIHandler
								.obtainMessage(WHAT_DID_LOAD_DATA);
						msg.what = WHAT_DID_LOAD_DATA;
						if (tmp == null || tmp.size() == 0
								|| allCnt == list.size()) {
							msg.what = 7;
						}
						msg.sendToTarget();
						
						if (popupWindow.isShowing()) {
							popupWindow.dismiss();// 关闭
							//

							

							//
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

		final LinearLayout parent = (LinearLayout) this
				.findViewById(R.id.title);

		TextView selectBtn = (TextView) this.findViewById(R.id.publishJobBtn);
		selectBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// popupWindow.showAtLocation(parent, Gravity.TOP, 0, 160);
				//
				int[] location = new int[2];
				v.getLocationOnScreen(location);
				int h = parent.getHeight();
				popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY,
						location[0], location[1] - popupWindow.getHeight() + h);

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
		
		
		
		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		// 注册一个ack回执消息的BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
				.getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		// setContactListener监听联系人的变化等
		//EMContactManager.getInstance().setContactListener(new MyContactListener());
		// 注册一个监听连接状态的listener
		//EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		// 注册群聊相关的listener
		//EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();

	}
	private NewMessageBroadcastReceiver msgReceiver;
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			// EMMessage message =
			// EMChatManager.getInstance().getMessage(msgId);

			// 刷新bottom bar消息未读数
			updateUnreadLabel();
//			if (currentTabIndex == 0) {
//				// 当前页面如果为聊天历史页面，刷新此页面
//				if (chatHistoryFragment != null) {
//					chatHistoryFragment.refresh();
//				}
//			}
			// 注销广播，否则在ChatActivity中会收到这个广播
			abortBroadcast();
		}
	}
	
	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
//		int count = getUnreadMsgCountTotal();
//		if (count > 0) {
//			unreadLabel.setText(String.valueOf(count));
//			unreadLabel.setVisibility(View.VISIBLE);
//		} else {
//			unreadLabel.setVisibility(View.INVISIBLE);
//		}
	}
	
	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance().getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					msg.isAcked = true;
				}
			}
			abortBroadcast();
		}
	};

	int id;
	
	 protected void onDestroy() {  
		    
	        unregisterReceiver(ackMessageReceiver);  
	        unregisterReceiver(msgReceiver);  
	        
	        super.onDestroy();
	    };  

	
	
	private void parentControl() {
		// 获取密度
		this.getDensity();

	 
		imageViewIndex = (ImageView) findViewById(R.id.menu_home_img);
		imageViewIndex.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 
				startActivity(new Intent(getApplicationContext(),
						IndexUI.class));
				NearbyModule.this.finish();
				
			}
		});
	 

		imageViewType = (ImageView) findViewById(R.id.menu_brand_img);
		imageViewType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 
			}
		});

		ImageView menu_my_letao_img = (ImageView) findViewById(R.id.menu_my_letao_img);
		menu_my_letao_img.setOnClickListener(new View.OnClickListener() {
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
					NearbyModule.this.finish();
				}
				
				

			
				
			}
		});
		

		ImageView imageViewMyLetao = (ImageView) findViewById(R.id.menu_shopping_cart_img);
		imageViewMyLetao.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				startActivity(new Intent(getApplicationContext(), I.class));
				NearbyModule.this.finish();
				
			}
		});

		imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
		imageViewMore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			 
			 
				startActivity(new Intent(getApplicationContext(), MoreUI.class));
				NearbyModule.this.finish();
			}
		});

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
