package com.yedianchina.ui.job;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gallery.ImageAdapter;
import com.my.Files;
import com.my.Net;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.control.BrandGallery;
import com.yedianchina.control.ImageAdapterJobDetailUI;
import com.yedianchina.dao.JobsDao;
import com.yedianchina.dao.MerchantDao;
import com.yedianchina.po.JobsPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.LoginUI;
import com.yedianchina.ui.R;
import com.yedianchina.ui.YeDianChinaApplication;

public class JobReqDetailUI extends CommonActivity implements
		OnItemClickListener, OnClickListener, OnItemSelectedListener {
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

	private JobsPO po;
	private Long pk;
	int cat2Id;
	int cat1Id;
	private List<String> imgList;// 商品图片轮播
	private int num = 0;
	public static ImageAdapterJobDetailUI imageAdapter;

	public void updateCurrendData() {
		this.po = JobsDao.loadJobs(pk);
	}

	public void favJob(Long uid) {
		MerchantDao.favJobReq(uid, pk);
	}

	String mp;
	TextView callMpTv;
	TextView linkNumber;

	TextView jiubaBtn;
	TextView ktvBtn;
	TextView yezonghuiBtn;

	public void telFn() {
		mp = po.getMp();
		AlertDialog.Builder alert = new AlertDialog.Builder(JobReqDetailUI.this);
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
				imgList = new ArrayList<String>();

				String pic0 = po.getPic0();
				if (pic0 != null && pic0.length() > 5) {
					pic0 = CONSTANTS.HOST + "ios/" + pic0;
					imgList.add(pic0);
				}
				//
				String pic1 = po.getPic1();
				if (pic1 != null && pic1.length() > 5) {
					pic1 = CONSTANTS.HOST + "ios/" + pic1;
					imgList.add(pic1);
				}
				//
				String pic2 = po.getPic2();
				if (pic2 != null && pic2.length() > 5) {
					pic2 = CONSTANTS.HOST + "ios/" + pic2;
					imgList.add(pic2);
				}
				//
				String pic3 = po.getPic3();
				if (pic3 != null && pic3.length() > 5) {
					pic3 = CONSTANTS.HOST + "ios/" + pic3;
					imgList.add(pic3);
				}
				setLoanInfoDetailData();

				// //////////
				String _linknumber = po.getMp();
				String _linkman = po.getTrueName();
				// Log.e("_linknumber=", _linknumber);
				// Log.e("_linkman=", _linkman);
				if (_linkman == null || _linkman.equals("null")) {
					_linkman = "";
				}

				//
				if (_linknumber != null && _linknumber.length() >= 7) {
					linkMpTv.setText(_linknumber);
				}

				callMpTv.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						telFn();

					}
				});

				// /////////////////////

				if ("1".equals(po.getJob_type())) {
					jiubaBtn.setBackgroundResource(R.drawable.jb_focus);

					ktvBtn.setBackgroundResource(R.drawable.ktv);

					yezonghuiBtn.setBackgroundResource(R.drawable.yzh);

					jiubaBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
					ktvBtn.setTextColor(Color.parseColor("#ff404040"));
					yezonghuiBtn.setTextColor(Color.parseColor("#ff404040"));
				}
				if ("2".equals(po.getJob_type())) {
					jiubaBtn.setBackgroundResource(R.drawable.jb);

					ktvBtn.setBackgroundResource(R.drawable.ktv_focus);

					yezonghuiBtn.setBackgroundResource(R.drawable.yzh);

					jiubaBtn.setTextColor(Color.parseColor("#ff404040"));
					ktvBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
					yezonghuiBtn.setTextColor(Color.parseColor("#ff404040"));

				}
				if ("3".equals(po.getJob_type())) {
					jiubaBtn.setBackgroundResource(R.drawable.jb);

					ktvBtn.setBackgroundResource(R.drawable.ktv);

					yezonghuiBtn.setBackgroundResource(R.drawable.yzh_focus);

					jiubaBtn.setTextColor(Color.parseColor("#ff404040"));
					ktvBtn.setTextColor(Color.parseColor("#ff404040"));
					yezonghuiBtn.setTextColor(Color.parseColor("#FFFFFFFF"));

				}

				// /////////////////////////
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
	TextView titleTv;
	TextView genderTv;
	TextView ageEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.reqjob_detail);

		// 手机界面标题设置
		super.textViewTitle = (TextView) findViewById(R.id.tvHeaderTitle);

		ageEt = (TextView) this.findViewById(R.id.ageEt);
		genderTv = (TextView) this.findViewById(R.id.genderTv);

		linkMpTv = (TextView) this.findViewById(R.id.linkMpTv);
		callMpTv = (TextView) findViewById(R.id.linkMpTv);
		favBtn = (TextView) this.findViewById(R.id.favBtn);
		if (favBtn != null) {
			favBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					SharedPreferences preferences = getSharedPreferences(
							"yedianchina_user_info", Activity.MODE_PRIVATE);
					final Long uid = preferences.getLong("uid", 0);
					if (uid == 0) {
						Intent intent = new Intent();
						intent.setClass(JobReqDetailUI.this, LoginUI.class);

						JobReqDetailUI.this.startActivity(intent);
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
		String strPK = bundle.getString("pk");
		this.pk = Long.parseLong(strPK);

		Log.e("主键", "pk=" + pk);

		contentDescTv = (TextView) this.findViewById(R.id.descTv);

		Files.mkdir(this);
		Bitmap image = BitmapFactory.decodeResource(getResources(),
				R.drawable.empty_photo);
		// imagesCache.put("background_non_load", image); // 设置缓存中默认的图片

		new Thread() {
			public void run() {

				updateCurrendData();

				loadingHandler.sendEmptyMessage(1);

			}
		}.start();

		TextView qiandaoBtn = (TextView) this.findViewById(R.id.qiandaoBtn);
		if (qiandaoBtn != null) {
			qiandaoBtn.setVisibility(View.INVISIBLE);// 隐藏返回按钮 但是可以点击
		}
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					JobReqDetailUI.this.finish();

				}
			});

		}

		// ///////////////////////////////////////
		TextView linkmanPreTv = (TextView) this.findViewById(R.id.linkmanPreTv);
		TextPaint paint = linkmanPreTv.getPaint();
		paint.setFakeBoldText(true);

		//

		TextView sexPreTv = (TextView) this.findViewById(R.id.sexPreTv);
		paint = sexPreTv.getPaint();
		paint.setFakeBoldText(true);

		TextView agePreTv = (TextView) this.findViewById(R.id.agePreTv);
		paint = agePreTv.getPaint();
		paint.setFakeBoldText(true);

		TextView linkMpPreTv = (TextView) this.findViewById(R.id.linkMpPreTv);
		paint = linkMpPreTv.getPaint();
		paint.setFakeBoldText(true);
		//
		TextView linkEmailPreTv = (TextView) this
				.findViewById(R.id.linkEmailPreTv);
		paint = linkEmailPreTv.getPaint();
		paint.setFakeBoldText(true);

		// descPreTv
		TextView descPreTv = (TextView) this.findViewById(R.id.descPreTv);
		paint = descPreTv.getPaint();
		paint.setFakeBoldText(true);
		//

		TextView reqJobNamePreTv = (TextView) this
				.findViewById(R.id.reqJobNamePreTv);
		paint = reqJobNamePreTv.getPaint();
		paint.setFakeBoldText(true);

		//

		TextView NavigateTitle = (TextView) this
				.findViewById(R.id.NavigateTitle);
		NavigateTitle.setText("求职简历");
		//
		jiubaBtn = (TextView) this.findViewById(R.id.jiubaBtn);

		ktvBtn = (TextView) this.findViewById(R.id.ktvBtn);
		yezonghuiBtn = (TextView) this.findViewById(R.id.yezonghuiBtn);

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

				Intent intent = new Intent();
				intent.setClass(JobReqDetailUI.this, JobReqDetailUI.class);
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

		String mp = po.getMp();

		emailTv = (TextView) findViewById(R.id.emailTv);
		String email = po.getEmail();
		if (email != null && email.length() > 4) {
			emailTv.setText(email);
		}

		String age = po.getAge();
		if (age != null && age.length() > 0&&StringUtils.isNotEmpty(age)) {
			if("null".equals(age)){
				age="";
			}
			ageEt.setText(age);
		}

		String gender = po.getGender();
