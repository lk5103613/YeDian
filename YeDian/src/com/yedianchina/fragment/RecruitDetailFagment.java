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
import com.yedianchina.po.RecruitPO;
import com.yedianchina.ui.R;

public class RecruitDetailFagment extends Fragment {
	public void onSuccess(Object object) {
	}

	private static final String TAG = "TestFragment";
	Long pk = null;
	static RecruitPO po = null;
	static MerchantPO merchantPO;
	static int page = 0;

	public static RecruitDetailFagment newInstance(RecruitPO _po,
			MerchantPO _merchantPO, int _page) {
		RecruitDetailFagment newFragment = new RecruitDetailFagment();
		Bundle bundle = new Bundle();
		po = _po;
		merchantPO = _merchantPO;
		page = _page;

		newFragment.setArguments(bundle);
		return newFragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "TestFragment-----onCreate");

	}

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "TestFragment-----onCreateView"+page);
		View view = null;
		
	
			
			view=inflater.inflate(R.layout.recruit_detail, container, false);
			 
			titleTv = (TextView) view.findViewById(R.id.titleTv);
			addTimeTv = (TextView) view.findViewById(R.id.addTimeTv);
			salaryTv = (TextView) view.findViewById(R.id.salaryTv);

			companyNameTv = (TextView) view.findViewById(R.id.companyNameTv);
			addressTv = (TextView) view.findViewById(R.id.addressTv);
			tjTv = (TextView) view.findViewById(R.id.tjTv);

			jobDescTv = (TextView) view.findViewById(R.id.jobDescTv);// 职位描述
			linkmanTv = (TextView) view.findViewById(R.id.linkmanTv);
			linkMpTv = (TextView) view.findViewById(R.id.linkMpTv);

			//
			emailTv = (TextView) view.findViewById(R.id.emailTv);
			companyAddressTv = (TextView) view.findViewById(R.id.companyAddressTv);

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

			salaryTv.setText(salary);

			String address = po.getAddr();
			if (address != null && address.length() > 0) {
				addressTv.setText(address);
			}
			String tj = po.getTj();// 招聘条件
			if (tj != null && tj.length() > 0) {

				tjTv.setText(Html.fromHtml(tj));
			}
			//
			String job_desc = po.getJob_desc();
			if (job_desc != null && job_desc.length() > 0) {

				jobDescTv.setText(Html.fromHtml(job_desc));
			}
			//
			String email = po.getEmail();
			if (email == null || "null".equals(email)) {
				email = "";
			}
			emailTv.setText(email);
			return view;

	

	
		//

		

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "TestFragment-----onDestroy");
	}
}
