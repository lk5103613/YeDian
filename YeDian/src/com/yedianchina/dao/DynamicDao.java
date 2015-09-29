package com.yedianchina.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.yedianchina.tools.CONSTANTS;

public class DynamicDao {

	public static Map loadUserDynamicDetail(long dynamic_id) {
		try {
			StringBuilder url = new StringBuilder(
					CONSTANTS.NEARBY_USER_DYNAMIC_DETAIL + dynamic_id
							+ "&YD_APPID=" + CONSTANTS.YD_APPID);
			
			Log.e("动态", "url=" + url);
			
			HttpClient client = new DefaultHttpClient();
			HttpPost request;

			request = new HttpPost(new URI(url.toString()));

			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("动态", "out=" + out);
					JSONObject jsonObject = new JSONObject(out);

					HashMap<String, Object> po = null;

					po = new HashMap<String, Object>();
					String content = jsonObject.getString("content");
					System.out.println("content=" + content);
					if (content != null && content.length() > 0) {
						po.put("content", content);
					}
					int zan_cnt = jsonObject.getInt("zan_cnt");

					po.put("zan_cnt", zan_cnt);

					int msg_cnt = jsonObject.getInt("msg_cnt");

					po.put("msg_cnt", msg_cnt);

					String addTime = jsonObject.getString("add_time");
					if (addTime != null && addTime.length() > 0) {
						addTime = addTime.substring(0, 10);
						po.put("addTime", addTime);
					}

					String avatar = jsonObject.getString("avatar");
					if (avatar != null && avatar.length() > 5) {
						po.put("avatar", avatar);
					}

					po.put("dynamic_id", dynamic_id);
					
					String nickname=jsonObject.getString("nickname");
					if(nickname!=null&&nickname.length()>0){
						po.put("nickname", nickname);
					}else{
						po.put("nickname", "还没有昵称");
					}

				 

					return po;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

	public static Map findUserDynamicList(long uid, int currentPage) {
		try {
			StringBuilder url = new StringBuilder(
					CONSTANTS.NEARBY_USER_DYNAMIC_PAGE + uid + "&currentPage="
							+ currentPage + "&YD_APPID=" + CONSTANTS.YD_APPID);
			Map resultMap = new HashMap();
			Log.e("动态", "url=" + url);
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			HttpClient client = new DefaultHttpClient();
			HttpPost request;

			request = new HttpPost(new URI(url.toString()));

			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("动态", "out=" + out);
					JSONObject jo = new JSONObject(out);
					int allCnt = jo.getInt("size");
					resultMap.put("allCnt", allCnt);

					JSONArray jsonArray = jo.getJSONArray("list");
					HashMap<String, Object> po = null;
					if (jsonArray != null && jsonArray.length() > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArray
									.get(i);
							po = new HashMap<String, Object>();
							String content = jsonObject.getString("content");
							System.out.println("content=" + content);
							if (content != null && content.length() > 0) {
								po.put("content", content);
							}
							int zan_cnt = jsonObject.getInt("zan_cnt");

							po.put("zan_cnt", zan_cnt);

							int msg_cnt = jsonObject.getInt("msg_cnt");

							po.put("msg_cnt", msg_cnt);

							String addTime = jsonObject.getString("add_time");
							if (addTime != null && addTime.length() > 0) {
								addTime = addTime.substring(0, 10);
								po.put("addTime", addTime);
							}

							String avatar = jsonObject.getString("avatar");
							if (avatar != null && avatar.length() > 5) {
								po.put("avatar", avatar);
							}

							Long dynamic_id = jsonObject.getLong("dynamic_id");
							if (dynamic_id != null) {
								po.put("dynamic_id", dynamic_id);
							}
							String nickname=jsonObject.getString("nickname");
							if(nickname!=null&&nickname.length()>0){
								po.put("nickname", nickname);
							}else{
								po.put("nickname", "还没有昵称");
							}

							list.add(po);
						}
						resultMap.put("list", list);
					}

					return resultMap;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

}
