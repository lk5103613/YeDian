package com.yedianchina.dao;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.yedianchina.tools.CONSTANTS;

public class SmsService {
	public static String updateMpStatus(String mp) {
		try {

			StringBuilder url = new StringBuilder(CONSTANTS.UPDATE_MP_STATUS);
			url.append("?mp=");
			url.append(mp);
			url.append("&YD_APPID=" + CONSTANTS.YD_APPID);

			HttpClient client = new DefaultHttpClient();
			HttpPost request;
		 
			request = new HttpPost(new URI(url.toString()));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					out=out.trim();
				 
					return out;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}
	
	
	
	//////////////////////////////////////////////////////////////////
	// 获取手机验证码
	public static String sendSmsFn(String mp, String code) {
		try {

			StringBuilder url = new StringBuilder(CONSTANTS.SMS_SEND_CODE);
			url.append("?mp=");
			url.append(mp);
			url.append("&code=");
			url.append(code);
			url.append("&YD_APPID=" + CONSTANTS.YD_APPID);
			Log.e("Sms", url.toString());

			HttpClient client = new DefaultHttpClient();
			HttpPost request;
		 
			request = new HttpPost(new URI(url.toString()));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
				 
					return out;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}
}
