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

import com.yedianchina.po.ActivityPO;
import com.yedianchina.po.UserPO;
import com.yedianchina.tools.CONSTANTS;

public class ActivityDao {
	public static boolean saveActivity(ActivityPO po) {
		String url = CONSTANTS.SAVE_ACTIVITY;

		
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();

			params.add(new BasicNameValuePair("name", po.getName()));
			params.add(new BasicNameValuePair("content", po.getContent()));
			params.add(new BasicNameValuePair("activity_time", po.getActivityTime()));
			params.add(new BasicNameValuePair("logo", po.getLogo()));
			params.add(new BasicNameValuePair("address", po.getAddress()));
			//
			params.add(new BasicNameValuePair("longi", po.getLongi()+""));
			params.add(new BasicNameValuePair("lanti", po.getLanti()+""));
			params.add(new BasicNameValuePair("xcy", po.getXcy()));
			params.add(new BasicNameValuePair("merchantName", po.getMerchantName()));
			//photo0
			params.add(new BasicNameValuePair("photo0", po.getPhoto0()));
			params.add(new BasicNameValuePair("photo1", po.getPhoto1()));
			params.add(new BasicNameValuePair("photo2", po.getPhoto2()));
			params.add(new BasicNameValuePair("photo3", po.getPhoto3()));
		
			
			

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
	
	public static boolean saveBaoming(String user_cnt ,String linkman,String linkmp,Long activity_id,String memo) {
		String url = CONSTANTS.SAVE_BAOMING;

		UserPO po = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();

			params.add(new BasicNameValuePair("user_cnt", user_cnt));
			params.add(new BasicNameValuePair("linkman", linkman));
			params.add(new BasicNameValuePair("linkmp", linkmp));
			params.add(new BasicNameValuePair("activity_id", activity_id+""));
			params.add(new BasicNameValuePair("memo", memo));
			
			

			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					po = new UserPO();
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	////////////////////////////////////////////////////
	public static ActivityPO loadDetail(Long pk) {
		String url = CONSTANTS.ACTIVITY_DETAIL+pk;
		
		 
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
					 

					ActivityPO m = null;
					 
						
						m = new ActivityPO();
						

						String activityTime = jo.getString("activity_time");//活动举办时间
						m.setActivityTime(activityTime);

						Long act_id = jo.getLong("act_id");
						m.setAct_id(act_id);
						
						String merchant_name=jo.getString("name");
						if(merchant_name!=null&&merchant_name.length()>0){
							m.setName(merchant_name);
						}
						
						String logo=jo.getString("logo");
						if(logo!=null){
							logo=CONSTANTS.IMG_HOST+logo;
							m.setLogo(logo);
						}
						
						
						String address=jo.getString("address");
						if(address!=null&&address.length()>0){
							m.setAddress(address);
						}
						String xcy=jo.getString("xcy");
						if(xcy!=null&&xcy.length()>0){
							m.setXcy(xcy);
						}else{
							m.setXcy("");
						}
						
						
						String merchantName=jo.getString("merchantName");
						if(merchantName!=null&&merchantName.length()>0){
							m.setMerchantName(merchantName);
						}
						
						
						String pic0=jo.getString("pic0");
						if(pic0!=null&&pic0.length()>5){
							m.setPhoto0(pic0);
						}
						
						
						String pic1=jo.getString("pic1");
						if(pic1!=null&&pic1.length()>5){
							m.setPhoto1(pic1);
						}
						
						String pic2=jo.getString("pic2");
						if(pic2!=null&&pic2.length()>5){
							m.setPhoto1(pic2);
						}
						
						String pic3=jo.getString("pic3");
						if(pic3!=null&&pic3.length()>5){
							m.setPhoto3(pic3);
						}
						
					

					
						
						
					 

					return m;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//附近活动分页
	public static Map pageList(int currentPage,String longitude,String latitude) {
		String url = CONSTANTS.NEARBY_ACTIVITY_PAGE+currentPage+"&longitude="+longitude+"&latitude="+latitude;
		
		List<ActivityPO> list = new ArrayList<ActivityPO>();
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

					ActivityPO m = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jo = (JSONObject) jsonArray.get(i);
						m = new ActivityPO();
						

						String activityTime = jo.getString("activity_time");//活动举办时间
						m.setActivityTime(activityTime);

						Long act_id = jo.getLong("act_id");
						m.setAct_id(act_id);
						
						String merchant_name=jo.getString("name");
						if(merchant_name!=null&&merchant_name.length()>0){
							m.setName(merchant_name);
						}
						
						String pic0=jo.getString("pic0");
						String pic1=jo.getString("pic1");
						String pic2=jo.getString("pic2");
						String pic3=jo.getString("pic3");
						
						
						
						if(pic0!=null&&pic0.length()>5){
							pic0=CONSTANTS.IMG_HOST+pic0;
							m.setLogo(pic0);
						}else if(pic1!=null&&pic1.length()>5){
							pic1=CONSTANTS.IMG_HOST+pic1;
							m.setLogo(pic1);
						}else if(pic2!=null&&pic2.length()>5){
							pic2=CONSTANTS.IMG_HOST+pic2;
							m.setLogo(pic2);
						}else if(pic3!=null&&pic3.length()>5){
							pic3=CONSTANTS.IMG_HOST+pic3;
							m.setLogo(pic3);
						}
						
						
						String address=jo.getString("address");
						if(address!=null&&address.length()>0){
							m.setAddress(address);
						}
						String xcy=jo.getString("xcy");
						if(xcy!=null&&xcy.length()>0){
							m.setXcy(xcy);
						}else{
							m.setXcy("");
						}
						
						
						String merchantName=jo.getString("merchantName");
						if(merchantName!=null&&merchantName.length()>0){
							m.setMerchantName(merchantName);
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

