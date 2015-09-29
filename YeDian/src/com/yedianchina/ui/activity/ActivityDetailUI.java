package com.yedianchina.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.gallery.ImageAdapter;
import com.yedianchina.dao.ActivityDao;
import com.yedianchina.po.ActivityPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.R;

@SuppressLint("NewApi")
public class ActivityDetailUI extends CommonActivity {

	@Override
	protected void onDestroy() {
		Log.e("onDestory", "onDestory");

		super.onDestroy();

	}

	private ImageView leftImageView = null;
	private int num = 0;
	/**
	 * 右边箭头按钮
	 */
	private ImageView rightImageView = null;
	private Long pk;
	ActivityPO po = null;

	public void updateCurrendData() {
		this.po = ActivityDao.loadDetail(pk);
	}

	private List<String> imgList;// 商品图片轮播

	TextView themeTv;
	TextView timeTv;
	TextView addrTv;

	TextView navigateTitle;
	TextView contentTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_detail);

//		ImageView h_t = (ImageView) this.findViewById(R.id.h_t);
//		h_t.setBackgroundDrawable(new BitmapDrawable(readBitMap(
//				this.getApplicationContext(), R.drawable.ht0702)));

		navigateTitle = (TextView) this.findViewById(R.id.NavigateTitle);
		navigateTitle.setText("活动详情");
		themeTv = (TextView) this.findViewById(R.id.themeTv);
		timeTv = (TextView) this.findViewById(R.id.timeTv);
		addrTv = (TextView) this.findViewById(R.id.addrTv);
		
		
		contentTv = (TextView) this.findViewById(R.id.contentTv);
		

		Bundle bundle = this.getIntent().getExtras();
		this.pk = bundle.getLong("pk");

		new Thread() {
			public void run() {

				updateCurrendData();
				imgList = new ArrayList<String>();
				String pic0 = po.getPhoto0();
				String pic1 = po.getPhoto1();
				String pic2 = po.getPhoto2();
				String pic3 = po.getPhoto3();

				if (pic0 != null && pic0.length() > 5) {
					pic0 = CONSTANTS.HOST +  pic0;
					imgList.add(pic0);
				}
				if (pic1 != null && pic1.length() > 5) {
					pic1 = CONSTANTS.HOST +  pic1;
					imgList.add(pic1);
				}
				///
				if (pic2 != null && pic2.length() > 5) {
					pic2 = CONSTANTS.HOST + pic2;
					imgList.add(pic2);
				}
				if (pic3 != null && pic3.length() > 5) {
					pic3 = CONSTANTS.HOST + pic3;
					imgList.add(pic3);
				}
				
				
				
				
				
				

				loadingHandler.sendEmptyMessage(1);

			}
		}.start();

		TextView qiandaoBtn = (TextView) this.findViewById(R.id.qiandaoBtn);
		if (qiandaoBtn != null) {
			qiandaoBtn.setText("报名");
			qiandaoBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(ActivityDetailUI.this, BaomingUI.class);
					intent.putExtra("pk", pk);
					ActivityDetailUI.this.startActivity(intent);

				}
			});
		}

		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					ActivityDetailUI.this.finish();

				}
			});
		}
		
		/////
		
		TextView themePreTv=(TextView)this.findViewById(R.id.themePreTv);
		TextPaint paint = themePreTv.getPaint(); 
		paint.setFakeBoldText(true);
		//
		
		
		TextView timePreTv=(TextView)this.findViewById(R.id.timePreTv);
	    paint = timePreTv.getPaint(); 
		paint.setFakeBoldText(true);
		
//		TextView addrPreTv=(TextView)this.findViewById(R.id.addrPreTv);
//	    paint = addrPreTv.getPaint(); 
//		paint.setFakeBoldText(true);

	}

	private void setLoanInfoDetailData() {
		// 商品图片展示 调整图片大小在ImageAdapter setLayoutParams(new
		// GalleryFlow.LayoutParams(270, 400));
		ImageAdapter adapter = new ImageAdapter(ActivityDetailUI.this, imgList);
		adapter.createReflectedImages();

		final Gallery galleryFlow = (Gallery) findViewById(R.id.Gallery01);
		galleryFlow.setAdapter(adapter);
		galleryFlow.setSpacing(10); // 图片之间的间距

		galleryFlow
				.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});

		galleryFlow
				.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

					}

				});

	}

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				setLoanInfoDetailData();
				//
				String theme = po.getName();
				if (theme != null && theme.length() > 0) {
					themeTv.setText(theme);

				}
				String activityTime = po.getActivityTime();
				timeTv.setText(activityTime);
				//
				String address = po.getAddress();
				if (address != null) {
					addrTv.setText(address);
				}
				String content=po.getContent();
				if(content!=null&&content.length()>2){
				    contentTv.setText(content);
				}

			}
		}
	};

}
