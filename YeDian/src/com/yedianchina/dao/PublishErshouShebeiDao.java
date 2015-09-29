package com.yedianchina.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.yedianchina.po.ErshoushebeiPO;
import com.yedianchina.tools.CONSTANTS;

public class PublishErshouShebeiDao {
	//保持二手设备 服装 培训 管理
	public static Long saveErshoushebei(ErshoushebeiPO po){
		try{
		String url=CONSTANTS.SAVE_ERSHOUSHEBEI;
		HttpPost request = new HttpPost(new URI(url));
		List params = new ArrayList();
		params.add(new BasicNameValuePair("name", po.getName()));
		
		params.add(new BasicNameValuePair("price", po.getPrice()));
		params.add(new BasicNameValuePair("desc", po.getDesc()));
		
		params.add(new BasicNameValuePair("linkmp", po.getLinkmp()));
		params.add(new BasicNameValuePair("linkman", po.getLinkman()));
		
		params.add(new BasicNameValuePair("flag",po.getFlag()));
		
		params.add(new BasicNameValuePair("email",po.getEmail()));
		 
		params.add(new BasicNameValuePair("imgURL0",po.getImgURL0()));
		params.add(new BasicNameValuePair("imgURL1",po.getImgURL1()));
		params.add(new BasicNameValuePair("imgURL2",po.getImgURL2()));
		params.add(new BasicNameValuePair("imgURL3",po.getImgURL3()));
		
	 
		params.add(new BasicNameValuePair("addr", po.getAddr()));
		
		
		
		params.add(new BasicNameValuePair("YD_APPID", CONSTANTS.YD_APPID));
		HttpClient client = new DefaultHttpClient();

		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String out = EntityUtils.toString(entity, "UTF-8");
			 

				JSONObject jo = new JSONObject(out);
				return  jo.getLong("pk");
			}
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0L;
		
	}

}
