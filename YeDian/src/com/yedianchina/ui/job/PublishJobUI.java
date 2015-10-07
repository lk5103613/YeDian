package com.yedianchina.ui.job;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.Util;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.like.entity.Area;
import com.like.network.DataFetcher;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.platformtools.Log;
import com.yedianchina.dao.JobsDao;
import com.yedianchina.po.JobsPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.tools.UploadUtil;
import com.yedianchina.ui.CameraUI;
import com.yedianchina.ui.R;

public class PublishJobUI extends Activity {
	
	private DataFetcher mDataFetcher;
	private ArrayAdapter<String> mAdapter1;
	private ArrayAdapter<String> mAdapter2;
//	private Spinner mAreaSelect;

	TextView jiubaBtn;
	TextView ktvBtn;
	TextView yezonghuiBtn;

	int job_type = 1;
	ImageView photo0;
	ImageView photo1;
	ImageView photo2;
	ImageView photo3;
	
	private Context mContext;

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	String imgURL0;
	String imgURL1;
	String imgURL2;
	String imgURL3;
	String serverImgName;
	int k = 0;
	private String imgPath;
	public String fileName = "tmp.jpg";
	public static final int PHOTOHRAPH = 1;
	public static final int PHOTOZOOM = 2;
	public static final int PHOTORESOULT = 33;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private static final int REQUEST_CODE_TAKE_VIDEO = 4;
	public static final int NONE = 0;
	private File mRecAudioFile;
	private String bigPic;

	private String strVideoPath = "";
	String requestURL = CONSTANTS.IMG_HOST + "yw_uploadify.php";
	
	private Dialog mSuccessDialog;

