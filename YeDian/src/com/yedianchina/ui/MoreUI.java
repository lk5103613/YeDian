package com.yedianchina.ui;

import java.io.InputStream;
import java.text.SimpleDateFormat;

import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.GetFromWXActivity;
import net.sourceforge.simcpux.ShowFromWXActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.activity.MainActivity;
import com.sina.weibo.sdk.WeiboSDK;
import com.sina.weibo.sdk.api.BaseResponse;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.sina.weibo.sdk.api.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.IWeiboHandler;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.utils.Util;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.WXAppExtendObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.demo.ConstantS;

import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.sso.SsoHandler;
import com.yedianchina.dao.VersionService;
import com.yedianchina.po.AppVersion;
import com.yedianchina.service.UpdateService;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.i.I;
import com.yedianchina.ui.i.IMingpianUI;
import com.yedianchina.ui.index.IndexUI;
import com.yedianchina.ui.more.FeedBackUI;
import com.yedianchina.ui.nearby.NearbyModule;
//更多功能
public class MoreUI extends CommonActivity implements IWXAPIEventHandler,IWeiboHandler.Response{
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	       if (keyCode == KeyEvent.KEYCODE_BACK)
			{
	    	   new AlertDialog.Builder(this).setTitle("夜店中国").setMessage("是否退出夜店中国？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					
						if(true) {
							YeDianChinaApplication.getInstance().exit();
						}
						}
				}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).show();
			}
			return false;
		}
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    private IWeiboAPI mWeiboAPI;
    private void ininWeiboSDK() {
        // 初始化SDK
        mWeiboAPI = WeiboSDK.createWeiboAPI(this, ConstantS.APP_KEY);
        mWeiboAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
            @Override
            public void onCancel() {
                Toast.makeText(MoreUI.this, "取消下载", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
  

   
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			goToGetMsg();		
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

 
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
	
	private void goToGetMsg() {
		Intent intent = new Intent(this, GetFromWXActivity.class);
		intent.putExtras(getIntent());
		startActivity(intent);
		finish();
	}
	
	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
		WXMediaMessage wxMsg = showReq.message;		
		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
		
		StringBuffer msg = new StringBuffer();  
		msg.append("description: ");
		msg.append(wxMsg.description);
		msg.append("\n");
		msg.append("extInfo: ");
		msg.append(obj.extInfo);
		msg.append("\n");
		msg.append("filePath: ");
		msg.append(obj.filePath);
		
		Intent intent = new Intent(this, ShowFromWXActivity.class);
		intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
		intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
		intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
		startActivity(intent);
		finish();
	}
	
 
	private int serverVersion;
	private YeDianChinaApplication myApplication;
	private String url;
	
	protected void onResume() {
		if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 }
		super.onResume();
	}
  
    private String tel400;
    private String sms;
  
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.more);
        
        TextView more_feedback=(TextView)this.findViewById(R.id.more_feedback);
        if(more_feedback!=null){
        	more_feedback.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(MoreUI.this, FeedBackUI.class);

					MoreUI.this.startActivity(intent);
					
				}
			});
        }
      
        //版本更新
        TextView more_update=(TextView)this.findViewById(R.id.checkVersionTv);
        if(more_update!=null){
        	more_update.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					new Thread() {
						@Override
						public void run() {
							// 你要执行的方法
							AppVersion po=VersionService.getLatestVersion();
							serverVersion=po.getVersion();
							if(po!=null&&po.getUrl()!=null){
							   url=po.getUrl();
							}
							Message msg=new Message();
							msg.what=1;
							verHandler.sendMessage(msg);
						}
					}.start();
					
				}
			});
        }
           
        
        
        TextView  tvHeaderTitle=(TextView)this.findViewById(R.id.NavigateTitle);
        tvHeaderTitle.setText("更多功能");
        
        
        
		
        
     
		
        parentControl();
       
   
        YeDianChinaApplication.getInstance().addActivity(this); 
    }
	Handler  verHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {

				int what = msg.what;
				if (what == 1) {
					 checkVersion(url);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
	};
  
  
    private IWXAPI api;
  
	/***
	 * 检查是否更新版本
	 */
	public void checkVersion(final String url) {
		myApplication = (YeDianChinaApplication) getApplication();
		
		if (myApplication.localVersion <this.serverVersion) {
		 
			// 发现新版本，提示用户更新
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("软件升级")
					.setMessage("发现新版本,建议立即更新使用.")
					.setPositiveButton("更新",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Intent updateIntent = new Intent(
											MoreUI.this,
											UpdateService.class);
									
									updateIntent.putExtra(
											"app_name",
											getResources().getString(
													R.string.app_name));
									updateIntent.putExtra(
											"apk_download_url",
											url);
									
									startService(updateIntent);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent mainIntent = new Intent(MoreUI.this, IndexUI.class);
									MoreUI.this.startActivity(mainIntent);
									MoreUI.this.finish();
								}
							});
			alert.create().show();

		}else{
			Toast.makeText(getApplicationContext(), "当前已经是最新版本!",
				     Toast.LENGTH_SHORT).show();
		}
	}
    //
	private void parentControl() {
		// 获取密度
		this.getDensity();

	 
		imageViewIndex = (ImageView) findViewById(R.id.menu_home_img);
		imageViewIndex.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 
				startActivity(new Intent(getApplicationContext(),
						IndexUI.class));
				MoreUI.this.finish();
				
			}
		});
	 

		imageViewType = (ImageView) findViewById(R.id.menu_brand_img);
		imageViewType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(),
						NearbyModule.class));
				
				MoreUI.this.finish();
			}
		});

		imageViewShooping = (ImageView) findViewById(R.id.menu_my_letao_img);
		imageViewShooping.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			
				
			
				Log.e("ImageViewMyLetao", "聊天---");
				SharedPreferences preferences = getSharedPreferences(
						CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);
				Long uid = preferences.getLong(CONSTANTS.UID, 0);
				if(uid==null||uid==0){
					Toast.makeText(getApplicationContext(),
							"您尚未登录,请先登录", 1).show();
					startActivity(new Intent(getApplicationContext(), LoginUI.class));
					return;
				}
				
				

				String myNickname = preferences.getString(CONSTANTS.NICKNAME, "");
				if (myNickname == null || myNickname.length() == 0
						|| "".equals(myNickname)) {
					Toast.makeText(getApplicationContext(),
							"您的资料不完善，请去“我的中心－》右上角我的名片处完善个人资料 ", 1).show();
					
					startActivity(new Intent(getApplicationContext(), IMingpianUI.class));

				} else {
				 
					
					startActivity(new Intent(getApplicationContext(),
							I.class));
					MoreUI.this.finish();
				}
				
				

			
				
			}
		});
		

		imageViewMyLetao = (ImageView) findViewById(R.id.menu_shopping_cart_img);
		imageViewMyLetao.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				intent=new Intent();
				
				startActivity(new Intent(getApplicationContext(),
						I.class));
				MoreUI.this.finish();
				
			}
		});

