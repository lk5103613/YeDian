package com.yedianchina.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.yedianchina.po.City;
import com.yedianchina.tools.CONSTANTS;

public class CityService {
	public List<City> findCityList() {
		try {
			String url =CONSTANTS.CITY_LIST+"?YD_APPID="+CONSTANTS.YD_APPID;

			List<City> list = new ArrayList<City>();
			HttpClient client = new DefaultHttpClient();
			HttpPost request;
         
			request = new HttpPost(new URI(url.toString()));

			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
				 
					
					
					JSONArray jsonArray = new JSONArray(out);
					City po = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						po = new City();
						Long city_id = jsonObject.getLong("city_id");
						System.out.println("city_id="+city_id);
						if (city_id != null) {
							po.setCityId(city_id);
						}
						String city_name = jsonObject.getString("city_name");
						if (city_name != null && city_name.length() > 0) {
							po.setName(city_name);
						}
						list.add(po);
					}

					return list;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

}
