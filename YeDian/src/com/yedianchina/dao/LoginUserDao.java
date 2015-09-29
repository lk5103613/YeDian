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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.yedianchina.po.UserPO;
import com.yedianchina.tools.CONSTANTS;

public class LoginUserDao {
	public UserPO regMp(String mp, String pwd, String userType, String gender) {
		String url = CONSTANTS.REG_MP;

		UserPO po = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		
		Log.e("", "mp＝"+mp+" pwd="+pwd+" userType="+userType+"  gender="+gender);
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();
			params.add(new BasicNameValuePair("mp",mp));
			params.add(new BasicNameValuePair("pwd", pwd));
			params.add(new BasicNameValuePair("userType", userType));
			params.add(new BasicNameValuePair("gender", gender));
			//params.add(new BasicNameValuePair("YD_APPID", CONSTANTS.YD_APPID));

			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					po = new UserPO();
					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("regMp===", out);

					JSONObject jsonObject = new JSONObject(out);

					String _code = jsonObject.getString("code");
					int code = Integer.valueOf(_code);
					po.setCode(code);
					if (code == 7) {//邮箱已经被注册
						return po;
					}
					if (code == 1) {
						String _uid = jsonObject.getString("loginUID");
						po.setMp(mp);
						po.setUserType(Integer.valueOf(userType));
						po.setUid(Long.valueOf(_uid));
						return po;
					}
					return po;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}
	//修改百度uid
	public int updateBaiduUID(Long loginUID,String baidu_uid) {
		String url = CONSTANTS.UPDATE_BAIDUUID;

		UserPO po = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();
			params.add(new BasicNameValuePair("loginUID", loginUID+""));
			
		
			params.add(new BasicNameValuePair("baidu_uid", baidu_uid));
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
					return  code;


				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return -1;

	}
	
	
	public  static JSONArray  updateLastLogin(){
		String url = CONSTANTS.UPDATE_LAST_LOGIN;

	
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
					JSONArray list=new JSONArray(out);
					return list;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	 
	}
	//修改密码
	public int updatePwd(Long loginUID,String oldPwd, String  newPwd) {
		String url = CONSTANTS.UPDATE_PWD;

		UserPO po = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();
			params.add(new BasicNameValuePair("loginUID", loginUID+""));
			
			params.add(new BasicNameValuePair("oldPwd", oldPwd));
			params.add(new BasicNameValuePair("newPwd", newPwd));
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
					return  code;


				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return -1;

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//更新用户最后登陆时间
	public  static JSONArray  updateLastLogin(Long  id){
		String url = CONSTANTS.UPDATE_LAST_LOGIN;

	
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();

			params.add(new BasicNameValuePair("id", id + ""));
			params.add(new BasicNameValuePair("YD_APPID", CONSTANTS.YD_APPID));

			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					 
					String out = EntityUtils.toString(entity, "UTF-8");
					JSONArray list=new JSONArray(out);
					return list;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	 
	}
	public UserPO loadUserInfo(Long uid) {
		String url = CONSTANTS.LOAD_USER_INFO;

		UserPO po = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();

			params.add(new BasicNameValuePair("uid", uid + ""));
			params.add(new BasicNameValuePair("YD_APPID", CONSTANTS.YD_APPID));

			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					po = new UserPO();
					String out = EntityUtils.toString(entity, "UTF-8");

					JSONObject jsonObject = new JSONObject(out);

					Double balance = jsonObject.getDouble("balance");

					po.setBalance(balance);

					return po;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

	public UserPO regEmail(String email, String pwd, String userType, String gender) {
		String url = CONSTANTS.REG_EMAIL;

		UserPO po = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		
		Log.e("", "email"+email+" pwd="+pwd+" userType="+userType+"  gender="+gender);
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();
			params.add(new BasicNameValuePair("email",email));
			params.add(new BasicNameValuePair("pwd", pwd));
			params.add(new BasicNameValuePair("userType", userType));
			params.add(new BasicNameValuePair("gender", gender));
			//params.add(new BasicNameValuePair("YD_APPID", CONSTANTS.YD_APPID));

			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					po = new UserPO();
					String out = EntityUtils.toString(entity, "UTF-8");
					Log.e("regEmail===", out);

					JSONObject jsonObject = new JSONObject(out);

					String _code = jsonObject.getString("code");
					int code = Integer.valueOf(_code);
					po.setCode(code);
					if (code == 7) {//邮箱已经被注册
						return po;
					}
					if (code == 1) {
						String _uid = jsonObject.getString("loginUID");
						po.setMp(email);
						po.setUid(Long.valueOf(_uid));
						po.setUserType(Integer.valueOf(userType));
						return po;
					}
					return po;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

	public UserPO login(String mp, String p) {
		String url = CONSTANTS.LOGIN;

		UserPO po = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost request;
		try {
			request = new HttpPost(new URI(url));
			List params = new ArrayList();

			params.add(new BasicNameValuePair("mp", mp));
			params.add(new BasicNameValuePair("pwd", p));
			params.add(new BasicNameValuePair("YD_APPID", CONSTANTS.YD_APPID));

			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					
					String out = EntityUtils.toString(entity, "UTF-8");
					System.out.println("out=="+out);
					

					JSONObject jo = new JSONObject(out);
					
				    Log.e("login.JSON", jo.toString());
					
					

					String codeStr = jo.getString("code");
					String sql=jo.getString("sql");
					
					Log.e("sql=", sql+"  code="+codeStr);

					if ("1".equals(codeStr)) {
						po = new UserPO();
						Long uid = jo.getLong("id");
						po.setMp(mp);
						po.setCode(1);
						po.setUid(uid);
						int userType=jo.getInt("user_type");
						String  nickname=jo.getString("nickname");
						if(nickname!=null&&nickname.length()>0){
							po.setNickname(nickname);
						}
						po.setUserType(userType);
						
						
						 
						
						 
						
						return po;
					}
					if ("7".equals(codeStr)) {
					return null;
					}
					

					return po;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;

	}

}