//		if ("0".equals(gender)) {
//			genderTv.setText("男");
//		}
		if ("1".equals(gender)) {
			genderTv.setText("女");
		}else{
			genderTv.setText("男");
		}

		cityNameTv = (TextView) findViewById(R.id.cityNameTv);
		String cityName = po.getCityName();
		if (cityName != null && cityName.length() > 0) {
			cityNameTv.setText(cityName);
		}

		linkmanTv = (TextView) findViewById(R.id.linkmanTv);
		String linkman = po.getLinkman();
		if (linkman != null && linkman.length() > 0) {
			linkmanTv.setText(linkman);
		}

		jobTv = (TextView) findViewById(R.id.reqJobNameEt);
		String job = po.getReqJobName();

		if (job != null && job.length() > 0) {
			jobTv.setText(job);
		}

		TextView salaryTv = (TextView) findViewById(R.id.salaryTv);
		String salary = po.getSalary();
		if (salary != null && salary.length() > 0) {
			salaryTv.setText(salary);
		}
		String desc = po.getDesc();
		if (desc != null && desc.length() > 0) {

			contentDescTv.setText(Html.fromHtml(desc));
		}
		String address = po.getWorkAddress();
		if (address != null && address.length() > 0) {
			addressTv.setText(address);
		}

		// 商品图片展示
		ImageAdapter adapter = new ImageAdapter(JobReqDetailUI.this, imgList);
		adapter.createReflectedImages();
		// ///////////

		// /////////

		final Gallery galleryFlow = (Gallery) findViewById(R.id.Gallery01);
		galleryFlow.setAdapter(adapter);
		galleryFlow.setSpacing(10); // 图片之间的间距

		galleryFlow
				.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

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
							.downloadResource(JobReqDetailUI.this, url);
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
