package com.like.network;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.like.likeutils.network.DataFetcherBase;

public class DataFetcher extends DataFetcherBase {
	
	private static DataFetcher mDataFetcher;

	private DataFetcher(Context context) {
		super(context);
		setDebug(true);
	}
	
	
	public static DataFetcher getInstance(Context context) {
		if(mDataFetcher == null) {
			mDataFetcher = new DataFetcher(context);
		}
		return mDataFetcher;
	}
	
	public void fetchRecruitList(String currentPage, String type, Listener<String> listener, ErrorListener errorListener) {
		fetchData(APIS.GET_RECRUIT_LIST, listener, errorListener, currentPage, type);
	}
	
	public void fetchRecruitDetail(String id, Listener<String> listener, ErrorListener errorListener) {
		fetchData(APIS.GET_RECRUIT_DETAIL, listener, errorListener, id);
	}

}
