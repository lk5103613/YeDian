package com.yedianchina.ui.nearby;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yedianchina.ui.R;

public class NearbyDengji extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_dengji);
		
		ImageView  backBtn=(ImageView)this.findViewById(R.id.backBtn);
		if(backBtn!=null){
			backBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					NearbyDengji.this.finish();
					
				}
			});
		}
	 
		
		
		
		
	}

}
