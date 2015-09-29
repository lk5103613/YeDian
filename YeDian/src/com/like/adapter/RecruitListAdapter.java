package com.like.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.like.entity.RecruitInfo;
import com.like.likeutils.network.NetworkUtil;
import com.like.network.APIS;
import com.yedianchina.ui.R;

public class RecruitListAdapter extends BaseAdapter {
	
	private List<RecruitInfo> mRecruits;
	private LayoutInflater mInflater;
	private ImageLoader mImgLoader;
	
	public RecruitListAdapter(Context context, List<RecruitInfo> recruits) {
		this.mRecruits = recruits;
		mInflater = LayoutInflater.from(context);
		mImgLoader = NetworkUtil.getInstance(context).getImageLoader();
	}

	@Override
	public int getCount() {
		return mRecruits.size();
	}

	@Override
	public Object getItem(int position) {
		return mRecruits.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RecruitInfo info = (RecruitInfo) getItem(position);
		ViewHolder vh;
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.post_cell, parent, false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.sLblTitle.setText(info.title);
		if(!TextUtils.isEmpty(info.merchantName)) {
			vh.sName.setText(info.merchantName);
		}
		vh.sAddr.setText(info.addr);
		if(!TextUtils.isEmpty(info.addTime)) {
			vh.sTime.setText(info.addTime);
		}
		mImgLoader.get(APIS.BASE_URL + "/upload/" + info.pic0, ImageLoader.getImageListener(vh.sImg, R.drawable.unloadimage, R.drawable.unloadimage));
		return convertView;
	}
	
	public void clear() {
		this.mRecruits.clear();
		notifyDataSetChanged();
	}
	
	public void update(List<RecruitInfo> recruits) {
		this.mRecruits.addAll(recruits);
		notifyDataSetChanged();
	}
	
	public class ViewHolder {
		public TextView sLblTitle;
		public TextView sName;
		public TextView sAddr;
		public ImageView sImg;
		public TextView sdes;
		public TextView sTime;
		
		public ViewHolder(View v) {
			sLblTitle = (TextView) v.findViewById(R.id.titleTv);
			sName = (TextView) v.findViewById(R.id.merchantNameTv);
			sAddr = (TextView) v.findViewById(R.id.addrTv);
			sTime = (TextView) v.findViewById(R.id.addtimeTv);
			sImg = (ImageView) v.findViewById(R.id.logoImg);
		}
	}

}
