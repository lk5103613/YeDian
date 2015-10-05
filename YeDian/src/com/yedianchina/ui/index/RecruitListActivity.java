package com.yedianchina.ui.index;

import java.lang.reflect.Type;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.like.adapter.RecruitListAdapter;
import com.like.entity.ListResult;
import com.like.entity.RecruitInfo;
import com.yedianchina.control.XListView;
import com.yedianchina.control.XListView.IXListViewListener;
import com.yedianchina.ui.R;
import com.yedianchina.ui.activity.EditPerInfoActivity;

public class RecruitListActivity extends BaseActivity {
	
	private XListView mList;
	private int mCurrentPage = 0;
	private String mCurrentType = "1";
	private String mKey = "";
	private String mCatName = "";
	private RecruitListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recruit_list);
		
		mList = (XListView) findViewById(R.id.listview);
		mList.setPullRefreshEnable(true);
		mList.setPullLoadEnable(true);
		mList.setAutoLoadEnable(true);
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				RecruitInfo info = (RecruitInfo) mAdapter.getItem(position);
				Intent intent = new Intent(mContext, EditPerInfoActivity.class);
				String json = new Gson().toJson(info);
				intent.putExtra("info", json);
				startActivity(intent);
			}
		});
		mList.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				mCurrentPage = 0;
				mAdapter.clear();
				updateList();
			}
			@Override
			public void onLoadMore() {
				mCurrentPage++;
				updateList();
			}
		});
		
		updateList();
	}
	
	private void updateList() {
		mDataFetcher.fetchRecruitList(mCurrentPage+"", mCurrentType, mCatName, mKey, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Type type = new TypeToken<ListResult<RecruitInfo>>(){}.getType();
				ListResult<RecruitInfo> result = new Gson().fromJson(response, type);
				if(mAdapter == null) {
					mAdapter = new RecruitListAdapter(mContext, result.list);
					mList.setAdapter(mAdapter);
				} else {
					mAdapter.update(result.list);
				}
				mList.stopLoadMore();
				mList.stopRefresh();
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
			
		});
	}
	
}
