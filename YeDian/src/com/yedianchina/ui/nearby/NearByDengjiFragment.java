package com.yedianchina.ui.nearby;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yedianchina.ui.R;

public class NearByDengjiFragment extends Fragment {
	static Long mPK;

	public static  NearByDengjiFragment initFm(Long pk){
		mPK=pk;
	
		return new NearByDengjiFragment();
		
	}
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        //System.out.println("AAAAAAAAAA____onCreateView"+po.getTitle());
	        
	        View view= inflater.inflate(R.layout.nearby_dengji, container, false);
	        
	        
	        return view;
	 }
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        System.out.println("AAAAAAAAAA____onCreate");
	    }
	
	
	
	
	
	

}
