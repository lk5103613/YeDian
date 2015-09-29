package com.yedianchina.ui.nearby;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.control.XListView;
import com.yedianchina.dao.DynamicDao;
import com.yedianchina.dao.NearbyDynamicCommentDao;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.LoginUI;
 
import com.yedianchina.ui.R;

public class NearbyDynamicDetailUI extends Activity implements
XListView.IXListViewListener{
	Map map;

	public void updateCurrendData() {
		map = DynamicDao.loadUserDynamicDetail(pk);

	}

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {

				setData();

			}
		};
	};

	public void setData() {

		String nickname = (String) map.get("nickname");
		nicknameTv.setText(nickname);

		String logo = (String) map.get("avatar");
		if (logo != null && logo.length() > 5) {
			logo = CONSTANTS.HOST + logo;
			Log.e("头像", logo);
			avatarTv.setTag(logo);

			// imageLoader = new ImageLoader(this.getApplicationContext());
			// imageLoader
			// .DisplayImage(logo, NearbyDynamicDetailUI.this, avatarTv);
			imageLoader.displayImage(logo, avatarTv);
		}
		String addTime = (String) map.get("addTime");
		if (addTime != null) {
			addTimeTv.setText(addTime);
		}

		String content = (String) map.get("content");
		if (content != null && content.length() > 0) {
			contentTv.setText(content);
		}
		int zan_cnt = (Integer) map.get("zan_cnt");
		zan_cntTv.setText("" + zan_cnt);

		Integer msg_cnt = (Integer) map.get("msg_cnt");
		msg_cntTv.setText("" + msg_cnt);
	}

	Long pk = null;
	TextView nicknameTv;
	ImageView avatarTv;
	TextView addTimeTv;
	TextView contentTv;
	TextView zan_cntTv;
	TextView msg_cntTv;

 
	private XListView mListView;
	ArrayList<HashMap<String, Object>> list;
	private CustomAdapter mAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.nearby_dynamic_detail);

		list = new ArrayList<HashMap<String, Object>>();

		nicknameTv = (TextView) this.findViewById(R.id.nicknameTv);
		avatarTv = (ImageView) this.findViewById(R.id.avatarTv);
		addTimeTv = (TextView) this.findViewById(R.id.addTimeTv);
		contentTv = (TextView) this.findViewById(R.id.contentTv);

		zan_cntTv = (TextView) this.findViewById(R.id.zan_cntTv);
		msg_cntTv = (TextView) this.findViewById(R.id.msg_cntTv);

		pk = this.getIntent().getExtras().getLong("pk");
		Log.e("主见", "pk==" + pk);
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					NearbyDynamicDetailUI.this.finish();

				}
			});
		}
		//
		
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  

		new Thread() {
			public void run() {

				updateCurrendData();

				loadingHandler.sendEmptyMessage(1);

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

		TextView tvHeaderTitle = (TextView) this
				.findViewById(R.id.tvHeaderTitle);
		if (tvHeaderTitle != null) {
			tvHeaderTitle.setText("动态详情");
		}

		TextView favBtn = (TextView) this.findViewById(R.id.favBtn);
		if (favBtn != null) {
			favBtn.setVisibility(View.INVISIBLE);
		}
		contentEt = (EditText) this.findViewById(R.id.contentEt);

		TextView publish_comment = (TextView) this
				.findViewById(R.id.publish_comment);
		if (publish_comment != null) {
			publish_comment.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SharedPreferences preferences = getSharedPreferences(
							CONSTANTS.YEDIANCHINA_USER_INFO,
							Activity.MODE_PRIVATE);
					Long pub_comment_uid = preferences.getLong("uid", 0);
					Log.e("", "-----发布评论0703pub_comment_uid=" + pub_comment_uid);

					if (pub_comment_uid == null || pub_comment_uid == 0) {
						Intent intent = new Intent();
						intent.setClass(NearbyDynamicDetailUI.this,
								LoginUI.class);

						NearbyDynamicDetailUI.this.startActivity(intent);

					} else {
						Log.e("", "-----发布评论0703");
						
						String content=contentEt.getText()
								.toString();
                        
						
						
						if(content!=null&&content.length()>0){
							boolean  b=NearbyDynamicCommentDao.saveComment(content, pk, pub_comment_uid);
							contentEt.setText("");
						}
						imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS); //关闭键盘
					}

				}
			});
		}

	}
	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}
	
	InputMethodManager imm;
	EditText contentEt;

	int allCnt;
	int currentPage;

	private void loadData() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				currentPage = 0;
				list.clear();

				Map resultMap = NearbyDynamicCommentDao.findDynamicCommentList(
						pk, currentPage);

				ArrayList<HashMap<String, Object>> tmp = (ArrayList<HashMap<String, Object>>) resultMap
						.get("list");
				allCnt = 0;
				if (resultMap != null) {
					allCnt = (Integer) resultMap.get("allCnt");
				}
				if (tmp != null && tmp.size() > 0) {
					list.addAll(tmp);
				}

				Message msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
				msg.what = WHAT_DID_LOAD_DATA;
				msg.sendToTarget();

			}
		}).start();
	}

	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 2;
	private static final int WHAT_DID_MORE = 1;
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
	ImageLoader imageLoader = ImageLoader.getInstance();

	public class CustomAdapter extends BaseAdapter {

		private ArrayList<HashMap<String, Object>> _list;
		private LayoutInflater mInflater;

		public class ViewHolder {
			TextView addTime;
			TextView nicknameTv;
			ImageView avatarImg;
			TextView contentTv;

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
			_list = list;
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.nearby_dynamic_comment_cell, null);
				holder = new ViewHolder();

				holder.nicknameTv = (TextView) convertView
						.findViewById(R.id.mp);

				holder.addTime = (TextView) convertView
						.findViewById(R.id.addTimeTv);
				holder.contentTv = (TextView) convertView
						.findViewById(R.id.contentTv);

				holder.avatarImg = (ImageView) convertView
						.findViewById(R.id.logoImg);
				holder.nicknameTv = (TextView) convertView
						.findViewById(R.id.nicknameTv);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			HashMap<String, Object> m = (HashMap<String, Object>) _list
					.get(position);

			String content = (String) m.get("content");

			String addTime = (String) m.get("addTime");
			holder.addTime.setText(addTime);

			holder.contentTv.setText(content);

			String avatar = (String) m.get("avatar");

			if (avatar != null && avatar.length() > 5) {
				String url = CONSTANTS.HOST + avatar;
				if (url != null && url.length() > 10) {

					if (url != null && url.length() > 10) {
						holder.avatarImg.setTag(url);
						// imageLoader = new ImageLoader(
						// NearbyDynamicDetailUI.this);
						// imageLoader.DisplayImage(url,
						// NearbyDynamicDetailUI.this, holder.avatarImg);

						ImageLoader.getInstance().displayImage(url,
								holder.avatarImg);
					}

				}

			}

			return convertView;

		}
	}

	 

	@Override
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				currentPage = 0;
				list.clear();

				Map resultMap = NearbyDynamicCommentDao.findDynamicCommentList(
						pk, currentPage);

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

	@Override
	public void onLoadMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				currentPage++;

				Map resultMap = NearbyDynamicCommentDao.findDynamicCommentList(
						pk, currentPage);

				ArrayList<HashMap<String, Object>> tmp = (ArrayList<HashMap<String, Object>>) resultMap
						.get("list");
				allCnt = (Integer) resultMap.get("allCnt");
				if (list != null && tmp != null) {
					list.addAll(tmp);
				}

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
