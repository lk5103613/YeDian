package com.yedianchina.ui.paidui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.yedianchina.dao.PaiDuiDao;
import com.yedianchina.po.PaiDuiPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.R;
import com.yedianchina.ui.activity.BaomingUI;

@SuppressLint("NewApi")
public class PaiDuiDetailUI extends CommonActivity {

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
	PaiDuiPO po = null;

	public void updateCurrendData() {
		this.po = PaiDuiDao.loadPaiDui(pk);
	}

	private List<String> imgList;// 商品图片轮播
	
	

	TextView themeTv;
	TextView timeTv;
	TextView addrTv;

	TextView navigateTitle;

	TextView info1Tv;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.paidui_detail);

	

		navigateTitle = (TextView) this.findViewById(R.id.NavigateTitle);
		navigateTitle.setText("派对详情");
		themeTv = (TextView) this.findViewById(R.id.themeTv);
		
		
	 
		TextPaint paint = themeTv.getPaint(); 
		paint.setFakeBoldText(true);
		
		
		timeTv = (TextView) this.findViewById(R.id.timeTv);
	    paint = timeTv.getPaint(); 
		paint.setFakeBoldText(true);
		
		
		addrTv = (TextView) this.findViewById(R.id.addrTv);
		   paint = addrTv.getPaint(); 
			paint.setFakeBoldText(true);
			
		info1Tv = (TextView) this.findViewById(R.id.info1Tv);
	 

		Bundle bundle = this.getIntent().getExtras();
		this.pk = bundle.getLong("pk");

		new Thread() {
			public void run() {

				updateCurrendData();
				imgList = new ArrayList<String>();

				if (po.getPhoto0().length() > 5) {
					String photo0 = CONSTANTS.HOST + po.getPhoto0();
					imgList.add(photo0);
				}
				if (po.getPhoto1().length() > 5) {
					String photo1 = CONSTANTS.HOST + po.getPhoto1();
					imgList.add(photo1);
				}
				if (po.getPhoto2().length() > 5) {
					String photo2 = CONSTANTS.HOST + po.getPhoto2();
					imgList.add(photo2);
				}
				if (po.getPhoto3().length() > 5) {
					String photo3 = CONSTANTS.HOST + po.getPhoto3();
					imgList.add(photo3);
				}
				// ////////
				if (po.getPhoto4().length() > 5) {
					String photo4 = CONSTANTS.HOST + po.getPhoto4();
					imgList.add(photo4);
				}
				if (po.getPhoto5().length() > 5) {
					String photo5 = CONSTANTS.HOST + po.getPhoto5();
					imgList.add(photo5);
				}
				if (po.getPhoto6().length() > 5) {
					String photo6 = CONSTANTS.HOST + po.getPhoto6();
					imgList.add(photo6);
				}
				if (po.getPhoto7().length() > 5) {
					String photo7 = CONSTANTS.HOST + po.getPhoto7();
					imgList.add(photo7);
				}

				Log.e("imgList", "imgList数量====" + imgList.size());

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
					intent.setClass(PaiDuiDetailUI.this, BaomingUI.class);
					intent.putExtra("pk", pk);
					PaiDuiDetailUI.this.startActivity(intent);

				}
			});
		}

		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					PaiDuiDetailUI.this.finish();

				}
			});
		}
		/////08-22字号加粗
		TextView thmemePreTv=(TextView)this.findViewById(R.id.thmemePreTv);
		paint = thmemePreTv.getPaint(); 
		paint.setFakeBoldText(true);
		//
		TextView timePreTv=(TextView)this.findViewById(R.id.timePreTv);
		paint = timePreTv.getPaint(); 
		paint.setFakeBoldText(true);
		//
		TextView addrPreTv=(TextView)this.findViewById(R.id.addrPreTv);
		paint = addrPreTv.getPaint(); 
		paint.setFakeBoldText(true);
		
		
		TextView detailPreTv=(TextView)this.findViewById(R.id.detailPreTv);
		paint = detailPreTv.getPaint(); 
		paint.setFakeBoldText(true);

	}

	private void setLoanInfoDetailData() {
		// 商品图片展示 调整图片大小在ImageAdapter setLayoutParams(new
		// GalleryFlow.LayoutParams(270, 400));
		ImageAdapter adapter = new ImageAdapter(PaiDuiDetailUI.this, imgList);
		adapter.createReflectedImages();

		final Gallery galleryFlow = (Gallery) findViewById(R.id.Gallery01);
		galleryFlow.setAdapter(adapter);
		galleryFlow.setSpacing(6); // 图片之间的间距

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
				String activityTime = po.getTime();
				timeTv.setText(activityTime);
				//
				String address = po.getAddr();
				if (address != null) {
					addrTv.setText(address);
				}

				String info1 = po.getInfo1();
				if (info1 != null && info1.length() > 0) {
					info1Tv.setText(info1);
				}

				

			}
		}
	};

}
