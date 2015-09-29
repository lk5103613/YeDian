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

import com.yedianchina.tools.CONSTANTS;

public class MyOrderService {
	//我的服务单  11-26
	public static Map findMyServiceOrderList(int currentPage, String mp) {

		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		HashMap<String,Object> po = null;
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		try {
			String url="CONSTANTS.findMyServiceOrderList" + mp
					+ "&currentPage=" + currentPage+"&YD_APPID="+CONSTANTS.YD_APPID;
			request = new HttpPost(new URI(url));
		 
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					JSONObject jsonObj = new JSONObject(out);
					Object listObj = jsonObj.get("list");
					Object sizeObj = jsonObj.get("size");

					String s=listObj.toString();
					if(s==null||s.length()<10){
						
						return null;
					}
				 
					JSONArray jsonArray = new JSONArray(listObj.toString());
					Map m = null;

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						po = new HashMap<String,Object>();

						String endAddr = jsonObject.getString("end_addr");
						System.out.println("endAddr=" + endAddr);
						if (endAddr != null && endAddr.length() > 0&&!"null".equals(endAddr)){
							po.put("endAddr", endAddr);
						}else{
							po.put("endAddr", "");
						}
						mp = jsonObject.getString("driver_mp");

						if (mp != null&&!"null".equals(mp)){
							po.put("mp", mp);
						}else{
							po.put("mp", "");
						}

						String subscribe = jsonObject.getString("subscribe");
						if (subscribe != null && subscribe.length() > 0&&!"null".equals(subscribe)){
							//subscribe=subscribe.substring(0,10);
							po.put("subscribe", subscribe);
						}else{
							po.put("subscribe", "");
						}
						String start_addr=jsonObject.getString("start_addr");
						if(start_addr!=null&&start_addr.length()>0&&!"null".equals(start_addr)){
							po.put("start_addr", start_addr);
						}else{
							po.put("start_addr", "");
						}
						String add_time=jsonObject.getString("add_time");
						if(add_time!=null&&add_time.length()>0&&!"null".equals(add_time)){
							//add_time=add_time.substring(0, 12);
							po.put("add_time", add_time);
						}else{
							po.put("add_time", "");
						}
						String avatar=jsonObject.getString("avatar");
					 
						if(avatar!=null&&avatar.length()>4){
							avatar=CONSTANTS.HOST+avatar;
							po.put("avatar", avatar);
						}
						
						
						String arrive_time=jsonObject.getString("arrive_time");
						if(arrive_time!=null){
							po.put("arrive_time", arrive_time);
						}
						

						Long id = jsonObject.getLong("id");
						po.put("id", id);
						list.add(po);
					}
					m=new HashMap();
					m.put("list", list);
					int size=new Integer(sizeObj.toString());
					m.put("size", size);
					

					return m;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}
	
