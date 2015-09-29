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

import com.yedianchina.tools.CONSTANTS;
//消费记录 11-09
public class TransactionLogDao {
	public static Map findMyLogList(int currentPage, String mp) {

		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		HashMap<String,Object> po = null;
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		try {
			String url=CONSTANTS.MY_TRANSACTION_LIST + mp
					+ "&currentPage=" + currentPage;
			request = new HttpPost(new URI(url));
		 
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					JSONObject jsonObj = new JSONObject(out);
					Object listObj = jsonObj.get("list");
					Object sizeObj = jsonObj.get("size");

				 
				 
					JSONArray jsonArray = new JSONArray(listObj.toString());
					Map m = null;

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
						po = new HashMap<String,Object>();
						String balance= jsonObject.getString("balance");
						if(balance!=null&&balance.length()>0){
							po.put("balance", balance);
						}

						String money = jsonObject.getString("money");
						System.out.println("money=" + money);
						if (money != null && money.length() > 0) {
							po.put("money", money);
						}
						String add_time = jsonObject.getString("add_time");

						if (add_time != null) {
							po.put("add_time", add_time);
						}

						

						Long id = jsonObject.getLong("id");
						po.put("recharge_id", id);
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