	public void xc() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1.5);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, 889);

	}
	private void getArea() {
		mDataFetcher.fetchArea("22", new Listener<String>() {
			@Override
			public void onResponse(String response) {
				List<String> areasStr = new ArrayList<String>();
				Type type = new TypeToken<List<Area>>(){}.getType();
				List<Area> areas = new Gson().fromJson(response, type);
				for(Area a : areas) {
					areasStr.add(a.areaName);
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, areasStr);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//				mAreaSelect.setAdapter(adapter);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("resultCode=", "" + resultCode);
		Log.e("requestCode=", "" + requestCode);

		if (resultCode == 888) {

			xc();
		}

		if (requestCode == 101 && 888 != resultCode) {
			if (resultCode == -1) {
				return;
			}
			paizhao();
		}

		if (requestCode == PHOTOHRAPH) {

			File picture = new File(Environment.getExternalStorageDirectory()
					+ "/" + fileName);
			System.out.println("------------------------" + picture.getPath());
			bigPic = Environment.getExternalStorageDirectory() + "/" + fileName;
			startPhotoZoom(Uri.fromFile(picture));
		}
		if (requestCode == REQUEST_CODE_TAKE_VIDEO) {
			if (resultCode == RESULT_OK) {
				Uri uriVideo = data.getData();
				Cursor cursor = this.getContentResolver().query(uriVideo, null,
						null, null, null);
				if (cursor.moveToNext()) {

					strVideoPath = cursor.getString(cursor
							.getColumnIndex("_data"));

				}
			}
		}

		if (data == null)
			return;

		if (requestCode == PHOTOZOOM) {
			startPhotoZoom(data.getData());
		}

		if (resultCode == -1 && requestCode == 889
				|| requestCode == PHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");

				if (resultCode == -1 && requestCode == 889) {
					CompressFormat format = Bitmap.CompressFormat.JPEG;
					int quality = 100;
					OutputStream stream = null;
					try {
						stream = new FileOutputStream("/sdcard/" + fileName);
						photo.compress(format, quality, stream);
					} catch (FileNotFoundException e) {

						e.printStackTrace();
					}

				}

				//
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				// photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0
				// -
				// 100)ѹ���ļ�
				// final
				// LinearLayout
				// layout=(LinearLayout)this.findViewById(R.id.ll8);
				// ImageView tmp=new ImageView(this);
				// tmp.setImageBitmap(photo);
				// layout.addView(tmp);
				// 图片允许最大空间 单位：KB
				double maxSize = 400.00;
				// 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 50, baos);
				byte[] b = baos.toByteArray();
				// 将字节换成KB
				double mid = b.length / 1024;
				// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
				if (mid > maxSize) {
					// 获取bitmap大小 是允许最大大小的多少倍
					double i = mid / maxSize;
					// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
					// （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
					// photo = zoomImage(photo, photo.getWidth() / Math.sqrt(i),
					// photo.getHeight() / Math.sqrt(i));

				}
				zoomImage(photo, 200, 300);

				// ////////////////////////////////////////
				if (k == 0) {
					photo0.setImageBitmap(photo);
				}
				if (k == 1) {
					photo1.setImageBitmap(photo);
				}
				if (k == 2) {
					photo2.setImageBitmap(photo);
				}
				if (k == 3) {
					photo3.setImageBitmap(photo);
				}

				final File file = new File("/sdcard/" + fileName);

				if (file != null) {
					// Log.e(strVideoPath, ":"+requestURL);

					new Thread() {
						public void run() {
							try {

								DateFormat f2 = new SimpleDateFormat(
										"yyyyMMddHHmmss");
								String day = f2.format(new Date());

								int max = 10000;
								int min = 99999;
								Random random = new Random();
								int s = random.nextInt(max) % (max - min + 1)
										+ min;
								serverImgName = day + s;

								System.out.println("上传文 serverImgName=="
										+ serverImgName + "       " + k);
								if (k == 0) {
									imgURL0 = serverImgName + ".jpg";
								}
								if (k == 1) {
									imgURL1 = serverImgName + ".jpg";
								}
								if (k == 2) {
									imgURL2 = serverImgName + ".jpg";
								}
								if (k == 3) {
									imgURL3 = serverImgName + ".jpg";
								}

								UploadUtil.post(file, requestURL, serverImgName);

								// //////////////

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();

				}

			}

		}

		Toast.makeText(this, strVideoPath, Toast.LENGTH_SHORT).show();

	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1.5);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	public void paizhao() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), fileName)));
		// System.out.println("=============" +
		// Environment.getExternalStorageDirectory());
		startActivityForResult(intent, PHOTOHRAPH);
	}

	TextView imgBtn;

	TextView manTxt;
	TextView manImg;
	TextView manPreImg;

	TextView girlPreImg;
	TextView girlTxt;
	TextView girlImg;
	TextView saveBtn;

	int gender = 1;
	Spinner reqJobNameEt;
	EditText descTv;
	public static final String action = "publishjob.broadcast.action";
	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {

				int what = msg.what;
				if (what == 1) {
					Intent intent = new Intent(action);
					intent.putExtra("publishjob", "1");
					sendBroadcast(intent);
					Bitmap bm = BitmapFactory.decodeResource(getResources(),
							R.drawable.logo200);
					String text = "哈我最新使用来夜店中国，里面信息量大 招聘 求职 交友什么都有，有空你也来玩～";
					// if (text == null || text.length() == 0) {
					// return;
					// }

					WXTextObject textObj = new WXTextObject();
					textObj.text = "哈我最新使用来夜店中国，里面信息量大 招聘 求职 交友什么都有，有空你也来玩～";

					Bitmap thumbBmp = Bitmap.createScaledBitmap(bm, 150,
							150, true);

					WXMediaMessage msg2 = new WXMediaMessage();
					msg2.mediaObject = textObj;
					msg2.thumbData = Util.bmpToByteArray(thumbBmp, true); // 设置缩略图

					msg2.title = "夜店中国";
					msg2.description = text;

					SendMessageToWX.Req req = new SendMessageToWX.Req();

					req.transaction = "夜店中国";
					req.message = msg2;
					req.scene = SendMessageToWX.Req.WXSceneTimeline;
					IWXAPI api =  WXAPIFactory.createWXAPI(PublishJobUI.this, Constants.APP_ID);
					api.sendReq(req);
					PublishJobUI.this.finish();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
	};

	private TextPaint paint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.publish_job);

		mContext = this;
		mAdapter1 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, new String[]{
				"DJ", "灯光", "mc", "舞台总监", "舞编", "A组ds", "B组ds", "服务员", "咨客", "营销", "服务员领班", "总经理", "副总经理", "楼面经理", "企划", "收银员", "收银员", "保安", "其他"
		});
		mAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mAdapter2 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, new String[]{
				"DJ公主", "少爷", "楼面经理", "保安", "总经理", "副总经理", "收银员", "佳丽", "男模", "包房服务员", "其他"
		});
		mAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mDataFetcher = DataFetcher.getInstance(mContext);