//		imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
//		imageViewMore.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
////				intent=new Intent();
////			 
////				intent.setClass(getApplicationContext(), MoreUI.class);
////				NearbyModule.this.finish();
//			}
//		});

	}
    private void getDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		density = metrics.density;
	}
    private float density = 0;
private TextView mText;
    
    public static Oauth2AccessToken accessToken;
    
    public static final String TAG = "sinasdk";
    
    class AuthDialogListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
        	
        	String code = values.getString("code");
        	if(code != null){
	        	//mText.setText("取得认证code: \r\n Code: " + code);
	        	//Toast.makeText(MoreUI.this, "认证code成功", Toast.LENGTH_SHORT).show();
	        	return;
        	}
            String token = values.getString("access_token");
            String expires_in = values.getString("expires_in");
            com.weibo.sdk.android.demo.MainActivity.accessToken = new Oauth2AccessToken(token, expires_in);
            if (com.weibo.sdk.android.demo.MainActivity.accessToken.isSessionValid()) {
            	 String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                 .format(new java.util.Date(mAccessToken.getExpiresTime()));
//         mText.setText("认证成功: \r\n access_token: " + token + "\r\n" + "expires_in: "
//                 + expires_in + "\r\n有效期：" + date);

         AccessTokenKeeper.keepAccessToken(MoreUI.this, mAccessToken);
         //Toast.makeText(MoreUI.this, "认证成功", Toast.LENGTH_SHORT).show();
      
         
      // 创建微博对外接口实例
 		mWeiboAPI = WeiboSDK.createWeiboAPI(MoreUI.this, ConstantS.APP_KEY);
 		mWeiboAPI.responseListener(getIntent(), MoreUI.this);
 		 
 				System.out.println("Thread is running.");
 				mWeiboAPI.registerApp();
         
         reqMsg(true, true, false,
        		 false, false,
        		 false);
                
                
            }
        }

        @Override
        public void onError(WeiboDialogError e) {
            Toast.makeText(getApplicationContext(),
                    "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
//            Toast.makeText(getApplicationContext(), "Auth cancel",
//                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getApplicationContext(),
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }
    private SsoHandler mSsoHandler;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // SSO 授权回调
        // 重要：发起 SSO 登陆的Activity必须重写onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
    
    private void reqMsg(boolean hasText, boolean hasImage, boolean hasWebpage,
			boolean hasMusic, boolean hasVedio, boolean hasVoice) {

		if (mWeiboAPI.isWeiboAppSupportAPI()) {
			Toast.makeText(this, "当前微博版本支持SDK分享", Toast.LENGTH_SHORT).show();

			Toast.makeText(this, "当前微博版本只支持单条消息分享", Toast.LENGTH_SHORT).show();
			reqSingleMsg(hasText, hasImage, hasWebpage, hasMusic, hasVedio/*
																		 * ,
																		 * hasVoice
																		 */);

		} else {
			Toast.makeText(this, "当前微博版本不支持SDK分享", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 第三方应用发送请求消息到微博，唤起微博分享界面。 当isWeiboAppSupportAPI() < 10351 只支持分享单条消息，即
	 * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
	 * 
	 * @param hasText
	 *            分享的内容是否有文本
	 * @param hasImage
	 *            分享的内容是否有图片
	 * @param hasWebpage
	 *            分享的内容是否有网页
	 * @param hasMusic
	 *            分享的内容是否有音乐
	 * @param hasVideo
	 *            分享的内容是否有视频
	 */
	private void reqSingleMsg(boolean hasText, boolean hasImage,
			boolean hasWebpage, boolean hasMusic, boolean hasVideo/*
																 * , boolean
																 * hasVoice
																 */) {

		// 1. 初始化微博的分享消息
		// 用户可以分享文本、图片、网页、音乐、视频中的一种
		WeiboMessage weiboMessage = new WeiboMessage();
		if (true) {
			weiboMessage.mediaObject = this.getTextObj();
		}
//		if (true) {
//			weiboMessage.mediaObject = getImageObj();
//		}
//
//		if (true) {
//			weiboMessage.mediaObject = getWebpageObj();
//		}
//		if (hasMusic) {
//			weiboMessage.mediaObject = getMusicObj();
//		}
//		if (hasVideo) {
//			weiboMessage.mediaObject = getVideoObj();
//		}
		/*
		 * if (hasVoice) { weiboMessage.mediaObject = getVoiceObj(); }
		 */

		// 2. 初始化从第三方到微博的消息请求
		SendMessageToWeiboRequest req = new SendMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = weiboMessage;
	 

		// 3. 发送请求消息到微博，唤起微博分享界面
		mWeiboAPI.sendRequest(this, req);
	}

	private String getActionUrl() {
		return "http://sina.com?eet" + System.currentTimeMillis();
	}

	/**
	 * 文本消息构造方法。
	 * 
	 * @return 文本消息对象。
	 */
	private TextObject getTextObj() {
		TextObject textObject = new TextObject();
		textObject.text =this.sms;
		//textObject.description=this.sms;
		InputStream inputStream = getResources().openRawResource(
				R.drawable.logo200);

		BitmapDrawable bitmapDrawable = new BitmapDrawable(inputStream);
		Bitmap bitmap=bitmapDrawable.getBitmap();  
		textObject.setThumbImage(bitmap);
		return textObject;
	}

	/**
	 * 图片消息构造方法。
	 * 
	 * @return 图片消息对象。
	 */
	private ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();

		InputStream inputStream = getResources().openRawResource(
				R.drawable.logo200);

		BitmapDrawable bitmapDrawable = new BitmapDrawable(inputStream);

		imageObject.setImageObject(bitmapDrawable.getBitmap());
		imageObject.description=this.sms;
		imageObject.title=this.sms;
		return imageObject;
	}

	/**
	 * 多媒体（网页）消息构造方法。
	 * 
	 * @return 多媒体（网页）消息对象。
	 */
	private WebpageObject getWebpageObj() {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Util.generateId();// 创建一个唯一的ID
		mediaObject.title = "夜店中国";
		mediaObject.description = this.sms;

		// 设置bitmap类型的图片到视频对象里
		InputStream inputStream = getResources().openRawResource(
				R.drawable.logo200);

		BitmapDrawable bitmapDrawable = new BitmapDrawable(inputStream);

		// BitmapDrawable bitmapDrawable = (BitmapDrawable)
		// mWebpageImage.getDrawable();
		mediaObject.setThumbImage(bitmapDrawable.getBitmap());
		mediaObject.actionUrl = getActionUrl();
		mediaObject.defaultText = "webpage默认文案";
		return mediaObject;
	}

	/**
	 * 多媒体（视频）消息构造方法。
	 * 
	 * @return 多媒体（视频）消息对象。
	 */
	private VideoObject getVideoObj() {
		// 创建媒体消息
		VideoObject videoObject = new VideoObject();
		videoObject.identify = Util.generateId();// 创建一个唯一的ID
		videoObject.title = "we";
		videoObject.description ="uwuweuuwe";

		InputStream inputStream = getResources().openRawResource(
				R.drawable.logo200);

		BitmapDrawable bitmapDrawable = new BitmapDrawable(inputStream);
		videoObject.setThumbImage(bitmapDrawable.getBitmap());
		videoObject.actionUrl = getActionUrl();
		videoObject.dataUrl = "www.weibo.com";
		videoObject.dataHdUrl = "www.weibo.com";
		videoObject.duration = 10;
		videoObject.defaultText = "vedio默认文案";
		return videoObject;
	}

	/**
	 * 多媒体（音乐）消息构造方法。
	 * 
	 * @return 多媒体（音乐）消息对象。
	 */
	private MusicObject getMusicObj() {
		// 创建媒体消息
		MusicObject musicObject = new MusicObject();
		musicObject.identify = Util.generateId();// 创建一个唯一的ID
		musicObject.title ="夜店中国";
		musicObject.description =this.sms;

		// 设置bitmap类型的图片到视频对象里
				InputStream inputStream = getResources().openRawResource(
						R.drawable.logo200);

				BitmapDrawable bitmapDrawable = new BitmapDrawable(inputStream);
		musicObject.setThumbImage(bitmapDrawable.getBitmap());
		musicObject.actionUrl = getActionUrl();
		musicObject.dataUrl = "www.weibo.com";
		musicObject.dataHdUrl = "www.weibo.com";
		musicObject.duration = 10;
		musicObject.defaultText = "music默认文案";
		return musicObject;
	}
	/**
	 * 从本应用->微博->本应用 接收响应数据，该方法被调用。 注意：确保{@link #onCreate(Bundle)} 与
	 * {@link #onNewIntent(Intent)}中， 调用 mWeiboAPI.responseListener(intent,
	 * this)
	 */
	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_OK:
			Toast.makeText(this, "成功！！", Toast.LENGTH_LONG).show();
			break;
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_CANCEL:
			Toast.makeText(this, "用户取消！！", Toast.LENGTH_LONG).show();
			break;
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_FAIL:
			Toast.makeText(this, baseResp.errMsg + ":失败！！", Toast.LENGTH_LONG)
					.show();
			break;
		}
	}

	


}
