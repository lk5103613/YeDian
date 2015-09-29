package com.yedianchina.dao;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.yedianchina.tools.CONSTANTS;



public class AppConfigDao {
	public String  getWechatInvite() {
		try {
			String url =CONSTANTS.HOST+"index.php/AppConfig/getWechatInvite";

			 
			HttpClient client = new DefaultHttpClient();
			HttpPost request;
           
			request = new HttpPost(new URI(url.toString()));

			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
				 
					JSONObject jo=new JSONObject(out);
					return  jo.getString("val");
					
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

	
	public String  aboutUs() {
		try {
			String url =CONSTANTS.HOST+"index.php/AppConfig/aboutUs";

			 
			HttpClient client = new DefaultHttpClient();
			HttpPost request;
           
			request = new HttpPost(new URI(url.toString()));

			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
				 
					return out.trim();
					
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

}
