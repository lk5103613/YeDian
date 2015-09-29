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
import com.yedianchina.po.RecruitPO;
import com.yedianchina.tools.CONSTANTS;

public class RecruitDao {
	public static Long saveRecruit(RecruitPO po) {
		try {
			String url = CONSTANTS.SAVE_RECRUIT;
			HttpPost request = new HttpPost(new URI(url));
			List params = new ArrayList();
			params.add(new BasicNameValuePair("title", po.getTitle()));

			params.add(new BasicNameValuePair("job_desc", po.getJob_desc()));
			params.add(new BasicNameValuePair("merchant_name", po
					.getMerchant_name()));

			params.add(new BasicNameValuePair("pic0", po.getPic0()));
			params.add(new BasicNameValuePair("pic1", po.getPic1()));
			params.add(new BasicNameValuePair("pic2", po.getPic2()));
			params.add(new BasicNameValuePair("pic3", po.getPic3()));

			params.add(new BasicNameValuePair("email", po.getEmail()));

			params.add(new BasicNameValuePair("tj", po.getTj()));

			params.add(new BasicNameValuePair("recruit_type", po
					.getRecruit_type()));
			params.add(new BasicNameValuePair("linkman", po.getLinkman()));

			params.add(new BasicNameValuePair("mp", po.getMp()));
			params.add(new BasicNameValuePair("addr", po.getAddr()));

			params.add(new BasicNameValuePair("job_desc", po.getJob_desc()));

			params.add(new BasicNameValuePair("YD_APPID", CONSTANTS.YD_APPID));
			HttpClient client = new DefaultHttpClient();

			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");

					System.out.println("out==" + out);

					JSONObject jo = new JSONObject(out);
					return jo.getLong("pk");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;

	}

	public static int favRecruitReq(Long uid, Long recruit_id) {
		try {
			StringBuilder url = new StringBuilder(
					CONSTANTS.FAV_RECRUIT_DETAIL_URL + recruit_id + "&uid="
							+ uid);

			HttpClient client = new DefaultHttpClient();
			HttpPost request;

			request = new HttpPost(new URI(url.toString()));

			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");

					JSONObject jo = new JSONObject(out);
					return jo.getInt("code");

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		return -1;
	}

	public static MerchantPO loadMerchant(Long merchantId) {
		String url = CONSTANTS.MERCHANT_DETAIL + merchantId;

		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			Log.e("loadMerchant=", url);
			request = new HttpPost(new URI(url));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("", out);
					JSONObject jo = new JSONObject(out);

					MerchantPO m = null;

					m = new MerchantPO();
					String name = jo.getString("name");// 商户名称
					m.setName(name);

					String longi = jo.getString("longi");
					m.setLongi(longi);

					String lanti = jo.getString("lanti");
					if (lanti != null && lanti.length() > 0) {
						m.setLanti(lanti);
					}
					String mp = jo.getString("mp");
					if (mp != null) {
						m.setMp(mp);
					}
					String addr = jo.getString("addr");
					if (addr != null && addr.length() > 0) {
						m.setAddr(addr);
					}
					String desc = jo.getString("desc");// 商家简介
					if (desc != null && desc.length() > 0) {
						m.setDesc(desc);
					} else {
						m.setDesc("暂无介绍");
					}

					String email = jo.getString("email");
					if (email != null && email.length() > 0) {
						m.setEmail(email);
					}
					String linkman = jo.getString("linkman");
					if (linkman != null && linkman.length() > 0) {
						m.setLinkman(linkman);
					}

					return m;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static RecruitPO loadRecruit(Long recruit_id) {
		String url = CONSTANTS.RECRUIT_DETAIL + recruit_id;

		List<RecruitPO> list = new ArrayList<RecruitPO>();
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

					RecruitPO m = null;

					m = new RecruitPO();
					String title = jo.getString("title");
					m.setTitle(title);

					String add_time = jo.getString("add_time");
					m.setAdd_time(add_time);

					Long id = jo.getLong("recruit_id");
					m.setId(id);

					String mp = jo.getString("mp");
					if (mp != null) {
						m.setMp(mp);
					}
					String addr = jo.getString("addr");
					if (addr != null && addr.length() > 0) {
						m.setAddr(addr);
					}

					//
					String tj = jo.getString("tj");
					if (tj != null && tj.length() > 0) {
						m.setTj(tj);
					}

					String email = jo.getString("email");
					if (email != null && email.length() > 0) {
						m.setEmail(email);
					}
					String linkman = jo.getString("linkman");
					if (linkman != null && linkman.length() > 0) {
						m.setLinkman(linkman);
					}

					String pic0 = jo.getString("pic0");
					if (pic0 != null && pic0.length() > 0) {
						m.setPic0(pic0);
					}

					String pic1 = jo.getString("pic1");
					if (pic1 != null && pic1.length() > 0) {
						m.setPic1(pic1);
					}

					String pic2 = jo.getString("pic2");
					if (pic2 != null && pic2.length() > 0) {
						m.setPic2(pic2);
					}

					String pic3 = jo.getString("pic3");
					if (pic3 != null && pic3.length() > 0) {
						m.setPic3(pic3);
					}

					return m;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map pageList(int currentPage, int recruit_type) {
		String url = CONSTANTS.RECRUIT_PAGE + currentPage + "&recruit_type="
				+ recruit_type;

		List<RecruitPO> list = new ArrayList<RecruitPO>();
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
					Log.e("pageList", out);
					JSONObject _JSONObject = new JSONObject(out);
					JSONArray jsonArray = _JSONObject.getJSONArray("list");
					int allCnt = _JSONObject.getInt("size");

					RecruitPO m = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jo = (JSONObject) jsonArray.get(i);
						m = new RecruitPO();
						String title = jo.getString("title");
						m.setTitle(title);

						String add_time = jo.getString("add_time");
						m.setAdd_time(add_time);

						Long id = jo.getLong("recruit_id");
						m.setId(id);
						String merchant_name = jo.getString("merchant_name");
						if (merchant_name != null && merchant_name.length() > 0) {
							m.setMerchant_name(merchant_name);
						}
						String pic0 = jo.getString("pic0");
						String pic1 = jo.getString("pic1");
						String pic2 = jo.getString("pic2");
						String pic3 = jo.getString("pic3");

						if (pic0 != null && pic0.length() >= 5) {
							pic0 = CONSTANTS.IMG_HOST +"ios/"+pic0;
							Log.e("logo**********pic0", pic0);

							m.setLogo(pic0);
						} else if (pic1 != null && pic1.length() >= 5) {
							pic1 = CONSTANTS.IMG_HOST +"ios/"+ pic1;
							Log.e("logo**********pic1", pic1);

							m.setLogo(pic1);
						}
						else if (pic2 != null && pic2.length() >= 5) {
							pic2 = CONSTANTS.IMG_HOST +"ios/"+ pic2;
							Log.e("logo**********pic2", pic2);

							m.setLogo(pic2);
						}else if (pic3 != null && pic3.length() >= 5) {
							pic3 = CONSTANTS.IMG_HOST +"ios/"+ pic3;
							Log.e("logo**********pic3", pic3);

							m.setLogo(pic3);
						}
						
						
						
						

						String addr = jo.getString("addr");
						if (addr != null && addr.length() > 0) {
							m.setAddr(addr);
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
