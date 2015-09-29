package com.yedianchina.ui.i;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.my.Files;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.control.XListView;
import com.yedianchina.dao.AttentionDao;
import com.yedianchina.dao.CommentService;
import com.yedianchina.dao.DynamicDao;
import com.yedianchina.dao.UserDao;
import com.yedianchina.po.UserPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.LoginUI;

import com.yedianchina.ui.R;
import com.yedianchina.ui.YeDianChinaApplication;

public class IDynamicUI extends Activity implements
XListView.IXListViewListener{
	private ProgressDialog progressDialog;
	TextView mingpianBtn;
	
	TextView nearbyClassScoreID;

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
	private UserPO userPO;
	private long userId;
	String distance;// 距离 从列表界面传递过来 因为计算太损耗性能 只要计算一次即可

	public void updateCurrendData() {
		userPO = UserDao.loadUserInfo(userId);

	}

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				Log.e("userPO", userPO.getNickname());
				setNearbyUserDetailData();

			}
		};
	};
	TextView titleTxtView;
	ImageView userAvatar;
	TextView sexIV;
	TextView distanceTv;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == 8) {
			initView();
		}
	}

	TextView astroTv;
	
	ImageView nearby_sendmsg;
	
	TextView nearbyAttentionID;
	
	Handler gzHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;

			if (what == 1) {
				nearby_gz.setImageResource(R.drawable.nearby_gz_focus);
				gzHandler.removeMessages(1);
				Toast toast = Toast.makeText(getApplicationContext(),
						"关注成功", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			} if (what == 7) {

				Toast toast = Toast.makeText(getApplicationContext(),
						"您已经关注该用户，请勿重复关注", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

		};
	};
	ImageView  nearby_gz;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.nearby_user_dynamic);
		Bundle bundle = this.getIntent().getExtras();
		final Long pk = bundle.getLong("pk");
        this.userId = pk;
        
        
        TextView nearbyClassScoreID=(TextView)this.findViewById(R.id.nearbyClassScoreID);
        if(nearbyClassScoreID!=null){
        	nearbyClassScoreID.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(IDynamicUI.this, IDengji.class);
					 
					IDynamicUI.this.startActivity(intent);
					
				}
			});
        }
        
		
		//TA关注
		nearbyAttentionID=(TextView)this.findViewById(R.id.nearbyAttentionID);
		if(nearbyAttentionID!=null){
			nearbyAttentionID.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(IDynamicUI.this, IAttentionUI.class);
					intent.putExtra("pk", userId);
					IDynamicUI.this.startActivity(intent);
					
				}
			});
		}
		//底部 关注
		nearby_gz=(ImageView)this.findViewById(R.id.nearby_gz);
		if(nearby_gz!=null){
			nearby_gz.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SharedPreferences preferences = getSharedPreferences(CONSTANTS.YEDIANCHINA_USER_INFO,
							Activity.MODE_PRIVATE);
					final Long  uid=preferences.getLong("uid", 0);
					if(uid==0){
						intent.setClass(IDynamicUI.this, LoginUI.class);
						
						startActivity(intent);
						
					}else{
						new Thread() {
							public void run() {
								int code=AttentionDao.nearbyBottomGz(uid, userId);
								gzHandler.sendEmptyMessage(code);

							}
						}.start();
					}
					
				}
			});
		}
		
		
		
		
        
        //底部菜单-发消息
        nearby_sendmsg=(ImageView)this.findViewById(R.id.nearby_sendmsg);
        nearby_sendmsg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SharedPreferences preferences = getSharedPreferences(CONSTANTS.YEDIANCHINA_USER_INFO,
						Activity.MODE_PRIVATE);
				
				intent.setClass(IDynamicUI.this, ISendMsgUI.class);
				
				startActivity(intent);
				
			}
		});
        
        //右上角名片
		mingpianBtn = (TextView) this.findViewById(R.id.mingpianBtn);// 名片
		if (mingpianBtn != null) {
			mingpianBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(IDynamicUI.this, IMingpianUI.class);
					 
					intent.putExtra("pk", String.valueOf(pk));
					IDynamicUI.this.startActivity(intent);

				}
			});
		}
		list = new ArrayList<HashMap<String, Object>>();
		astroTv = (TextView) this.findViewById(R.id.astroTv);

		this.titleTxtView = (TextView) findViewById(R.id.tvHeaderTitle);

		
		Files.mkdir(this);
		Bitmap image = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo200);
		imagesCache.put("background_non_load", image); // 设置缓存中默认的图片
		initView();

		//返回按钮
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					IDynamicUI.this.finish();

				}
			});
		}
	
		
		

		userAvatar = (ImageView) this.findViewById(R.id.avatarTv);

		distanceTv = (TextView) this.findViewById(R.id.distanceTv);
		sexIV = (TextView) this.findViewById(R.id.sexIV);

		new Thread() {
			public void run() {

				updateCurrendData();

				loadingHandler.sendEmptyMessage(1);

			}
		}.start();

		TextView star = (TextView) this.findViewById(R.id.star);
		if (star != null) {
			star.setBackgroundResource(R.drawable.star1);
		}

		YeDianChinaApplication.getInstance().addActivity(this);
		// //////////////
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
		TextView tvHeaderTitle = (TextView) this
				.findViewById(R.id.NavigateTitle);
		if (tvHeaderTitle != null) {
			tvHeaderTitle.setText("夜店详情");
		}

