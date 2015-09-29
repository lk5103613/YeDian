package com.yedianchina.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import com.yedianchina.po.ActivityPO;
import com.yedianchina.po.PaiDuiPO;
import com.yedianchina.tools.CONSTANTS;

public class PaiDuiDao {

	public static PaiDuiPO loadPaiDui(Long id) {
		String url = CONSTANTS.PAIDUI_DETAIL + id;

		List<PaiDuiPO> list = new ArrayList<PaiDuiPO>();
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

					PaiDuiPO m = null;

					m = new PaiDuiPO();

					String name = jo.getString("name");
					m.setName(name);
					
					String info1=jo.getString("info1");
					if(info1!=null&&info1.length()>0){
						m.setInfo1(info1);
					}
					//
				
					
					

					String time = jo.getString("time");
					if (time != null && time.length() > 0) {
						m.setTime(time);
					}

					String avatar = jo.getString("photo0");
					if (avatar != null) {
						avatar = CONSTANTS.IMG_HOST + avatar;
						m.setAvatar(avatar);
						m.setPhoto0(jo.getString("photo0"));
					}else{
						m.setPhoto0("");
					}
					String photo1=jo.getString("photo1");
					if(StringUtils.isNotEmpty(photo1)&&photo1.length()>5){
						m.setPhoto1(photo1);
					}else{
						m.setPhoto1("");
					}
					//
					String photo2=jo.getString("photo2");
					if(StringUtils.isNotEmpty(photo2)&&photo2.length()>5){
						m.setPhoto2(photo2);
					}else{
						m.setPhoto2("");
					}
					//
					String photo3=jo.getString("photo3");
					if(StringUtils.isNotEmpty(photo3)&&photo3.length()>5){
						m.setPhoto3(photo3);
					}else{
						m.setPhoto3("");
					}
					//
					String photo4=jo.getString("photo4");
					if(StringUtils.isNotEmpty(photo4)&&photo4.length()>5){
						m.setPhoto4(photo4);
					}else{
						m.setPhoto4("");
					}
					//
					
					
					String photo5=jo.getString("photo5");
					if(StringUtils.isNotEmpty(photo5)&&photo5.length()>5){
						m.setPhoto5(photo5);
					}else{
						m.setPhoto5("");
					}
					
					String photo6=jo.getString("photo6");
					if(StringUtils.isNotEmpty(photo5)&&photo5.length()>5){
						m.setPhoto6(photo6);
					}else{
						m.setPhoto6("");
					}
					
					String photo7=jo.getString("photo7");
					if(StringUtils.isNotEmpty(photo7)&&photo7.length()>5){
						m.setPhoto7(photo7);
					}else{
						m.setPhoto7("");
					}
					
					
					
					
					
					
					
					
					
					

					String addr = jo.getString("addr");
					if (addr != null && addr.length() > 0) {
						m.setAddr(addr);
					}
					return  m;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean savePaidui(PaiDuiPO po) {
		String url = CONSTANTS.SAVE_PAIDUI;

		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();

			params.add(new BasicNameValuePair("name", po.getName()));
			params.add(new BasicNameValuePair("time", po.getTime()));
			params.add(new BasicNameValuePair("addr", po.getAddr()));
			
			String info1=po.getInfo1();
			if(info1!=null&&info1.length()>1){
				
			}else{
				info1="";
			}
			
			params.add(new BasicNameValuePair("info1", info1));

			//
			// params.add(new BasicNameValuePair("longi", po.getLongi()+""));
			// params.add(new BasicNameValuePair("lanti", po.getLanti()+""));
			// params.add(new BasicNameValuePair("xcy", po.getXcy()));
			// params.add(new BasicNameValuePair("merchantName",
			// po.getMerchantName()));
			// photo0
			params.add(new BasicNameValuePair("photo0", po.getPhoto0()));
			params.add(new BasicNameValuePair("photo1", po.getPhoto1()));
			params.add(new BasicNameValuePair("photo2", po.getPhoto2()));
			params.add(new BasicNameValuePair("photo3", po.getPhoto3()));

			params.add(new BasicNameValuePair("photo4", po.getPhoto4()));
			params.add(new BasicNameValuePair("photo5", po.getPhoto5()));
			params.add(new BasicNameValuePair("photo6", po.getPhoto6()));
			params.add(new BasicNameValuePair("photo7", po.getPhoto7()));

			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {

					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("登陆返回", out);

					JSONObject jo = new JSONObject(out);

					int code = jo.getInt("code");

					if (code == 1) {
						return true;
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;

	}

	public static Map pageList(int currentPage) {
		String url = CONSTANTS.NEARBY_PAIDUI_PAGE + currentPage;

		List<PaiDuiPO> list = new ArrayList<PaiDuiPO>();
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

					PaiDuiPO m = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jo = (JSONObject) jsonArray.get(i);
						m = new PaiDuiPO();

						String name = jo.getString("name");
						m.setName(name);

						String time = jo.getString("time");
						if (time != null && time.length() > 0) {
							m.setTime(time);
						}

						String avatar = jo.getString("photo0");
						if (avatar != null) {
							avatar = CONSTANTS.IMG_HOST + avatar;
							m.setAvatar(avatar);
						}

						String addr = jo.getString("addr");
						if (addr != null && addr.length() > 0) {
							m.setAddr(addr);
						}
						
						Long  id=jo.getLong("id");
						m.setId(id);

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
