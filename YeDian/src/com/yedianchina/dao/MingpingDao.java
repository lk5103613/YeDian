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

import android.util.Log;

import com.yedianchina.po.UserPO;
import com.yedianchina.tools.CONSTANTS;

public class MingpingDao {
	
	public static int updateMingPian(Long loginUID,UserPO userPO) {
		String url = CONSTANTS.UPDATE_USER;

		UserPO po = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();
			params.add(new BasicNameValuePair("uid", loginUID+""));
			
			params.add(new BasicNameValuePair("nickname", userPO.getNickname()));
			
			params.add(new BasicNameValuePair("avatar", userPO.getAvatar()));
			
			
			params.add(new BasicNameValuePair("gender", ""+userPO.getGender()));
			params.add(new BasicNameValuePair("birthday", userPO.getBirthday()));
			params.add(new BasicNameValuePair("height", userPO.getHeight()));
			params.add(new BasicNameValuePair("weight", userPO.getWeight()));
			params.add(new BasicNameValuePair("qm", userPO.getQm()));
			params.add(new BasicNameValuePair("city_name", userPO.getCity_name()));//所在地
			params.add(new BasicNameValuePair("home_town", userPO.getHome_town()));//故乡
			params.add(new BasicNameValuePair("chat","1"));
			
			

			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					po = new UserPO();
					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("登陆返回", out);

					JSONObject jo = new JSONObject(out);

					int code = jo.getInt("code");
					return  code;


				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return -1;

	}

}
