package com.yedianchina.ui.nearby;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yedianchina.ui.R;
import com.yedianchina.ui.nearby.attention.AttentionFansListUI;
import com.yedianchina.ui.nearby.attention.AttentionMerchantListUI;


public class NearByAttentionFragment extends Fragment {
	static Long mPK;
	static Context mCtx;

	public static  NearByAttentionFragment initFm(Context ctx,Long pk){
		mPK=pk;
		mCtx=ctx;
		return new NearByAttentionFragment();
		
	}
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        //System.out.println("AAAAAAAAAA____onCreateView"+po.getTitle());
	        
	        View view= inflater.inflate(R.layout.nearby_attention, container, false);
	        
	        LinearLayout attentionMerchantLL=(LinearLayout)view.findViewById(R.id.menuListLL);
	        attentionMerchantLL.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent=new Intent();
					intent.setClass(mCtx, AttentionMerchantListUI.class);

					
					intent.putExtra("pk", mPK);

					Log.e("主键", "uid=" + mPK);
					startActivity(intent);
					
				}
			});
	    
	        LinearLayout attentionFansLL=(LinearLayout)view.findViewById(R.id.menuList4LL);
	        attentionFansLL.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent=new Intent();
					intent.setClass(mCtx, AttentionFansListUI.class);

					
					intent.putExtra("pk", mPK);

					Log.e("主键", "uid=" + mPK);
					startActivity(intent);
					
				}
			});
	        
	        
	        return view;
	 }
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        System.out.println("AAAAAAAAAA____onCreate");
	    }
	
	
	
	
	
	

}
