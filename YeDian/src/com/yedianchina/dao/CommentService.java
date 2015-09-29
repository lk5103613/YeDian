package com.yedianchina.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.yedianchina.tools.CONSTANTS;

public class CommentService {
	// 保存评价司机
	public static boolean saveComment(String content, double star, String mp,
			Long order_id) {

		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {

			request = new HttpPost(new URI(CONSTANTS.SAVE_COMMENT));

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("content", content));
			nameValuePairs.add(new BasicNameValuePair("mp", mp));
			nameValuePairs.add(new BasicNameValuePair("star", star + ""));

			nameValuePairs
					.add(new BasicNameValuePair("order_id", order_id + ""));

			request.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));

			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");

					out = out.trim();
					JSONObject jo = new JSONObject(out);
					int code = jo.getInt("code");
					if (code == 1) {
						return true;
					}
					return false;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;

	}

	public Map findMerchantCommentList(
			long merchantId, int currentPage) {
		try {
			StringBuilder url = new StringBuilder(CONSTANTS.MERCHANT_COMMENT_URL
					+ merchantId + "&currentPage=" + currentPage + "&YD_APPID="
					+ CONSTANTS.YD_APPID);
            Map resultMap=new HashMap();
            Log.e("评论", "url=" + url);
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
							}
							int star = jsonObject.getInt("star");

							if (star > 0) {
								po.put("star", star);
							}

							String mp = jsonObject.getString("mp");
							if (mp != null && mp.length() > 0) {
								String pre = mp.substring(0, 3);
								pre = pre + "*******";
								String last = mp.substring(9, 11);
								mp = pre + last;
								po.put("mp", mp);
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

					return resultMap;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

}
