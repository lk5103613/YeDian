package com.yedianchina.ui.recruit;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gallery.ImageAdapter;
import com.yedianchina.dao.RecruitDao;
import com.yedianchina.po.RecruitPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.R;

@SuppressLint("NewApi")
public class RecruitDetailUI extends Activity {
	
	private List<String> imgList;//图片轮播
	private void setLoanInfoDetailData() {
		// 商品图片展示  调整图片大小在ImageAdapter setLayoutParams(new GalleryFlow.LayoutParams(270, 400));
		ImageAdapter adapter = new ImageAdapter(RecruitDetailUI.this, imgList);
		adapter.createReflectedImages();
		

		final Gallery  galleryFlow = (Gallery ) findViewById(R.id.Gallery01);
		galleryFlow.setAdapter(adapter);
		galleryFlow.setSpacing(0); // 图片之间的间距
		galleryFlow.setLeft(0);

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
	
	

 
	Long pk;
	RecruitPO po = null;
	 
	
	
	 TextView titleTv;
		TextView addTimeTv;

		TextView salaryTv;
		TextView companyNameTv;

		TextView addressTv;
		TextView tjTv;

		TextView jobDescTv;
		//
		TextView linkmanTv;
		TextView linkMpTv;

		TextView emailTv;
		TextView companyAddressTv;
		LinearLayout linkmpLL;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.recruit_detail);
		pk = this.getIntent().getExtras().getLong("pk");
		
		this.linkmpLL=(LinearLayout)this.findViewById(R.id.linkmpLL);

		resources = getResources();

		new Thread() {
			public void run() {
				po = RecruitDao.loadRecruit(pk);
				imgList = new ArrayList<String>();
				
				String pic0=po.getPic0();
				if(pic0!=null&&pic0.length()>5){
					pic0=CONSTANTS.HOST+"ios/"+pic0;
					
					imgList.add(pic0);
				}
				
				String pic1=po.getPic1();
				if(pic1!=null&&pic1.length()>5){
					pic1=CONSTANTS.HOST+"ios/"+pic1;
					imgList.add(pic1);
				}
				//
				String pic2=po.getPic2();
				if(pic2!=null&&pic2.length()>5){
					pic2=CONSTANTS.HOST+"ios/"+pic2;
					imgList.add(pic2);
				}
				//
				String pic3=po.getPic3();
				if(pic3!=null&&pic3.length()>5){
					pic3=CONSTANTS.HOST+"ios/"+pic3;
					imgList.add(pic3);
				}
				
				Log.e("", "pic0="+pic0+" pic1="+pic1+"  pic2="+pic2+"  pic3="+pic3);
			

				loadingHandler.sendEmptyMessage(1);

			}
		}.start();
		
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					RecruitDetailUI.this.finish();

				}
			});
		}
		
		
		TextView  qiandaoBtn=(TextView)this.findViewById(R.id.qiandaoBtn);
		if(qiandaoBtn!=null){
			qiandaoBtn.setVisibility(View.INVISIBLE);
		}
		
		titleTv = (TextView) this.findViewById(R.id.titleTv);
		TextView titleTv = (TextView) this.findViewById(R.id.titleTv);
		TextPaint  paint = titleTv.getPaint();
		paint.setFakeBoldText(true);
		
		
		addTimeTv = (TextView) this.findViewById(R.id.addTimeTv);
	 

	 
		tjTv = (TextView) this.findViewById(R.id.tjTv);

	 
		linkmanTv = (TextView) this.findViewById(R.id.linkmanTv);
		linkMpTv = (TextView) this.findViewById(R.id.linkMpTv);

		//
		emailTv = (TextView) this.findViewById(R.id.emailTv);
		companyAddressTv = (TextView) this.findViewById(R.id.companyAddressTv);
		
		
		TextView NavigateTitle=(TextView)this.findViewById(R.id.NavigateTitle);
		if(NavigateTitle!=null){
			NavigateTitle.setText("招聘详情");
		}
		//
		TextView tqPre = (TextView) this.findViewById(R.id.tqPre);
		paint = tqPre.getPaint();
		paint.setFakeBoldText(true);
		
	
		//
		TextView linkmanPre = (TextView) this.findViewById(R.id.linkmanPre);
		paint = linkmanPre.getPaint();
		paint.setFakeBoldText(true);
		
		//
		TextView linkmpPre = (TextView) this.findViewById(R.id.linkmpPre);
		paint = linkmpPre.getPaint();
		paint.setFakeBoldText(true);
		//
		TextView emailPre = (TextView) this.findViewById(R.id.emailPre);
		paint = emailPre.getPaint();
		paint.setFakeBoldText(true);
		//
		TextView addressLinkPre = (TextView) this.findViewById(R.id.addressLinkPre);
		paint = addressLinkPre.getPaint();
		paint.setFakeBoldText(true);
	 

	}

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {

				int what = msg.what;
				if (what == 1) {
					
					
					

					// //////////////
					String title = po.getTitle();
					if (title == null || "null".equals(title)) {
						title = "";
					}
					titleTv.setText(title);

					String add_time = po.getAdd_time();
					if (add_time == null || "null".equals(add_time)) {
						add_time = "";
					}
					addTimeTv.setText(add_time);

					String salary = po.getSalary();

					if (salary == null || "null".equals(salary)) {
						salary = "";
					}
					final String mp = po.getMp();

					String linkman = po.getLinkman();
					if (linkman == null || "null".equals(linkman)) {
						linkman = "";
					}
					linkmanTv.setText(linkman);
					
					
					if(mp!=null&&mp.length()>=7){
					linkMpTv.setText(mp);
					
					linkmpLL.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							telFn(mp);
							
						}
					});
					}
					
					String email=po.getEmail();
					if("null".equals(email)){
						email="";
					}
					if(StringUtils.isNotEmpty(email)){
					   emailTv.setText(email);
					}

				

					 
					String tj = po.getTj();// 招聘条件
					if (tj != null && tj.length() > 0) {

						tjTv.setText(Html.fromHtml(tj));
					}
				 
					
				
					
					companyAddressTv.setText(po.getAddr());
					
					setLoanInfoDetailData();

					

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	public void telFn(final String  mp) {
	 
		AlertDialog.Builder alert = new AlertDialog.Builder(RecruitDetailUI.this);
		alert.setTitle("拨打电话确认")
				.setMessage("您确认拨打对方电话吗？")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent phoneIntent = new Intent(
								"android.intent.action.CALL",

								Uri.parse("tel:" + mp));
						dialog.dismiss();
						startActivity(phoneIntent);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		alert.create().show();
	}

	

	
	private Resources resources;
	private int currIndex = 0;
	private TextView detailTab, companyTab;

	

}
