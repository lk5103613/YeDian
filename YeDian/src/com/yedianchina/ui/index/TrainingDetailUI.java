package com.yedianchina.ui.index;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gallery.GalleryFlow;
import com.gallery.ImageAdapter;
import com.my.Files;
import com.my.Net;
import com.yedianchina.control.BrandGallery;
import com.yedianchina.control.ImageAdapterJobDetailUI;
import com.yedianchina.dao.ErshoushebeiDao;
import com.yedianchina.dao.MerchantDao;
import com.yedianchina.po.ErshoushebeiPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.LoginUI;
import com.yedianchina.ui.R;
import com.yedianchina.ui.YeDianChinaApplication;
import com.yedianchina.ui.job.JobReqDetailUI;
//培训 06-07
public class TrainingDetailUI extends CommonActivity implements
		OnItemClickListener, OnClickListener, OnItemSelectedListener {
	int flag=4;
	//图片幻灯片
	private void setGalleryFlow(){


		ImageAdapter adapter = new ImageAdapter(TrainingDetailUI.this, imgList);
		adapter.createReflectedImages();
		// ///////////

		// /////////

		final GalleryFlow galleryFlow = (GalleryFlow) findViewById(R.id.Gallery01);
		galleryFlow.setAdapter(adapter);
		//galleryFlow.setSpacing(1); // 图片之间的间距
		
		galleryFlow.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
				
			}
			
		});

		galleryFlow
				.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						
					}

				});
		// /
	
		
		
		
		
	}
	
	
	public static HashMap<String, Bitmap> imagesCache = new HashMap<String, Bitmap>(); // 图片缓存
	/**
	 * 品牌介绍
	 */
	private TextView trueNameAgeTv = null;
	/**
	 * 价格信息
	 */
	private TextView priceTextView = null;
	/**
	 * 元为单位
	 */
	private TextView dollarTextView = null;
	/**
	 * 立即购买
	 */
	private TextView buyBtn = null;
	/**
	 * 加入购物�?
	 */
	private TextView emailTv = null;
	/**
	 * 上市时间
	 */
	private TextView cityNameTv = null;
	/**
	 * 款式
	 */
	private TextView linkmanTv = null;
	/**
	 * 所属行业
	 */
	private TextView jobTv = null;

	/**
	 * 地址
	 */
	private TextView addressTv = null;
	/**
	 * 男女�?
	 */
	private TextView homeAddressTv = null;// 暂住地 或家庭地址

	private TextView contentDescTv = null;
	/**
	 * 专柜价格
	 */
	private TextView shoppePriceTextView = null;
	/**
	 * 85°C价格
	 */
	private TextView linkMpTv = null;
	/**
	 * 收藏
	 */
	private TextView favBtn = null;

	/**
	 * 商品图片展示
	 */
	private BrandGallery imagesGallery = null;
	/**
	 * 左边箭头按钮
	 */
	private ImageView leftImageView = null;
	/**
	 * 右边箭头按钮
	 */
	private ImageView rightImageView = null;
	/**
	 * value_hassize
	 */
	private LinearLayout codeLinearLayout = null;
	
	private ErshoushebeiPO po;
	private Long pk;
	int cat2Id;
	int cat1Id;
	private List<String> imgList;// 商品图片轮播
	private int num = 0;
	public static ImageAdapterJobDetailUI imageAdapter;

	public void updateCurrendData() {
		this.po = ErshoushebeiDao.loadErshoushebei(pk);
	}

	public void favJob(Long uid) {
		MerchantDao.favJobReq(uid, pk);
	}

	String mp;
	TextView callMpTv;
	TextView linkNumber;

	public void telFn() {
		mp = po.getLinkmp();
		AlertDialog.Builder alert = new AlertDialog.Builder(TrainingDetailUI.this);
		alert.setTitle("拨打电话确认")
				.setMessage("您确认拨打对方电话吗？")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent phoneIntent = new Intent(
								"android.intent.action.CALL",

								Uri.parse("tel:" + mp));
						dialog.dismiss();
						startActivity(phoneIntent);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		alert.create().show();
	}
	
	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				setLoanInfoDetailData();
				
				setGalleryFlow();

				// //////////
				String _linknumber = po.getLinkmp();
				String _linkman = po.getLinkman();
				//Log.e("_linknumber=", _linknumber);
				//Log.e("_linkman=", _linkman);
				if (_linkman == null || _linkman.equals("null")) {
					_linkman = "";
				}
				String str = _linknumber + "\n" + _linkman;

				linkNumber.setText(str);
				//
				callMpTv.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						telFn();

					}
				});
				
			
			}
		};
	};
	Handler saveFavHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				Toast toast = Toast.makeText(getApplicationContext(), "收藏成功!",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			if (what == 2) {
				Toast toast = Toast.makeText(getApplicationContext(), "收藏失败!",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			if (what == 3) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"您已经收藏本帖，请勿重复收藏!", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
	};
	
	ImageView avatarId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.ershoushebei_detail);
	 
		
		// 手机界面标题设置
		super.textViewTitle = (TextView) findViewById(R.id.tvHeaderTitle);

		linkNumber = (TextView) this.findViewById(R.id.linkMpTv);
		callMpTv = (TextView) findViewById(R.id.linkMpTv);
		favBtn = (TextView) this.findViewById(R.id.favBtn);
		if (favBtn != null) {
			favBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					SharedPreferences preferences = getSharedPreferences(CONSTANTS.YEDIANCHINA_USER_INFO,
							Activity.MODE_PRIVATE);
					final Long uid = preferences.getLong("uid", 0);
					if (uid == 0) {
						Intent intent = new Intent();
						intent.setClass(TrainingDetailUI.this, LoginUI.class);

						TrainingDetailUI.this.startActivity(intent);
					} else {
						new Thread() {
							public void run() {

								int code = MerchantDao.favJobReq(uid, pk);
								Log.e("保存收藏..........", "code=" + code);

								saveFavHandler.sendEmptyMessage(code);

							}
						}.start();
					}

				}
			});
		}
	 

		Bundle bundle = this.getIntent().getExtras();
		this.pk = bundle.getLong("pk");

		Log.e("主键", "pk=" + pk);
		// cat1Id

	 

		Files.mkdir(this);
		Bitmap image = BitmapFactory.decodeResource(getResources(),
				R.drawable.empty_photo);
		// imagesCache.put("background_non_load", image); // 设置缓存中默认的图片

		new Thread() {
			public void run() {

				updateCurrendData();
				imgList = new ArrayList<String>();
				String tmp = po.getImgurls();
				if (tmp != null) {
					String[] imgArray = tmp.split(";");
					if (imgArray != null && imgArray.length > 0) {
						for (int i = 0; i < imgArray.length; i++) {
							String img = imgArray[i];
							if (img != null && img.length() > 10) {
								img=CONSTANTS.HOST+img;
								imgList.add(img);
							}
						}
					}
				}

				loadingHandler.sendEmptyMessage(1);

			}
		}.start();

		TextView qiandaoBtn = (TextView) this.findViewById(R.id.qiandaoBtn);
		if (qiandaoBtn != null) {
			qiandaoBtn.setVisibility(View.INVISIBLE);// 隐藏返回按钮 但是可以点击
		}
		ImageView backBtn=(ImageView)this.findViewById(R.id.backBtn);
		if(backBtn!=null){
			backBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					TrainingDetailUI.this.finish();
					
				}
			});
			
		}
		

		YeDianChinaApplication.getInstance().addActivity(this);
	}

	public void saveImg2SD(final String url, final String _goodsId)
			throws IOException {
		Runnable getImage = new Runnable() {
			public void run() {

				try {

					URL u = new URL(url);

					HttpURLConnection c = (HttpURLConnection) u
							.openConnection();

					c.setRequestMethod("GET");

					c.setDoOutput(true);

					c.connect();

					String folder = Environment.getExternalStorageDirectory()
							.toString();

					FileOutputStream f = new FileOutputStream(folder + "/"
							+ _goodsId + ".jpg", true);

					InputStream in = c.getInputStream();

					byte[] buffer = new byte[1024];

					int len1 = 0;

					while ((len1 = in.read(buffer)) > 0) {

						f.write(buffer, 0, len1);

					}

					f.close();

				} catch (Exception e) {

					Log.e("保存图片到SD", "could not download and save IMAGE file",
							e);

				}

			}

		};

		new Thread(getImage).start();
	}

	@Override
	public void onClick(View view) {
		try {
			if (view == buyBtn) {
				// Toast toast = Toast.makeText(getApplicationContext(),
				// "购买成功!", Toast.LENGTH_SHORT);
				// toast.setGravity(Gravity.CENTER, 0, 0);
				// toast.show();
				//
				Intent intent = new Intent();
				intent.setClass(TrainingDetailUI.this, JobReqDetailUI.class);
				Bundle bundle = new Bundle();

				bundle.putLong("fromUI", 1);// 从商品详情界面进入提交订�?
				intent.putExtras(bundle);
				startActivity(intent);

			} else if (view == favBtn) {
				Toast toast = Toast.makeText(getApplicationContext(), "收藏成功!",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			} else if (view == codeLinearLayout) {//
				// codeTextView.setBackgroundColor(Color.RED);
				new AlertDialog.Builder(this)
						.setTitle("请")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setSingleChoiceItems(new String[] { "300X1" }, 0,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).setNegativeButton("确定", null).show();

				System.out.println("TanRuixiang");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 应用的最后一个Activity关闭时应释放DB

	}

	private void setLoanInfoDetailData() {
	
		linkmanTv = (TextView) findViewById(R.id.linkmanTv);
		String linkman = po.getLinkman();
		if (linkman != null && linkman.length() > 0) {
			linkmanTv.setText(linkman);
		}

		linkMpTv = (TextView) findViewById(R.id.linkMpTv);
		String mp = po.getLinkmp();

		if (mp != null && mp.length() > 0) {
			linkMpTv.setText(mp);
		}

		TextView emailTv = (TextView) findViewById(R.id.emailTv);
		String email = po.getEmail();
		if (email != null && email.length() > 0) {
			emailTv.setText(email);
		}
		String desc = po.getDesc();
		if (desc != null && desc.length() > 0) {

			contentDescTv.setText(Html.fromHtml(desc));
		}
		String address = po.getAddr();
		if (address != null && address.length() > 0) {
			addressTv.setText(address);
		}

		// 商品图片展示
		ImageAdapter adapter = new ImageAdapter(TrainingDetailUI.this, imgList);
		adapter.createReflectedImages();
		// ///////////

		// /////////

		final GalleryFlow galleryFlow = (GalleryFlow) findViewById(R.id.Gallery01);
		galleryFlow.setAdapter(adapter);
		//galleryFlow.setSpacing(1); // 图片之间的间距
		
		galleryFlow.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
				
			}
			
		});

		galleryFlow
				.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						
					}

				});
		// /

		
		
		
		
		
		
		

	}

	private List<TextView> btnList = new ArrayList<TextView>();

	/**
	 * 判断Gallery滚动是否停止,如果停止则加载当前页面的图片
	 */
	private void GalleryWhetherStop() {
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					int index = 0;
					index = num;
					Thread.sleep(1000);
					if (index == num) {

						Message m = new Message();
						m.what = 1;
						mHandler.sendMessage(m);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(runnable).start();
	}

	// 加载图片的异步任�?
	class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null;
			try {
				String url = params[0];
				boolean isExists = Files.compare(url);
				if (isExists == false) {
					Net net = new Net();
					byte[] data = net
							.downloadResource(TrainingDetailUI.this, url);
					bitmap = BitmapFactory
							.decodeByteArray(data, 0, data.length);
					imagesCache.put(url, bitmap);
					Files.saveImage(url, data);
				} else {
					byte[] data = Files.readImage(url);
					bitmap = BitmapFactory
							.decodeByteArray(data, 0, data.length);
					imagesCache.put(url, bitmap);
				}

				Message m = new Message();
				m.what = 0;
				mHandler.sendMessage(m);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case 0: {
					imageAdapter.notifyDataSetChanged();
					break;
				}
				case 1: {
					for (int i = 0; i < imgList.size(); i++) {
						LoadImageTask task = new LoadImageTask();// 异步加载图片
						task.execute(imgList.get(i));
						Log.i("mahua", imgList.get(i));
					}
					// imgList.clear();
				}
				}
				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (position > 0) {
			leftImageView.setVisibility(View.VISIBLE);
			if (position == imageAdapter.imageUrls.size() - 1) {
				rightImageView.setVisibility(View.GONE);
				leftImageView.setVisibility(View.VISIBLE);
			} else {
				rightImageView.setVisibility(View.VISIBLE);
			}
		} else if (position == 0) {
			leftImageView.setVisibility(View.GONE);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

}


