package com.yedianchina.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yedianchina.po.MerchantPO;
import com.yedianchina.ui.R;

public class RecruitCompanyDetailFragment extends Fragment {

	private static final String TAG = "TestFragment";
	
	static MerchantPO merchantPO;

	public static RecruitDetailFagment newInstance(MerchantPO _merchantPO) {
		RecruitDetailFagment newFragment = new RecruitDetailFagment();
		
		merchantPO=_merchantPO;
		//newFragment.setArguments(bundle);
		return newFragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "TestFragment-----onCreate");
		Bundle args = getArguments();
	 
	}

	TextView titleTv;
	TextView addTimeTv;
	
	TextView companyDescTv;
	TextView companyNameTv;
	
	TextView addressTv;
	TextView tjTv;
	
	TextView jobDescTv;
	//
	TextView linkmanTv;
	TextView linkMpTv;
	
	TextView emailTv;
	TextView companyAddressTv;
	TextView merchant2Tv;//夜店地址
	
	TextView recruitTv;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "TestFragment-----onCreateView"+merchantPO.getName());
		View view = inflater.inflate(R.layout.recruit_detail_company, container, false);
		
	
				
				
				titleTv = (TextView) view.findViewById(R.id.titleTv);
				addTimeTv = (TextView) view.findViewById(R.id.addTimeTv);
				companyDescTv= (TextView) view.findViewById(R.id.companyDescTv);//公司简介
				
				companyNameTv = (TextView) view.findViewById(R.id.companyNameTv);
				addressTv = (TextView) view.findViewById(R.id.addressTv);
				tjTv= (TextView) view.findViewById(R.id.tjTv);
				
				jobDescTv = (TextView) view.findViewById(R.id.jobDescTv);//职位描述
				linkmanTv = (TextView) view.findViewById(R.id.linkmanTv);
				linkMpTv= (TextView) view.findViewById(R.id.linkMpTv);
				
				
				emailTv = (TextView) view.findViewById(R.id.emailTv); 
				companyAddressTv = (TextView) view.findViewById(R.id.companyAddressTv);
				
				String name=merchantPO.getName();
				if(name==null||"null".equals(name)){
					name="";
				}
				titleTv.setText(name);
				
			 
				
			 
				final String mp=merchantPO.getMp();
				
				String linkman=merchantPO.getLinkman();
				if(linkman==null||"null".equals(linkman)){
					linkman="";
				}
				linkmanTv.setText(linkman);
				
				
				
				
				String address=merchantPO.getAddr();
				if(address!=null&&address.length()>0){
					companyAddressTv.setText(address);
				}
				
				if(address!=null&&address.length()>0){
				merchant2Tv.setText(address);
				}
			
				String desc=merchantPO.getDesc();
				if("null".equals(desc)){
					desc="暂无介绍";
				}
				if(desc!=null&&desc.length()>0){
					
					companyDescTv.setText(Html.fromHtml(desc));
				}
				//
				String email=merchantPO.getEmail();
				if(email==null||"null".equals(email)){
					email="";
				}
				emailTv.setText(email);
		
		
		return view;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "TestFragment-----onDestroy");
	}
}
