package com.easemob.chat.task;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.activity.ShowBigImage;
import com.easemob.chat.activity.ShowVideoActivity;
import com.easemob.chat.utils.ImageCache;
import com.yedianchina.ui.R;
import com.easemob.util.ImageUtils;

public class LoadVideoImageTask extends AsyncTask<Object, Void, Bitmap>{

	private ImageView iv=null;
	String thumbnailPath=null;
	String thumbnailUrl=null;
	Activity activity;
	EMMessage message;
	  
	
	@Override
	protected Bitmap doInBackground(Object... params) {
		thumbnailPath=(String)params[0];
		thumbnailUrl=(String)params[1];
		iv=(ImageView)params[2];
		activity=(Activity)params[3];
		message=(EMMessage) params[4];
		
		return ImageUtils.decodeScaleImage(thumbnailPath, 120, 120);
	}
	
	
	
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		if(result!=null)
		{
			iv.setImageBitmap(result);
			ImageCache.getInstance().put(thumbnailPath, result);
			iv.setClickable(true);
			iv.setTag(thumbnailPath);
			iv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(thumbnailPath!=null)
					{
						VideoMessageBody videoBody=(VideoMessageBody) message.getBody();
						Intent intent=new Intent(activity, ShowVideoActivity.class);
						File file=new File(videoBody.getLocalUrl());
						if(file!=null&&file.exists())
						{
							Uri uri=Uri.fromFile(file);
							intent.putExtra("uri", uri);
						}else{
							intent.putExtra("remotepath", videoBody.getRemoteUrl());
							intent.putExtra("secret", videoBody.getSecret());
						}
						if(message!=null&&message.direct==EMMessage.Direct.RECEIVE&&!message.isAcked)
						{
							message.isAcked=true;
							try {
								EMChatManager.getInstance().ackMessageRead(message.getFrom(),message.getMsgId());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						activity.startActivity(intent);
						
					}
					
					
					
					
				}
			});
			 
		}else{
			iv.setImageResource(R.drawable.default_image);
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