//		mAreaSelect = (Spinner) findViewById(R.id.local_select);
		getArea();
		TextView navigateTitle = (TextView) this
				.findViewById(R.id.NavigateTitle);
		if (navigateTitle != null) {
			navigateTitle.setText("发布求职简历");
		}

		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					PublishJobUI.this.finish();

				}
			});

		}

		reqJobNameEt = (Spinner) this.findViewById(R.id.reqJobNameEt);
		reqJobNameEt.setAdapter(mAdapter1);
		descTv = (EditText) this.findViewById(R.id.descTv);

		TextView qiandaoBtn = (TextView) this.findViewById(R.id.qiandaoBtn);
		qiandaoBtn.setVisibility(View.GONE);
		saveBtn = (TextView) findViewById(R.id.save_btn);

		saveBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String reqJobName = reqJobNameEt.getSelectedItem().toString();
				String desc = descTv.getText().toString();
				
				if(StringUtils.isEmpty(reqJobName)){
					Toast.makeText(mContext, "请填写应聘职位", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (TextUtils.isEmpty(desc)) {
					Toast.makeText(mContext, "请填写自我描述", Toast.LENGTH_SHORT).show();
					return;
				}
				
				JobsPO po = new JobsPO();
				po.setJob_type(job_type + "");
				po.setReqJobName(reqJobName);
				if (imgURL0 != null && imgURL0.length() > 5) {
					po.setPic0(imgURL0);
				} else {
					po.setPic0("");
				}

				if (imgURL1 != null && imgURL1.length() > 5) {
					po.setPic1(imgURL1);
				} else {
					po.setPic1("");
				}
				if (imgURL2 != null && imgURL2.length() > 5) {
					po.setPic2(imgURL2);
				} else {
					po.setPic2("");
				}
				if (imgURL3 != null && imgURL3.length() > 5) {
					po.setPic3(imgURL3);
				} else {
					po.setPic3("");
				}

				System.out.println("saveRecruit=   imgURL0=" + imgURL0
						+ imgURL1 + "=imgURL1    imgURL1=" + imgURL2 + "  "
						+ imgURL3);

				po.setDesc(desc);
				Long pk = JobsDao.saveJob(po);

				if (pk > 0) {
					showSuccess();
				} else {
					loadingHandler.sendEmptyMessage(2);
					Toast.makeText(mContext, "发布失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		jiubaBtn = (TextView) this.findViewById(R.id.jiubaBtn);
		ktvBtn = (TextView) this.findViewById(R.id.ktvBtn);
		yezonghuiBtn = (TextView) this.findViewById(R.id.yezonghuiBtn);

		jiubaBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				jiubaBtn.setBackgroundResource(R.drawable.jb_focus);

				ktvBtn.setBackgroundResource(R.drawable.ktv);
				yezonghuiBtn.setBackgroundResource(R.drawable.yzh);
				job_type = 1;
				reqJobNameEt.setAdapter(mAdapter1);

			}
		});
		//
		ktvBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				jiubaBtn.setBackgroundResource(R.drawable.jb);

				ktvBtn.setBackgroundResource(R.drawable.ktv_focus);
				yezonghuiBtn.setBackgroundResource(R.drawable.yzh);

				job_type = 2;
				reqJobNameEt.setAdapter(mAdapter2);

			}
		});
		//
		yezonghuiBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				jiubaBtn.setBackgroundResource(R.drawable.jb);

				ktvBtn.setBackgroundResource(R.drawable.ktv);
				yezonghuiBtn.setBackgroundResource(R.drawable.yzh_focus);

				job_type = 3;

			}
		});
		
		jiubaBtn.performClick();

		photo0 = (ImageView) this.findViewById(R.id.photo0);
		if (photo0 != null) {
			photo0.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 0;
					Intent intent = new Intent();
					intent.setClass(PublishJobUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}

		photo1 = (ImageView) this.findViewById(R.id.photo1);
		if (photo1 != null) {
			photo1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 1;
					Intent intent = new Intent();
					intent.setClass(PublishJobUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}

		photo2 = (ImageView) this.findViewById(R.id.photo2);
		if (photo2 != null) {
			photo2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 2;
					Intent intent = new Intent();
					intent.setClass(PublishJobUI.this, CameraUI.class);
					startActivityForResult(intent, 101);
				}
			});
		}
		//

		//
		photo3 = (ImageView) this.findViewById(R.id.photo3);
		if (photo3 != null) {
			photo3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 3;
					Intent intent = new Intent();
					intent.setClass(PublishJobUI.this, CameraUI.class);
					startActivityForResult(intent, 101);
				}
			});
		}

		// descPreTv
		TextView descPreTv = (TextView) this.findViewById(R.id.descPreTv);
		paint = descPreTv.getPaint();
		paint.setFakeBoldText(true);

		TextView reqJobNamePreTv = (TextView) this
				.findViewById(R.id.reqJobNamePreTv);
		paint = reqJobNamePreTv.getPaint();
		paint.setFakeBoldText(true);

	}
	
	private void showSuccess(){
		mSuccessDialog = new AlertDialog.Builder(this). 
                setTitle(""). 
                setMessage("发布成功,是否分享到朋友圈"). 
                setIcon(R.drawable.ic_launcher). 
                setPositiveButton("是", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						loadingHandler.sendEmptyMessage(1);
					}
				}).setNegativeButton("否", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						loadingHandler.sendEmptyMessage(1);
//						mSuccessDialog.dismiss();
					}
				}).create(); 
        mSuccessDialog.show(); 
	}

}
