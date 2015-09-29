package com.yedianchina.ui.recruit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yedianchina.po.RecruitPO;
import com.yedianchina.ui.R;


public class RecruitDetailFagment extends Fragment{
	static RecruitPO po;
	public static  RecruitDetailFagment initFm(RecruitPO recruitPO){
		po=recruitPO;
		return new RecruitDetailFagment();
		
	}
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        System.out.println("AAAAAAAAAA____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("AAAAAAAAAA____onCreate");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("AAAAAAAAAA____onCreateView"+po.getTitle());
        
        View view= inflater.inflate(R.layout.tab_recruit, container, false);
        
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("AAAAAAAAAA____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("AAAAAAAAAA____onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("AAAAAAAAAA____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("AAAAAAAAAA____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("AAAAAAAAAA____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("AAAAAAAAAA____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("AAAAAAAAAA____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("AAAAAAAAAA____onDetach");
    }
}
