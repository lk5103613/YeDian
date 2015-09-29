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

import com.yedianchina.po.Fee;
import com.yedianchina.tools.CONSTANTS;

public class FeeService {

	public Map findFeeList(int cityId) {
		Map retMap=new HashMap();
		
		try {
			String url =CONSTANTS.FEE_LIST+cityId+"&YD_APPID="+CONSTANTS.YD_APPID;

			List<Fee> list = new ArrayList<Fee>();
			HttpClient client = new DefaultHttpClient();
			HttpPost request;
           
			request = new HttpPost(new URI(url.toString()));
            HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					JSONObject jsonObj = new JSONObject(out);
				    
					
					JSONObject memoJo =jsonObj.getJSONObject("memoList");
					retMap.put("memoJo", memoJo);
					
					JSONArray jsonArray =jsonObj.getJSONArray("list");
					Fee po = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						po = new Fee();
						String val = jsonObject.getString("val");
						System.out.println("val="+val);
						if (val != null) {
							po.setVal(val);
						}
						String start_time = jsonObject.getString("start_time");
						if (start_time != null && start_time.length() > 0) {
							po.setStart_time(start_time);
						}
						String end_time = jsonObject.getString("end_time");
						if (end_time != null && end_time.length() > 0) {
							po.setEnd_time(end_time);
						}
						list.add(po);
					}
					retMap.put("list", list);
					
					
					

					return retMap;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}



}