//		TextView regBtn = (TextView) this.findViewById(R.id.regBtn);
//		if (regBtn != null) {
//			regBtn.setVisibility(View.INVISIBLE);
//		}

	}
	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	int allCnt;

	private void loadData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				CommentService dao = new CommentService();
				currentPage++;
				list.clear();

				Map resultMap = DynamicDao.findUserDynamicList(userId,
						currentPage);

				ArrayList<HashMap<String, Object>> tmp = (ArrayList<HashMap<String, Object>>) resultMap
						.get("list");
				allCnt = (Integer) resultMap.get("allCnt");
				if (tmp != null && tmp.size() > 0) {
					list.addAll(tmp);
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
 
	private XListView mListView;
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
			ImageView logoImg;
			TextView content;
			TextView drivingLicenceTv;
			TextView statusTv;
			TextView msgCntTv;
			TextView zanCntTv;
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
				convertView = mInflater.inflate(R.layout.nearby_dynamic_cell, null);
				holder = new ViewHolder();
				 
				holder.mp = (TextView) convertView.findViewById(R.id.mp);

				holder.addTime = (TextView) convertView
						.findViewById(R.id.addTime);
				holder.content = (TextView) convertView
						.findViewById(R.id.content);
				holder.msgCntTv = (TextView) convertView
						.findViewById(R.id.msgCntTv);
				holder.logoImg = (ImageView) convertView
						.findViewById(R.id.logoImg);
				holder.zanCntTv = (TextView) convertView
						.findViewById(R.id.zanCntTv);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			HashMap<String, Object> m = (HashMap<String, Object>) _list
					.get(position);

			String content = (String) m.get("content");
			Integer msg_cnt = (Integer) m.get("msg_cnt");
			holder.msgCntTv.setText(""+msg_cnt);
			String addTime = (String) m.get("addTime");
			holder.addTime.setText(addTime);

			holder.content.setText(content);
			
			int zan_cnt=(Integer)m.get("zan_cnt");
			holder.zanCntTv.setText(""+zan_cnt);
			String avatar=(String) m.get("avatar");
			ImageView imageView = holder.logoImg;
			if(avatar!=null&&avatar.length()>5){
				String url=CONSTANTS.HOST+avatar;
				if (url != null && url.length() > 10) {
					 
						 
						if (url != null && url.length() > 10) {
							holder.logoImg.setTag(url);
						
							
							 ImageLoader.getInstance().displayImage(url,holder.logoImg);
						}
					 
				} 
				
				
			}
			final Long dynamic_id=(Long)m.get("dynamic_id");
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {

					intent.setClass(IDynamicUI.this, IDynamicDetailUI.class);
					 
					
					
				
					intent.putExtra("pk",dynamic_id);
					
					Log.e("主键", "pk="+dynamic_id);
					startActivity(intent);

					
				}
			});
			
			

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
		SharedPreferences preferences = getSharedPreferences(CONSTANTS.YEDIANCHINA_USER_INFO,
				Activity.MODE_PRIVATE);
		String _mp = preferences.getString("mp", "");
		if (_mp != null && _mp.length() == 11) {
			this.mp = _mp;
		}
	}

	String inputStr;

	/**
	 * 加载(绑定)所有控件
	 * 
	 * @Author TanRuixiang
	 */
	private void setNearbyUserDetailData() {
		TextView tvHeaderTitle = (TextView) findViewById(R.id.NavigateTitle);
		if (tvHeaderTitle != null) {
			String nickName = userPO.getNickname();
			if (nickName != null && nickName.length() > 0) {
				tvHeaderTitle.setText(nickName);
			}
		}

		String logo = userPO.getAvatar();
		if (logo != null && logo.length() > 5) {

			Log.e("头像", logo);
			userAvatar.setTag(logo);

			
			
			 ImageLoader.getInstance().displayImage(logo,userAvatar);
		}
		int gender = userPO.getGender();
		if (gender == 1) {
			sexIV.setBackgroundResource(R.drawable.nearby_man);
		} else if (gender == 2) {
			sexIV.setBackgroundResource(R.drawable.nearby_girl);
		} else {
			sexIV.setBackgroundResource(R.drawable.sex);
		}

		int age = userPO.getAge();
		if (age > 0) {
			sexIV.setText(age + "");
		}
		String astro = userPO.getAstro();
		astroTv.setText(astro);

	}

	public ImageLoader imageLoader;
	public Intent intent = new Intent();
	

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

				Map resultMap = DynamicDao.findUserDynamicList(userId,
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

