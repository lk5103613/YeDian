 package com.yedianchina.control;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.yedianchina.ui.R;
import com.yedianchina.ui.job.JobReqDetailUI;



public class ImageAdapterJobDetailUI extends BaseAdapter {
	public static final BaseAdapter Adapter = null;
	public List<String> imageUrls;
	private Context context;
	int mGalleryItemBackground;

	public ImageAdapterJobDetailUI(List<String> imageUrls, Context context) {
		this.imageUrls = imageUrls;
		this.context = context;
		 
		TypedArray a = context.obtainStyledAttributes(R.styleable.Gallery);
		 
		mGalleryItemBackground = a.getResourceId(
				R.styleable.Gallery_android_galleryItemBackground, 0);
		 
		a.recycle();
	}

	public int getCount() {
		return imageUrls.size();
	}

	public Object getItem(int position) {
		return imageUrls.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Bitmap image;
		ImageView view = new ImageView(context);
		image = JobReqDetailUI.imagesCache.get(imageUrls.get(position));
		// �ӻ����ж�ȡͼƬ
		if (image == null) {
			image = JobReqDetailUI.imagesCache.get("background_non_load");
		}
		// ��������ͼƬ����Դ��ַ
		view.setImageBitmap(image);
		view.setScaleType(ImageView.ScaleType.FIT_XY);
		view.setLayoutParams(new Gallery.LayoutParams(320, 260));
		view.setBackgroundResource(mGalleryItemBackground);
		/* ����Gallery����ͼ */
		return view;
	}
}


