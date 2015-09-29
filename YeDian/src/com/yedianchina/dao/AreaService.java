package com.yedianchina.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.yedianchina.po.Area;
import com.yedianchina.tools.CONSTANTS;



public class AreaService {
	public static List<Area> listArea(String cityId) {
		String url = CONSTANTS.LIST_AREA+cityId;
		List<Area> resultList = new ArrayList<Area>();
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");

					JSONArray jsonArray = new JSONArray(out);
					Area po = new Area();
					po.setAreaId(0);
					po.setAreaName("不限");
					resultList.add(po);
					for (int i = 0; i < jsonArray.length(); i++) {

						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						po = new Area();

						int areaId = jsonObject.getInt("area_id");
						Log.e("name", "catId=" + areaId);
						if (areaId > 0) {
							po.setAreaId(areaId);
						}
						String areaName = jsonObject.getString("area_name");
						if (areaName != null && !"".equals(areaName)) {
							po.setAreaName(areaName);
						}

						resultList.add(po);

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return resultList;

	}

}
