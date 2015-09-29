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

import com.yedianchina.po.MerchantPO;
import com.yedianchina.po.UserPO;
import com.yedianchina.tools.CONSTANTS;

public class AttentionDao {
	public static Map pageList(int currentPage, Long pk) {
		String url = CONSTANTS.ATTENTION_MERCHANT_PAGE + currentPage + "&pk="
				+ pk;
		
		Log.e("09.30.AttentionDao.pageList", url);

		List<MerchantPO> list = new ArrayList<MerchantPO>();
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			System.out.println("url=="+url);
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

					MerchantPO m = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jo = (JSONObject) jsonArray.get(i);
						m = new MerchantPO();

						String add_time = jo.getString("add_time");
						m.setAdd_time(add_time);

						Long merchant_id = jo.getLong("merchant_id");
						m.setMerchant_id(merchant_id);

						String merchant_name = jo.getString("name");
						if (merchant_name != null && merchant_name.length() > 0) {
							m.setName(merchant_name);
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
						String flag = jo.getString("flag");
						if (flag != null && flag.length() > 0) {
							m.setFlag(flag);
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

	// /////////////////

	public static int nearbyBottomGz(Long uid, Long targetUID) {
		String url = CONSTANTS.NEARBY_USER_BOTTOM_GZ;

		UserPO po = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();

			params.add(new BasicNameValuePair("zhudong_uid", uid + ""));
			params.add(new BasicNameValuePair("beidong_uid", targetUID + ""));
			params.add(new BasicNameValuePair("YD_APPID", CONSTANTS.YD_APPID));

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
					return code;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return -1;

	}

}
