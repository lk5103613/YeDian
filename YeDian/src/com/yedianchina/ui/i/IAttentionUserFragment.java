package com.yedianchina.ui.i;


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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.control.XListView;
import com.yedianchina.dao.AttentionDao;
import com.yedianchina.dao.CommentService;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.NearByMerchantDetailUI;

import com.yedianchina.ui.R;
import com.yedianchina.ui.group.GroupDetailUI;
import com.yedianchina.ui.group.GroupListUI;



//我关注的夜店
public class IAttentionUserFragment extends Fragment implements XListView.IXListViewListener{
	static Long mPK;
	static Context mCtx;

	public static  IAttentionUserFragment initFm(Context ctx,Long pk){
		 
		
		mPK=pk;
		mCtx=ctx;
		return new IAttentionUserFragment();
		
	}
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        System.out.println("AAAAAAAAAA____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("AAAAAAAAAA____onCreate");
       
    }

    
    public XListView mListView;
	private CustomAdapter mAdapter;
	int currentPage;
	private static final int WHAT_DID_LOAD_DATA = 0;
	private static final int WHAT_DID_REFRESH = 2;
	private static final int WHAT_DID_MORE = 1;
	ArrayList<HashMap<String, Object>> list;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //System.out.println("AAAAAAAAAA____onCreateView"+po.getTitle());
    	
        
        View view= inflater.inflate(R.layout.nearby_user_dynamic, container, false);
    	list = new ArrayList<HashMap<String, Object>>();
        
        /*
		 * 1.使用PullDownView 2.设置OnPullDownListener 3.从mPullDownView里面获取ListView
		 */
	 
		/////////
//		mPullDownView = (PullDownView) view.findViewById(R.id.pull_down_view);
//		mPullDownView.setOnPullDownListener(this);
//		mListView = mPullDownView.getListView();
//
//		mListView.setOnItemClickListener(this);
//		mAdapter = new CustomAdapter(mCtx, list);
//		mListView.setAdapter(mAdapter);
//		mListView.setDivider(getResources().getDrawable(R.drawable.aoxian));
//
//		mPullDownView.enableAutoFetchMore(true, 1);
    	
    	
    	mListView = (XListView) view.findViewById(R.id.list_view);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(getTime());
		//mListView.setOnItemClickListener(this);
		mAdapter = new CustomAdapter(mCtx, list);
	 
		mListView.setAdapter(mAdapter);
		mListView.setDivider(getResources().getDrawable(R.drawable.aoxian));

	 

		loadData();
		
		return view;
    }
    
	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("AAAAAAAAAA____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("AAAAAAAAAA____onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("AAAAAAAAAA____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("AAAAAAAAAA____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("AAAAAAAAAA____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("AAAAAAAAAA____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("AAAAAAAAAA____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("AAAAAAAAAA____onDetach");
    }
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
	
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
						
							
							 ImageLoader.getInstance().displayImage(url,holder.logoImg );
						}
					 
				} 
				
				
			}
			
			  final Long  pk=(Long)m.get("dynamic_id");
			 
		      convertView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
				 
						Intent intent = new Intent();
						intent.setClass(mCtx, NearByMerchantDetailUI.class);
						intent.putExtra("pk", pk);
						mCtx.startActivity(intent);
						
					}
				});
			

			return convertView;

		}
	}
	int allCnt;

	private void loadData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				CommentService dao = new CommentService();
				currentPage++;
				list.clear();
				
				
				
			 
				 
				
				

				Map resultMap = AttentionDao.pageList(currentPage, mPK);

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
	

	
}
