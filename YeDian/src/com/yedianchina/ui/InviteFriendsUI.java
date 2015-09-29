package com.yedianchina.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class InviteFriendsUI extends Activity {
	protected void onResume() {
		if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 }
		super.onResume();
	}
    
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.invite_friends);
        //
        TextView  tvHeaderTitle=(TextView)this.findViewById(R.id.tvHeaderTitle);
        tvHeaderTitle.setText("邀请好友");
        
        
     
        
      
        
      
        YeDianChinaApplication.getInstance().addActivity(this); 
    }
    
   
    
   

}
