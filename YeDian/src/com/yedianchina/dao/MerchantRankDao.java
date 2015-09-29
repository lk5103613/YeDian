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

import com.yedianchina.po.MerchantPO;
import com.yedianchina.tools.CONSTANTS;

public class MerchantRankDao {
	private static final String TAG = "MerchantRankDao";

	public static Map pageList(int currentPage) {
		String url = CONSTANTS.MERCHANT_RANK_PAGE + currentPage;
		Map resultMap = new HashMap();
		List<MerchantPO> list = new ArrayList<MerchantPO>();
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {

			request = new HttpPost(new URI(url));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");

					JSONObject _JSONObject = new JSONObject(out);
					JSONArray jsonArray = _JSONObject.getJSONArray("list");

					int allCnt = _JSONObject.getInt("size");

					MerchantPO m = null;
					for (int i = 0; i < jsonArray.length(); i++) {

						JSONObject jo = (JSONObject) jsonArray.get(i);
						m = new MerchantPO();
						String name = jo.getString("name");
						if (name != null && name.length() > 0) {
							m.setName(name);
						}

						String add_time = jo.getString("add_time");
						if (add_time != null) {
							m.setAdd_time(add_time);
						}

						Long merchant_id = jo.getLong("merchant_id");
						if (merchant_id != null) {
							m.setMerchant_id(merchant_id);
						}

						Integer star = jo.getInt("star");
						if (star != null && star > 0) {
							m.setStar(star);
						}

						String logo = jo.getString("logo");
						if (logo != null) {
							logo = CONSTANTS.IMG_HOST + logo;
							m.setLogo(logo);
						}
						String addr = jo.getString("addr");
						if (addr != null && addr.length() > 0) {
							m.setAddr(addr);
						}

						list.add(m);
					}

					resultMap.put("list", list);
					resultMap.put("allCnt", allCnt);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
}
