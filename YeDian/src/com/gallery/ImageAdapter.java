package com.gallery;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("NewApi")
public class ImageAdapter extends BaseAdapter {

	int mGalleryItemBackground;
	private Context mContext;
	private List<String> mImageIds;
	private ImageView[] mImages;

	public ImageAdapter(Context c, List<String> ImageIds) {
		mContext = c;
		mImageIds = ImageIds;
		int size = 0;
		if (mImageIds != null && mImageIds.size() > 0) {
			size = mImageIds.size();
		}
		mImages = new ImageView[size];

		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		// 设置图片缓存大小为程序最大可用内存的1/8
		imagesCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};

	}

	int index = 0;
	private LruCache<String, Bitmap> imagesCache;

	public boolean createReflectedImages() {
		//final int reflectionGap = 4;
		if (mImageIds != null && mImageIds.size() > 0) {
			for (String url : mImageIds) {
				
				final ImageView imageView = new ImageView(mContext);

				Log.e("图片", "url=" + url);
				Bitmap cachedImage = imagesCache.get(url);
				if (cachedImage != null) {
					imageView.setImageBitmap(cachedImage);
				} else {
					imageView.setTag(url);

					ImageLoader.getInstance().displayImage(url, imageView);
				}

				imageView
						.setLayoutParams(new GalleryFlow.LayoutParams(150*2, 225*2));

				mImages[index++] = imageView;

			}
		}
		return true;
	}

	private Resources getResources() {

		return null;
	}

	public int getCount() {
		if (mImageIds == null || mImageIds.size() == 0) {
			return 0;
		}
		return mImageIds.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View v = mImages[position];

		return v;

	}

	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}

}
