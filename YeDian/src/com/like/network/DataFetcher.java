package com.like.network;

import java.util.Map;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.like.likeutils.network.DataFetcherBase;
import com.like.likeutils.network.NetParamGenerator;

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
	
	public void fetchRecruitList(String currentPage, String type, String catName, String key, Listener<String> listener, ErrorListener errorListener) {
		Map<String, String> params = NetParamGenerator.getMapParams(APIS.GET_RECRUIT_LIST, currentPage, type, catName, key);
		fetchData(APIS.GET_RECRUIT_LIST, params, listener, errorListener);
	}
	
	public void fetchRecruitDetail(String id, Listener<String> listener, ErrorListener errorListener) {
		fetchData(APIS.GET_RECRUIT_DETAIL, listener, errorListener, id);
	}
	
	public void fetchArea(String cityId, Listener<String> listener, ErrorListener errorListener) {
		fetchData(APIS.GET_AREA, listener, errorListener, cityId);
	}
	
}
