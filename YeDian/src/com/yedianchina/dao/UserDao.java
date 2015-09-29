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
import java.text.DecimalFormat;
import java.util.Locale;
public class UserDao {

	public static List pageMap(String longitude, String latitude) {
		String url = CONSTANTS.NEARBY_USER_Map + "?longi=" + longitude
				+ "&lanti=" + latitude;

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

					JSONArray jsonArray = new JSONArray(out);

					UserPO m = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jo = (JSONObject) jsonArray.get(i);
						m = new UserPO();
						String nickname = jo.getString("nickname");
						m.setNickname(nickname);

						String last_login = jo.getString("last_login");
						if (last_login != null && last_login.length() >= 10) {
							last_login = last_login.substring(6, 16);
							m.setLast_login(last_login);
						}

						Long uid = jo.getLong("uid");
						m.setUid(uid);
						String qm = jo.getString("qm");
						if (qm != null && qm.length() > 0) {
							m.setQm(qm);
						}
						String avatar = jo.getString("avatar");
						if (avatar != null) {
							avatar = CONSTANTS.IMG_HOST + avatar;
							m.setAvatar(avatar);
						}

						int age = jo.getInt("age");
						if (age > 0) {
							m.setAge(age);
						}

						String juli = jo.getString("juli");
						if (juli != null && juli.length() > 0) {
							m.setDistance(juli + "");
						}
						String longi = jo.getString("longi");
						if (longi != null && longi.length() > 0) {
							m.setLongi(longi);
						}

						String lanti = jo.getString("lanti");
						if (lanti != null && lanti.length() > 0) {
							m.setLanti(lanti);
						}

						list.add(m);
					}

					return list;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map pageList(int currentPage, String longitude,
			String latitude,int gender,int sortBy) {
		String url = CONSTANTS.NEARBY_USER_PAGE + currentPage + "&longi="
				+ longitude + "&lanti=" + latitude+"&gender="+gender;
		
		Log.e("附近URL＝＝＝＝＝＝＝＝＝＝＝＝＝", url);
		
		
		

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
						if (last_login != null && last_login.length() >= 10) {
							last_login = last_login.substring(6, 16);
							m.setLast_login(last_login);
						}

						Long uid = jo.getLong("uid");
						m.setUid(uid);
						String qm = jo.getString("qm");
						if (qm != null && qm.length() > 0) {
							m.setQm(qm);
						}
						String avatar = jo.getString("avatar");
						if (avatar != null) {
							avatar = CONSTANTS.IMG_HOST + avatar;
							m.setAvatar(avatar);
						}

						int age = jo.getInt("age");
						if (age > 0) {
							m.setAge(age);
						}

						String juli = jo.getString("juli");
						if (juli != null && juli.length() > 0) {
							double  _juli=Double.parseDouble(juli);
							DecimalFormat df1 = new DecimalFormat("####.0");
						
							
							_juli=_juli*0.001;
							String  distance=df1.format(_juli);
							
							m.setDistance(distance);
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
	

	public static UserPO loadUserInfo(long userId) {
		try {
			StringBuilder url = new StringBuilder(CONSTANTS.NEARBY_USER_DYNAMIC);
			url.append(userId);

			UserPO po = new UserPO();
			HttpClient client = new DefaultHttpClient();
			HttpPost request;
			Log.e("loadUserInfo-------------------", url.toString());

			request = new HttpPost(new URI(url.toString()));

			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");

					JSONObject jsonObject = new JSONObject(out);

					String nickname = jsonObject.getString("nickname");
					System.out.println("nickname=" + nickname);
					if (nickname != null && nickname.length() > 0) {
						po.setNickname(nickname);
					}else{
						po.setNickname("");
					}
					
					
					int age = jsonObject.getInt("age");
					if (age > 0) {
						po.setAge(age);
					} else {
						po.setAge(0);
					}

					String avatar = jsonObject.getString("avatar");
					if (avatar != null && avatar.length() > 0) {

						if (!"null".equals(avatar)) {
							avatar = CONSTANTS.IMG_HOST + avatar;

							po.setAvatar(avatar);
						}
					}else{
						po.setAvatar("");
					}

					String mp = jsonObject.getString("mp");
					if (mp != null && mp.length() > 0) {

						po.setMp(mp);
					}else{
						po.setMp("");
					}

					Long uid = jsonObject.getLong("uid");
					po.setUid(uid);

					int gender = jsonObject.getInt("gender");
					po.setGender(gender);

					String last_login = jsonObject.getString("last_login");
					if (last_login != null && last_login.length() > 0) {
						po.setLast_login(last_login);
					}else{
						po.setLast_login("");
					}

					String astro = jsonObject.getString("astro");
					if (astro != null && astro.length() > 0&&!"null".equals(astro)) {
						po.setAstro(astro);
					} else {
						po.setAstro("");
					}
					String height = jsonObject.getString("height");
					if (height != null && height.length() > 0) {
						po.setHeight(height);
					}else{
						po.setHeight("");
					}
					
					String weight = jsonObject.getString("weight");
					if (weight != null && weight.length() > 0) {
						po.setWeight(weight);
					}else{
						po.setWeight("");
					}
					
					int chat=jsonObject.getInt("chat");
					po.setChat(chat);
					
					String  pwd=jsonObject.getString("pwd");
					if(pwd!=null){
						po.setPwd(pwd);
					}else{
						po.setPwd("");
					}
					String city_name=jsonObject.getString("city_name");
					if(city_name!=null&&!"null".equals(city_name)){
						po.setCity_name(city_name);
					}else{
						po.setCity_name("");
					}
					String qm = jsonObject.getString("qm");
					if (qm != null && qm.length() > 0) {
						po.setQm(qm);
					}
					
					String birthday=jsonObject.getString("birthday");
					if(birthday!=null&&birthday.length()>5){
						po.setBirthday(birthday);
					}
				 
					String home_town=jsonObject.getString("home_town");
					if(home_town!=null&&home_town.length()>5){
						po.setHome_town(home_town);
					}
					
				

					return po;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

}
