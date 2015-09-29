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

import com.yedianchina.tools.CONSTANTS;

public class FeedBackDao {
	
	public static boolean saveFeedBack(String content) {
		
	    HttpClient client = new DefaultHttpClient();
        HttpPost request;
		try {
		 
			request = new HttpPost(new URI(CONSTANTS.SAVE_FEEDBACK));
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3); 
			nameValuePairs.add(new BasicNameValuePair("content", content)); 
			
			nameValuePairs.add(new BasicNameValuePair("YD_APPID",CONSTANTS.YD_APPID));
			 
			 
		

			request.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8)); 
		   


            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
				 
					out=out.trim();
					if("1".equals(out)){
						return true;
					}
					 
					

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			

		}
		return false;

}
}
