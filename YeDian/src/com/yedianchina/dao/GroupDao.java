package com.yedianchina.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.yedianchina.po.GroupPO;
import com.yedianchina.tools.CONSTANTS;

public class GroupDao {

	public static GroupPO loadGroup(Long group_id) {
		String url = CONSTANTS.GROUP_DETAIL + group_id;

		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			Log.e("RECRUIT_PAGE=", url);
			request = new HttpPost(new URI(url));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("", out);
					JSONObject jo = new JSONObject(out);

					GroupPO m = null;

					m = new GroupPO();
					String group_name = jo.getString("group_name");
					m.setGroup_name(group_name);

					String group_desc = jo.getString("group_desc");
					if (group_desc != null && group_desc.length() >= 10) {
						if (group_desc.length() > 18) {
							group_desc = group_desc.substring(0, 18);
							group_desc = group_desc + "...";
						}
						m.setGroup_desc(group_desc);
					} else {
						m.setGroup_desc("暂无介绍");
					}

					m.setGroup_id(group_id);

					String merchant_name = jo.getString("merchant_name");
					if (merchant_name != null && merchant_name.length() > 0) {
						m.setMerchantName(merchant_name);
					}
					String avatar = jo.getString("avatar");
					if (avatar != null) {
						avatar = CONSTANTS.IMG_HOST + avatar;
						m.setAvatar(avatar);
					}
					Long member_cnt = jo.getLong("member_cnt");
					if (member_cnt != null && member_cnt > 0) {
						m.setMember_cnt(member_cnt);
					} else {
						m.setMember_cnt(0L);
					}
					
					String group_master=jo.getString("group_master");
					if(group_master!=null&&group_master.length()>0){
						m.setGroup_master(group_master);
					}
					
					
					String add_time=jo.getString("add_time");
					if(add_time!=null&&add_time.length()>0){
						add_time=add_time.substring(0,10);
						m.setAdd_time(add_time);
					}

					return m;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map pageList(int currentPage, String longitude,
			String latitude) {
		String url = CONSTANTS.GROUP_LIST + currentPage + "&longi=" + longitude
				+ "&lanti=" + latitude;

		List<GroupPO> list = new ArrayList<GroupPO>();
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			Log.e("RECRUIT_PAGE=", url);
			request = new HttpPost(new URI(url));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("", out);
					JSONObject _JSONObject = new JSONObject(out);
					JSONArray jsonArray = _JSONObject.getJSONArray("list");
					int allCnt = _JSONObject.getInt("size");

					GroupPO m = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jo = (JSONObject) jsonArray.get(i);
						m = new GroupPO();
						String group_name = jo.getString("group_name");
						m.setGroup_name(group_name);

						String group_desc = jo.getString("group_desc");
						if (group_desc != null && group_desc.length() >= 10) {
							if (group_desc.length() > 18) {
								group_desc = group_desc.substring(0, 18);
								group_desc = group_desc + "...";
							}
							m.setGroup_desc(group_desc);
						} else {
							m.setGroup_desc("暂无介绍");
						}

						Long group_id = jo.getLong("group_id");
						m.setGroup_id(group_id);

						String merchant_name = jo.getString("merchant_name");
						if (merchant_name != null && merchant_name.length() > 0) {
							m.setMerchantName(merchant_name);
						}
						String avatar = jo.getString("avatar");
						if (avatar != null) {
							avatar = CONSTANTS.IMG_HOST + avatar;
							m.setAvatar(avatar);
						}
						Long member_cnt = jo.getLong("member_cnt");
						if (member_cnt != null && member_cnt > 0) {
							m.setMember_cnt(member_cnt);
						} else {
							m.setMember_cnt(0L);
						}

						// String juli=jo.getString("juli");
						// if(juli!=null&&juli.length()>0){
						// m.setDistance(juli+"");
						// }

						list.add(m);
					}
					Map resultMap = new HashMap();
					resultMap.put("list", list);
					resultMap.put("allCnt", allCnt);

					return resultMap;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
