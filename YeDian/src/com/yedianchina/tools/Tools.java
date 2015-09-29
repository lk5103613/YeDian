package com.yedianchina.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
public class Tools {
	public static boolean  isEmpty(String s){
		if(s==null||"".equals(s)||s.length()==0){
			return true;
		}
		return false;
	}
	public static boolean isNetworkConnected(Context context) {  
	     if (context != null) {  
	         ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                 .getSystemService(Context.CONNECTIVITY_SERVICE);  
	         NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	         if (mNetworkInfo != null) {  
	             return mNetworkInfo.isAvailable();  
	         }  
	     }  
	     return false;  
	 }
	
	


}
