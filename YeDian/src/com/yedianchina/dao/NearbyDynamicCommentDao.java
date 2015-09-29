package com.yedianchina.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.yedianchina.tools.CONSTANTS;

public class NearbyDynamicCommentDao {
	
	public static  boolean saveComment(String content, Long dynamic_id, Long pub_comment_uid) {
		String url = CONSTANTS.saveComment;

	 
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		
	 
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();
			params.add(new BasicNameValuePair("content",content));
			params.add(new BasicNameValuePair("dynamic_id", dynamic_id+""));
			params.add(new BasicNameValuePair("pub_comment_uid", pub_comment_uid+""));
		

			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
				 
					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("saveComment===", out);

					JSONObject jsonObject = new JSONObject(out);

					String _code = jsonObject.getString("code");
					int code = Integer.valueOf(_code);
				 
				
					if (code == 1) {
					     return  true;
					}
			 

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;

	}
	
	
	
	public   static Map findDynamicCommentList(
			long dynamicId, int currentPage) {
		Map resultMap=new HashMap();
		try {
			StringBuilder url = new StringBuilder(CONSTANTS.NEARBY_USER_DYNAMIC_COMMENT
					+ dynamicId + "&currentPage=" + currentPage + "&YD_APPID="
					+ CONSTANTS.YD_APPID);
			
            
            Log.e("动态评论", "url=" + url);
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			HttpClient client = new DefaultHttpClient();
			HttpPost request;

			request = new HttpPost(new URI(url.toString()));

			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("评论", "out=" + out);
					JSONObject jo = new JSONObject(out);
					int allCnt = jo.getInt("size");
					resultMap.put("allCnt", allCnt);

					JSONArray jsonArray=jo.getJSONArray("list");
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
							}else{
								po.put("content", "");
							}
						
							
							String addTime = jsonObject.getString("add_time");
							if (addTime != null && addTime.length() > 0) {
								addTime = addTime.substring(0, 10);
								po.put("addTime", addTime);
							}

							list.add(po);
						}
						resultMap.put("list", list);
					}

					

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return resultMap;

	}

}
