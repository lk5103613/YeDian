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

import com.yedianchina.po.UserPO;
import com.yedianchina.tools.CONSTANTS;

public class FansDao {
	public static Map pageList(int currentPage,String longitude,String latitude,Long uid) {
		String url = CONSTANTS.FANS_PAGE + currentPage+"&longi="+longitude+"&lanti="+latitude+"&uid="+uid;

		List<UserPO> list = new ArrayList<UserPO>();
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

					UserPO m = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jo = (JSONObject) jsonArray.get(i);
						m = new UserPO();
						String nickname = jo.getString("nickname");
						m.setNickname(nickname);

						String last_login = jo.getString("last_login");
						if(last_login!=null&&last_login.length()>=10){
							last_login=last_login.substring(6,16);
							m.setLast_login(last_login);
						}
						

						Long fansUID = jo.getLong("uid");
						m.setUid(fansUID);
						String qm = jo.getString("qm");
						if (qm != null && qm.length() > 0) {
							m.setQm(qm);
						}
						String avatar = jo.getString("avatar");
						if (avatar != null) {
							avatar = CONSTANTS.IMG_HOST + avatar;
							m.setAvatar(avatar);
						}
						
						int age=jo.getInt("age");
						if(age>0){
							m.setAge(age);
						}
						
						String juli=jo.getString("juli");
						if(juli!=null&&juli.length()>0){
							m.setDistance(juli+"");
						}

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
