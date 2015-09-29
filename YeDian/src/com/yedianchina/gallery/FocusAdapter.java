package com.yedianchina.gallery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yedianchina.ui.R;

public class FocusAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String,Object>> list;
	public FocusAdapter(Context context){
		this.context=context;
		this.list=new ArrayList<Map<String,Object>>();
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Map<String,Object> getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	public void addObject(Map<String,Object> map){
		list.add(map);
		notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.image_list,null);
		ImageView image = (ImageView)view.findViewById(R.id.imageList_img);
		
		Map<String,Object> map = list.get(position);
		image.setBackgroundDrawable((Drawable)map.get("image"));
		return view;
	}

}
