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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.yedianchina.tools.CONSTANTS;

public class AppConfigService {
	//获取400电话与短信邀请好友的内容
	public static JSONObject  get400AndSms() {
		HttpClient client = new DefaultHttpClient();
        HttpPost request;
        
		try {
			
			request = new HttpPost(new URI(CONSTANTS.TEL400));
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1); 
			
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8)); 
		   
			HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					 
					JSONObject jsonObject = new JSONObject(out);
					return jsonObject;
//					String  val=jsonObject.getString("val");
//					String sms

					

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			

		}
		return null;

}
}
