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

import com.yedianchina.po.JobsPO;
import com.yedianchina.tools.CONSTANTS;

public class JobsDao {
	public static Long saveJob(JobsPO po){
		try{
		String url=CONSTANTS.SAVE_JOB;
		HttpPost request = new HttpPost(new URI(url));
		List params = new ArrayList();
		params.add(new BasicNameValuePair("reqJobName", po.getReqJobName()));
		
		params.add(new BasicNameValuePair("desc", po.getDesc()));
		params.add(new BasicNameValuePair("gender", po.getGender()));
		
		params.add(new BasicNameValuePair("pic0", po.getPic0()));
		params.add(new BasicNameValuePair("pic1", po.getPic1()));
		params.add(new BasicNameValuePair("pic2", po.getPic2()));
		params.add(new BasicNameValuePair("pic3", po.getPic3()));
		
		
		params.add(new BasicNameValuePair("age", po.getAge()));
	
		
		params.add(new BasicNameValuePair("job_type", po.getJob_type()));
		params.add(new BasicNameValuePair("linkman", po.getLinkman()));
		
		params.add(new BasicNameValuePair("mp", po.getMp()));
		params.add(new BasicNameValuePair("email", po.getEmail()));
		
	
		params.add(new BasicNameValuePair("YD_APPID", CONSTANTS.YD_APPID));
		HttpClient client = new DefaultHttpClient();

		request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		HttpResponse response = client.execute(request);
		
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String out = EntityUtils.toString(entity, "UTF-8");
			 

				JSONObject jo = new JSONObject(out);
				return  jo.getLong("pk");
			}
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0L;
		
	}
	
	
	
	
	
	public static JobsPO loadJobs(Long jobId) {

		String url = CONSTANTS.JOBS_DETAIL_URL + jobId;

		JobsPO m = null;

		
		m = new JobsPO();
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

					
					String reqJobName = jo.getString("reqJobName");
					m.setReqJobName(reqJobName);

					String addTime = jo.getString("addTime");
					m.setAddTime(addTime);

					String id = jo.getString("id");
					m.setId(id);
					String trueName = jo.getString("trueName");
					if (trueName != null && trueName.length() > 0) {
						m.setTrueName(trueName);
					}
					String avatar = jo.getString("avatar");
					if (avatar != null) {
						avatar = CONSTANTS.IMG_HOST +"ios/"+ avatar;
						m.setAvatar(avatar);
					}
					
					String pic0 = jo.getString("pic0");
					if (pic0 != null&&pic0.length()>5) {
						
						m.setPic0(pic0);
						
						Log.e("getPic0", m.getPic0());
					}
					//
					String pic1 = jo.getString("pic1");
					if (pic1 != null&&pic1.length()>5) {
					
						m.setPic1(pic1);
					}
					//
					String pic2 = jo.getString("pic2");
					if (pic2 != null&&pic2.length()>5) {
					
						m.setPic2(pic2);
					}
					//
					String pic3 = jo.getString("pic3");
					if (pic3 != null&&pic3.length()>5) {
						
						m.setPic3(pic3);
					}
					String desc=jo.getString("desc");
					if(desc!=null){
						m.setDesc(desc);
					}
					String  mp=jo.getString("mp");
					if(mp!=null&&mp.length()>0){
						m.setMp(mp);
					}
					String email=jo.getString("email");
					if(email!=null&&email.length()>5){
						m.setEmail(email);
					}
					String  linkman=jo.getString("linkman");
					if(linkman!=null){
						m.setLinkman(linkman);
					}
					
					String age=jo.getString("age");
					if(age!=null&&age.length()>0){
						  m.setAge(age);
					}
					String  job_type=jo.getString("job_type");
					if(job_type!=null&&job_type.length()>0){
						m.setJob_type(job_type);
					}
					

					
					return m;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;

	}

	public static Map pageList(int currentPage,int job_type) {
		String url = CONSTANTS.JOBS_PAGE + currentPage+"&job_type="+job_type;

		List<JobsPO> list = new ArrayList<JobsPO>();
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

					JobsPO m = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jo = (JSONObject) jsonArray.get(i);
						m = new JobsPO();
						String reqJobName = jo.getString("reqJobName");
						m.setReqJobName(reqJobName);

						String addTime = jo.getString("addTime");
						if(addTime!=null&&addTime.length()>=10){
							addTime=addTime.substring(0,10);
							m.setAddTime(addTime);
						}
						

						String id = jo.getString("id");
						m.setId(id);
						String trueName = jo.getString("trueName");
						if (trueName != null && trueName.length() > 0) {
							m.setTrueName(trueName);
						}
						String pic0 = jo.getString("pic0");
						String pic1 = jo.getString("pic1");
						String pic2 = jo.getString("pic2");
						String pic3 = jo.getString("pic3");
						if (pic0 != null) {
							pic0 = CONSTANTS.IMG_HOST +"ios/"+pic0;
							m.setAvatar(pic0);
						}else if(pic1!=null&&pic1.length()>5){
							pic1 = CONSTANTS.IMG_HOST +"ios/"+pic1;
							m.setAvatar(pic1);
						}else if(pic2!=null&&pic2.length()>5){
							pic2 = CONSTANTS.IMG_HOST +"ios/"+pic2;
							m.setAvatar(pic2);
						}else if(pic3!=null&&pic3.length()>5){
							pic3 = CONSTANTS.IMG_HOST +"ios/"+pic3;
							m.setAvatar(pic3);
						}
						
						String age=jo.getString("age");
						if(age!=null&&age.length()>0){
							m.setAge(age);
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
