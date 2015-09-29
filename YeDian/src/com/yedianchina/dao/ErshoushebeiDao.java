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

import com.yedianchina.po.ErshoushebeiPO;
import com.yedianchina.tools.CONSTANTS;
//二手设备 06-07 16:59
public class ErshoushebeiDao {
	/**
	 * 
	 * @param currentPage:当前第几页
	 * @param flag 1:二手设备  2:服装   3.培训   4.管理
	 * @return
	 */
	public static Map pageList(int currentPage,int flag) {
		String url = CONSTANTS.ERSHOUSHEBEI_PAGE+currentPage+"&flag="+flag;
		
		
		List<ErshoushebeiPO> list = new ArrayList<ErshoushebeiPO>();
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

					ErshoushebeiPO m = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jo = (JSONObject) jsonArray.get(i);
					
						

						m = new ErshoushebeiPO();
						String name = jo.getString("name");
						m.setName(name);

						String linkmp = jo.getString("linkmp");
						m.setLinkmp(linkmp);

						Long id=jo.getLong("id");
						m.setId(id);
						
						String linkman = jo.getString("linkman");
						if(linkman!=null&&linkman.length()>0){
							m.setLinkman(linkman);
						}
						
						String img1=jo.getString("img1");
						if(img1!=null&&img1.length()>5){
							
							m.setImgurls(img1);
						}
						String price=jo.getString("price");
						if(price!=null&&price.length()>0){
							m.setPrice(price);
						}
					 
						m.setFlag(String.valueOf(flag));
						
						
					
						
						
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
	
	public static ErshoushebeiPO loadErshoushebei(Long id) {

		String url = CONSTANTS.ERSHOUSHEBEI_DETAIL_URL + id;

		ErshoushebeiPO  m=null;
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			
			request = new HttpPost(new URI(url));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("", out);
					JSONObject jo = new JSONObject(out);

				

					
					m = new ErshoushebeiPO();
					String name = jo.getString("name");
					m.setName(name);

					String linkmp = jo.getString("linkmp");
					m.setLinkmp(linkmp);

					
					m.setId(id);
					
					String linkman = jo.getString("linkman");
					if(linkman!=null&&linkman.length()>0){
						m.setLinkman(linkman);
					}
					String imgurls="";
					String img1=jo.getString("img1");
					if(img1!=null&&img1.length()>5){
						imgurls=imgurls+img1;
						imgurls=imgurls+";";
					}
					String img2=jo.getString("img2");
					if(img2!=null&&img2.length()>5){
						imgurls=imgurls+img2;
						imgurls=imgurls+";";
					}
					String img3=jo.getString("img3");
					if(img3!=null&&img3.length()>5){
						imgurls=imgurls+img3;
						imgurls=imgurls+";";
					}
					String img4=jo.getString("img4");
					if(img4!=null&&img4.length()>5){
						imgurls=imgurls+img4;
						imgurls=imgurls+";";
					}
					
					if(imgurls!=null&&imgurls.length()>5){
						m.setImgurls(imgurls);
					}

					
					return m;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
