package com.yedianchina.tools;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class UploadUtil {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10*1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    
    
    public static String post(File file,String urlServer,String  serverImgName) throws ClientProtocolException, IOException, JSONException {
        HttpClient httpclient = new DefaultHttpClient();
        //设置通信协议版本
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
         
        //File path= Environment.getExternalStorageDirectory(); //取得SD卡的路径
         
        //String pathToOurFile = path.getPath()+File.separator+"ak.txt"; //uploadfile
        //String urlServer = "http://192.168.1.88/test/upload.php"; 
         
        HttpPost httppost = new HttpPost(urlServer);
//        File file = new File(pathToOurFile);
     
        MultipartEntity mpEntity = new MultipartEntity(); //文件传输
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("filename", cbFile); // <input type="file" name="userfile" />  对应的
        //
        ContentBody cbStr=new StringBody(serverImgName);
        mpEntity.addPart("serverImgName", cbStr);
        //
        
     
     
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
         
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
     
        //System.out.println(response.getStatusLine());//通信Ok
        String json="";
        String path="";
        if (resEntity != null) {
        	
        	
          //System.out.println(EntityUtils.toString(resEntity,"UTF-8"));
          //json=EntityUtils.toString(resEntity,"utf-8");
        
        }
        if (resEntity != null) {
          resEntity.consumeContent();
        }
     
        httpclient.getConnectionManager().shutdown();
        return path;
      }
    
    
   
}