	public static Map loadMyOrder(long id) {

		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		HashMap<String,Object> po = null;
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		try {
			String url=CONSTANTS.LOAD_ORDER_DETAIL + id;
				
			request = new HttpPost(new URI(url));
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1); 
			nameValuePairs.add(new BasicNameValuePair("YD_APPID", CONSTANTS.YD_APPID)); 
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8)); 
			
			
			
			
		 
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					JSONObject jsonObject = new JSONObject(out);
					
						po = new HashMap<String,Object>();

						String endAddr = jsonObject.getString("end_addr");
						System.out.println("endAddr=" + endAddr);
						if (endAddr != null && endAddr.length() > 0&&!"null".equals(endAddr)){
							po.put("end_addr", endAddr);
						}else{
							po.put("endAddr", "");
						}
						String mp = jsonObject.getString("driver_mp");

						if (mp != null&&!"null".equals(mp)){
							po.put("driver_mp", mp);
						}else{
							po.put("driver_mp", "");
						}

						String subscribe = jsonObject.getString("subscribe");
						if (subscribe != null && subscribe.length() > 0&&!"null".equals(subscribe)){
							//subscribe=subscribe.substring(0,10);
							po.put("subscribe", subscribe);
						}else{
							po.put("subscribe", "");
						}
						String start_addr=jsonObject.getString("start_addr");
						if(start_addr!=null&&start_addr.length()>0&&!"null".equals(start_addr)){
							po.put("start_addr", start_addr);
						}else{
							po.put("start_addr", "");
						}
						String add_time=jsonObject.getString("add_time");
						if(add_time!=null&&add_time.length()>0&&!"null".equals(add_time)){
							add_time=add_time.substring(0, 16);
							po.put("add_time", add_time);
						}else{
							po.put("add_time", "");
						}
						
                        String driver_num=jsonObject.getString("driver_num");
						po.put("driver_num", driver_num);
						
						String arrive_time=jsonObject.getString("arrive_time");
						if(arrive_time!=null){
							po.put("arrive_time", arrive_time);
						}

					
						po.put("id", id);
						
					
					

					return po;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}
	
		public static Map findOrderList(int currentPage, String mp) {

			HttpClient client = new DefaultHttpClient();
			HttpPost request;
			HashMap<String,Object> po = null;
			List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
			try {
				String url=CONSTANTS.MY_ORDER_LIST + mp
						+ "&currentPage=" + currentPage+"&YD_APPID="+CONSTANTS.YD_APPID;
				request = new HttpPost(new URI(url));
			 
				HttpResponse response = client.execute(request);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						String out = EntityUtils.toString(entity, "UTF-8");
						JSONObject jsonObj = new JSONObject(out);
						Object listObj = jsonObj.get("list");
						Object sizeObj = jsonObj.get("size");

						String s=listObj.toString();
						if(s==null||s.length()<10){
							
							return null;
						}
					 
						JSONArray jsonArray = new JSONArray(listObj.toString());
						Map m = null;

						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = (JSONObject) jsonArray.get(i);
							po = new HashMap<String,Object>();

							String endAddr = jsonObject.getString("end_addr");
							System.out.println("endAddr=" + endAddr);
							if (endAddr != null && endAddr.length() > 0&&!"null".equals(endAddr)){
								po.put("endAddr", endAddr);
							}else{
								po.put("endAddr", "");
							}
							mp = jsonObject.getString("driver_mp");

							if (mp != null&&!"null".equals(mp)){
								po.put("mp", mp);
							}else{
								po.put("mp", "");
							}

							String subscribe = jsonObject.getString("subscribe");
							if (subscribe != null && subscribe.length() > 0&&!"null".equals(subscribe)){
								//subscribe=subscribe.substring(0,10);
								po.put("subscribe", subscribe);
							}else{
								po.put("subscribe", "");
							}
							String start_addr=jsonObject.getString("start_addr");
							if(start_addr!=null&&start_addr.length()>0&&!"null".equals(start_addr)){
								po.put("start_addr", start_addr);
							}else{
								po.put("start_addr", "");
							}
							String add_time=jsonObject.getString("add_time");
							if(add_time!=null&&add_time.length()>0&&!"null".equals(add_time)){
								//add_time=add_time.substring(0, 12);
								po.put("add_time", add_time);
							}else{
								po.put("add_time", "");
							}
							String avatar=jsonObject.getString("avatar");
						 
							if(avatar!=null&&avatar.length()>4){
								avatar=CONSTANTS.HOST+avatar;
								po.put("avatar", avatar);
							}
							String arrive_time=jsonObject.getString("arrive_time");
							if(arrive_time!=null){
								po.put("arrive_time", arrive_time);
							}
							

							Long id = jsonObject.getLong("id");
							po.put("id", id);
							list.add(po);
						}
						m=new HashMap();
						m.put("list", list);
						int size=new Integer(sizeObj.toString());
						m.put("size", size);
						

						return m;

					}
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
			return null;

		}
}
