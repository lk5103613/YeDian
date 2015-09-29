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

import com.yedianchina.po.Driver;
import com.yedianchina.po.MerchantPO;
import com.yedianchina.tools.CONSTANTS;

public class MerchantDao {
	//商家收藏用户求职信息
	public static int   favJobReq(Long uid,Long jobReqId){
		try {
			StringBuilder url = new StringBuilder(CONSTANTS.FAV_JOBS_DETAIL_URL);
			 
			 

			 
			HttpClient client = new DefaultHttpClient();
			HttpPost request;

		 
			request = new HttpPost(new URI(url.toString()));

			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
				 

					JSONObject jo = new JSONObject(out);
					return  jo.getInt("code");
					 

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		
		return -1;
	}
	
	
	public static MerchantPO loadMerchantInfo(long merchantId) {
		try {
			StringBuilder url = new StringBuilder(CONSTANTS.MERCHANT_DETAIL_URL);
			url.append(merchantId);
			 

			MerchantPO po = null;
			HttpClient client = new DefaultHttpClient();
			HttpPost request;

		 
			request = new HttpPost(new URI(url.toString()));

			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("loadMerchantInfo******", out);
				 

					JSONObject jsonObject = new JSONObject(out);
					po = new MerchantPO();
					String name = jsonObject.getString("name");
				
					if (name != null && name.length() > 0) {
						po.setName(name);
					}else{
						po.setName("");
					}
					
					Object _star = jsonObject.get("star");

					if (_star!=null) {
						int star=jsonObject.getInt("star");
						po.setStar(star);
					}else{
						po.setStar(3);
					}

//					String longi = jsonObject.getString("longi");
//					if (longi != null && longi.length() > 0) {
//						po.setLongi(longi);
//					}
//					String lanti = jsonObject.getString("lanti");
//					if (lanti != null && lanti.length() > 0) {
//						po.setLanti(lanti);
//					}
					
					
					
					String logo=jsonObject.getString("logo");
					if(logo!=null&&logo.length()>0){
						logo=CONSTANTS.IMG_HOST+logo;
				 
						po.setLogo(logo);
					}else{
						po.setLogo("");
					}
					
					String mp=jsonObject.getString("mp");
					String tel=jsonObject.getString("tel");
					if(tel!=null&&tel.length()>=7){
						po.setTel(tel);
					}else{
						po.setTel("");
					}
					
					if(mp!=null&&mp.length()>0){
						
						po.setMp(mp);
					}
					
					
					Long merchant_id=jsonObject.getLong("merchant_id");
					po.setMerchant_id(merchant_id);
					
					String desc=jsonObject.getString("desc");
					if(desc!=null){
						po.setDesc(desc);
					}else{
						po.setDesc("");
					}
					
					String addr=jsonObject.getString("addr");
					if(addr!=null&&addr.length()>2){
						po.setAddr(addr);
					}else{
						po.setAddr("");
					}
					

					return po;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}
	
	public Map findNearByMerchant(double longi, double lanti, int distance) {
		try {
			StringBuilder url = new StringBuilder(CONSTANTS.NEARBY_DRIVER_URL
					+ "?longi=" + longi + "&lanti=" + lanti+"&YD_APPID="+CONSTANTS.YD_APPID);

			List<Driver> list = new ArrayList<Driver>();
			HttpClient client = new DefaultHttpClient();
			HttpPost request;

		 
			request = new HttpPost(new URI(url.toString()));

			
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
				 Log.e("输出", out);
					
					
					JSONObject jo = new JSONObject(out);
					Driver po = null;
					JSONArray jsonArray=jo.getJSONArray("list");
					Integer DRIVER_PERIOD=jo.getInt("DRIVER_PERIOD");
					
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						po = new Driver();
						String truename = jsonObject.getString("truename");
						System.out.println("truename="+truename);
						if (truename != null && truename.length() > 0) {
							po.setTruename(truename);
						}
						int star = jsonObject.getInt("star");

						if (star > 0) {
							po.setStar(star);
						}

						String driverLongi = jsonObject.getString("longi");
						if (driverLongi != null && driverLongi.length() > 0) {
							po.setLongi(driverLongi);
						}
						String driverLanti = jsonObject.getString("lanti");
						if (driverLanti != null && driverLanti.length() > 0) {
							po.setLanti(driverLanti);
						}
						String workYear=jsonObject.getString("work_year");
						if (workYear!=null) {
							po.setWorkYear(new Integer(workYear));
						}else{
							po.setWorkYear(0);
						}
						Long driverId=jsonObject.getLong("driver_id");
						po.setDriverId(driverId);
						String avatar=jsonObject.getString("avatar");
						if(avatar!=null&&avatar.length()>0){
							avatar=CONSTANTS.IMG_HOST+avatar;
							 
							po.setAvatar(avatar);
						}
						String nativePlace=jsonObject.getString("native_place");
						if(nativePlace!=null&&nativePlace.length()>0){
							po.setNativePlace(nativePlace);
						}
						String drivingLicence=jsonObject.getString("driving_licence");
						if(drivingLicence!=null&&drivingLicence.length()>0){
							po.setDrivingLicence(drivingLicence);
						}
						int status=jsonObject.getInt("status");
						po.setStatus(status);
						
						String juli=jsonObject.getString("juli");
						if(juli!=null&&juli.length()>0){
						  Log.e("juli", "juli="+juli);
						  po.setJuli(juli);
						}

						list.add(po);
					}
					
					Map retMap=new HashMap();
					retMap.put("list", list);
					retMap.put("DRIVER_PERIOD", DRIVER_PERIOD);

					return retMap;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

}
