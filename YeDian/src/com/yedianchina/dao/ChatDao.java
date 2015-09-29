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

import com.yedianchina.po.ChatPO;
import com.yedianchina.tools.CONSTANTS;

public class ChatDao {
	public static Map pageList(int currentPage,Long  uid) {
		String url = CONSTANTS.CHAT_PAGE+currentPage+"&uid="+uid;
		
		List<ChatPO> list = new ArrayList<ChatPO>();
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

					ChatPO m = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jo = (JSONObject) jsonArray.get(i);
					
						

						m = new ChatPO();
						String addTime = jo.getString("add_time");
						m.setAddTime(addTime);

						String msg = jo.getString("msg");
						m.setMsg(msg);

						Long targetUID=jo.getLong("targetUID");
						m.setTargetUID(targetUID);
						
						String avatar = jo.getString("avatar");
						if(avatar!=null&&avatar.length()>0){
							m.setAvatar(avatar);
						}
						
						String nickname=jo.getString("nickname");
						if(nickname!=null&&nickname.length()>5){
							
							m.setNickname(nickname);
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
