package net.sourceforge.simcpux;

import net.sourceforge.simcpux.uikit.CameraUtil;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXAppExtendObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.yedianchina.dao.AppConfigDao;
import com.yedianchina.ui.R;

public class SendToWXActivity extends Activity {
	
	
	
	Handler loginHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			Log.e("msg.what", "what.................=" + what);
			if (what == 1) {
				initView();

			}

		};
	};


	private static final int THUMB_SIZE = 150;

	private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	private IWXAPI api;
	private static final int MMAlertSelect1  =  0;
	private static final int MMAlertSelect2  =  1;
	private static final int MMAlertSelect3  =  2;

	private CheckBox isTimelineCb;
    String shareMsg;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
	
		
		setContentView(R.layout.send_to_wx);
		TextView titleTxtView = (TextView) findViewById(R.id.tvHeaderTitle);
		titleTxtView.setText("微信朋友圈分享");
		
		TextView back_btn = (TextView) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SendToWXActivity.this.finish();
			}
		});
		
		new Thread() {
			public void run() {
				AppConfigDao  dao=new AppConfigDao();
				shareMsg=dao.getWechatInvite();
				Log.e("SendToWXActivity", "shareMsg="+shareMsg);
					loginHandler.sendEmptyMessage(1);
				

			}
		}.start();
		
		
	}

	private void initView() {

		EditText contentEt= (EditText) findViewById(R.id.contentEt);
		contentEt.setText(shareMsg);
 
		
		
		TextView tmp=(TextView)findViewById(R.id.loginBtn);
		tmp.setVisibility(View.INVISIBLE);
		
		TextView cancelSuggestTv=(TextView)findViewById(R.id.cancelSuggestTv);
		cancelSuggestTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SendToWXActivity.this.finish();
			}
		});
		
		TextView btn=(TextView)findViewById(R.id.sendSuggest);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
								
				Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.logo200);
						String text =shareMsg;
//						if (text == null || text.length() == 0) {
//							return;
//						}
						
					
						WXTextObject textObj = new WXTextObject();
						textObj.text = "哈我最新使用来夜店中国，里面信息量大 招聘 求职 交友什么都有，有空你也来玩～";

						Bitmap thumbBmp = Bitmap.createScaledBitmap(bm, THUMB_SIZE, THUMB_SIZE, true);
						

						WXMediaMessage msg = new WXMediaMessage();
						msg.mediaObject = textObj;
						msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // 设置缩略图
 
						msg.title ="夜店中国";
						msg.description =shareMsg;

					
						SendMessageToWX.Req req = new SendMessageToWX.Req();
					
						req.transaction ="夜店中国";
						req.message = msg;
						req.scene = SendMessageToWX.Req.WXSceneTimeline;
						
						
						api.sendReq(req);
						finish();
					}
			
			
		});

		
		
		
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case 0x101: {
			final WXAppExtendObject appdata = new WXAppExtendObject();
			final String path = CameraUtil.getResultPhotoPath(this, data, SDCARD_ROOT + "/tencent/");
			appdata.filePath = path;
			appdata.extInfo = "this is ext info";

			final WXMediaMessage msg = new WXMediaMessage();
			msg.setThumbImage(Util.extractThumbNail(path, 150, 150, true));
			msg.title = "this is title";
			msg.description = "this is description";
			msg.mediaObject = appdata;
			
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = buildTransaction("appdata");
			req.message = msg;
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
			api.sendReq(req);
			
			finish();
			break;
		}
		default:
			break;
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
}
