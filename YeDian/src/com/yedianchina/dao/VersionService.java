package com.yedianchina.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.sina.weibo.sdk.log.Log;
import com.yedianchina.po.AppVersion;
import com.yedianchina.tools.CONSTANTS;

public class VersionService {
	//获取最新的版本号
	public static AppVersion  getLatestVersion() {
		HttpClient client = new DefaultHttpClient();
        HttpPost request;
        AppVersion po=null;
		try {
		 
			request = new HttpPost(new URI(CONSTANTS.SERVER_VERSION_URL));
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1); 
			 
			nameValuePairs.add(new BasicNameValuePair("YD_APPID",CONSTANTS.YD_APPID));
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8)); 
		   
			HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("getLatestVersion=====", out);
					
					 
					JSONObject jsonObject = new JSONObject(out);

					int version = jsonObject.getInt("version");
				 
					String url=jsonObject.getString("url");
					
					po=new AppVersion();
					po.setVersion(version);
					if(url!=null){
					  po.setUrl(url);
					}
					return po;
					

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			

		}
		return po;

}


}
