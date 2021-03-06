package com.yedianchina.ui.recruit;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.Util;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.platformtools.Log;
import com.yedianchina.dao.RecruitDao;
import com.yedianchina.po.RecruitPO;
import com.yedianchina.tools.BitmapUtils;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.tools.ImageTools;
import com.yedianchina.tools.UploadUtil;
import com.yedianchina.ui.CameraUI;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.R;

public class PublishRecruitUI extends CommonActivity {

	private static final int SCALE = 5;// 照片缩小比例
//	private Spinner mAreaSelect;

	// 压缩图片

	private ArrayAdapter<String> mAdapter1;
	private ArrayAdapter<String> mAdapter2;
	public void yaSuo(String path) {
		FileInputStream in;
		byte[] mContent = null;
		Bitmap myBitmap;

		try {
			in = new FileInputStream(path);
			mContent = readStream(in);

		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}

		myBitmap = BitmapUtils.getBitmap(mContent, 480, 800);
		File file = new File(path);
		System.out.println("fileLenth" + file.length());
		int offset = 100;
		long fileSize = file.length();
		if (200 * 1024 < fileSize && fileSize <= 1024 * 1024)
			offset = 90;
		else if (1024 * 1024 < fileSize)
			offset = 85;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		myBitmap.compress(Bitmap.CompressFormat.JPEG, offset, baos);
		mContent = baos.toByteArray();

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		try {
			bos.write(mContent);
		} catch (IOException e) {

			e.printStackTrace();
		}

		if (bos != null) {
			try {
				bos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (fos != null) {
			try {
				fos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		File file1 = new File(path);
		System.out.println("fileLenth" + file1.length());
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

	// ///////////////////////////////////////////////////////
	ImageView photo0;
	ImageView photo1;
	ImageView photo2;
	ImageView photo3;

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
	int k = 0;
	private String imgPath;
	public String fileName = "tmp.jpg";
	public static final int PHOTOHRAPH = 1;
	public static final int PHOTOZOOM = 2;
	public static final int PHOTORESOULT = 33;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private static final int REQUEST_CODE_TAKE_VIDEO = 4;
	public static final int NONE = 0;

	private String bigPic;

	private String strVideoPath = "";
	String requestURL = CONSTANTS.IMG_HOST + "yw_uploadify.php";

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

	String serverImgName;
	
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		Log.e("resultCode=", "" + resultCode);
		System.out.println("requestCode=" + requestCode);

		System.out.println("resultCode=" + resultCode);

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

					try {

						if (photo == null) {
							ContentResolver resolver = getContentResolver();
							// 照片的原始资源地址
							Uri originalUri = data.getData();
							try {
								// 使用ContentProvider通过URI获取原始图片
								photo = MediaStore.Images.Media.getBitmap(
										resolver, originalUri);

								BufferedOutputStream bos = new BufferedOutputStream(
										new FileOutputStream("/sdcard/"
												+ fileName, false));
								photo.compress(Bitmap.CompressFormat.JPEG, 100,
										bos);
								bos.flush();
								bos.close();

							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						BufferedOutputStream bos = new BufferedOutputStream(
								new FileOutputStream("/sdcard/" + fileName,
										false));
						photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
						bos.flush();
						bos.close();

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

					} catch (Exception e) {

						e.printStackTrace();
					}

				}

				//
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);

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

				yaSuo("/sdcard/" + fileName);

				final File file = new File("/sdcard/" + fileName);

				if (file != null) {
					Log.e("老王", ":" + requestURL);

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

								Log.e("上传文件", "imgPath==" + imgPath);
								if (k == 0) {

									imgURL0 = serverImgName + ".jpg";
									Log.e("imgURL0", "imgURL0=" + imgURL0);
								}
								if (k == 1) {
									imgURL1 = serverImgName + ".jpg";
									Log.e("imgURL1", "imgURL1=" + imgURL1);
								}
								if (k == 2) {
									imgURL2 = serverImgName + ".jpg";
									Log.e("imgURL2", "imgURL2=" + imgURL2);
								}
								if (k == 3) {
									imgURL3 = serverImgName + ".jpg";
									Log.e("imgURL3", "imgURL3=" + imgURL3);
								}
								UploadUtil
										.post(file, requestURL, serverImgName);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();

				}

			} else {

				ContentResolver resolver = getContentResolver();
				// 照片的原始资源地址
				Uri originalUri = data.getData();
				try {
					// 使用ContentProvider通过URI获取原始图片
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);

					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream("/sdcard/" + fileName, false));
					photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
					bos.flush();
					bos.close();

					if (photo != null) {
						// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / SCALE, photo.getHeight()
										/ SCALE);
						// 释放原始图片占用的内存，防止out of memory异常发生
						photo.recycle();

						// avatarTv.setImageBitmap(smallBitmap);
						if (k == 0) {
							photo0.setImageBitmap(smallBitmap);
						}
						if (k == 1) {
							photo1.setImageBitmap(smallBitmap);
						}
						if (k == 2) {
							photo2.setImageBitmap(smallBitmap);
						}
						if (k == 3) {
							photo3.setImageBitmap(smallBitmap);
						}

					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				yaSuo("/sdcard/" + fileName);
				final File file = new File("/sdcard/" + fileName);

				//

				if (file != null) {
					Log.e("老王2", ":" + requestURL);

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

								Log.e("上传文件", "imgPath==" + imgPath);
								if (k == 0) {

									imgURL0 = serverImgName + ".jpg";
									Log.e("imgURL0", "imgURL0=" + imgURL0);
								}
								if (k == 1) {
									imgURL1 = serverImgName + ".jpg";
									Log.e("imgURL1", "imgURL1=" + imgURL1);
								}
								if (k == 2) {
									imgURL2 = serverImgName + ".jpg";
									Log.e("imgURL2", "imgURL2=" + imgURL2);
								}
								if (k == 3) {
									imgURL3 = serverImgName + ".jpg";
									Log.e("imgURL3", "imgURL3=" + imgURL3);
								}

								UploadUtil
										.post(file, requestURL, serverImgName);

								// //////////////

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();

				}

			}

		}

	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
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

	// //////////////////////////
	public static final String action = "publishrecruit.broadcast.action";
	Long pk = null;
	RecruitPO po = null;

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {

				int what = msg.what;
				if (what == 1) {
					Intent intent = new Intent(action);
					intent.putExtra("publishrecruit", "1");
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
					IWXAPI api =  WXAPIFactory.createWXAPI(PublishRecruitUI.this, Constants.APP_ID);
					api.sendReq(req);

					PublishRecruitUI.this.finish();

				} else if (what == 2) {

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		};
	};

	Spinner titleEt;
	TextView addTimeTv;

	EditText salaryEt;
	EditText companyNameEt;

	EditText addressEt;
	TextView tjEt;

	EditText jobDescEt;
	//
	TextView linkmanTv;
	TextView linkMpTv;

	TextView emailTv;
	TextView companyAddressTv;

	TextView shareBtn;
	TextView favBtn;
	private Context mContext;

	Handler loginHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;

			if (what == 1) {
				Toast toast = Toast.makeText(getApplicationContext(), "收藏成功",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			} else if (what == 7) {

				Toast toast = Toast.makeText(getApplicationContext(),
						"您已经收藏，请勿重复收藏", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			} else {

				Toast toast = Toast.makeText(getApplicationContext(), "收藏成功失败",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

		};
	};

	TextView jiubaBtn;
	TextView ktvBtn;
	TextView yezonghuiBtn;

	int recruit_type = 1;
	EditText ageEt;
	private AlertDialog mSuccessDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mContext = this;
		setContentView(R.layout.publish_recruit);
		
		getArea();
		
		mAdapter1 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, new String[]{
				"DJ", "灯光", "mc", "舞台总监", "舞编", "A组ds", "B组ds", "服务员", "咨客", "营销", "服务员领班", "总经理", "副总经理", "楼面经理", "企划", "收银员", "收银员", "保安", "其他"
		});
		mAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mAdapter2 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, new String[]{
				"DJ公主", "少爷", "楼面经理", "保安", "总经理", "副总经理", "收银员", "佳丽", "男模", "包房服务员", "其他"
		});
		mAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		titleEt = (Spinner) this.findViewById(R.id.titleEt);
		titleEt.setAdapter(mAdapter1);
		
		addTimeTv = (TextView) this.findViewById(R.id.addTimeTv);
//		mAreaSelect = (Spinner) findViewById(R.id.local_select);

		tjEt = (EditText) this.findViewById(R.id.tjEt);

		linkmanTv = (TextView) this.findViewById(R.id.linkmanTv);
		linkMpTv = (TextView) this.findViewById(R.id.linkMpTv);

		//
		emailTv = (TextView) this.findViewById(R.id.emailTv);
		companyAddressTv = (TextView) this.findViewById(R.id.companyAddressTv);

		TextView okBtn = (TextView) this.findViewById(R.id.okBtn);
		okBtn.setVisibility(View.INVISIBLE);

		TextView saveBtn = (TextView) this.findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				po = new RecruitPO();
				String title = titleEt.getSelectedItem().toString();
				String tj = tjEt.getText().toString();

				if (TextUtils.isEmpty(title)) {
					Toast.makeText(mContext, "请填写职位名称", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (TextUtils.isEmpty(tj)) {
					Toast.makeText(mContext, "请填写职位描述", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				po.setTitle(title);

				if (tj != null && tj.length() > 0) {
					po.setTj(tj);
				}

				po.setRecruit_type(String.valueOf(recruit_type));

				if (imgURL0 != null && imgURL0.length() > 5) {
					po.setPic0(imgURL0);
				} else {
					po.setPic0("");
				}
				Log.e("saveRecruit", imgURL0 + "  " + imgURL1 + "  " + imgURL2
						+ "  " + imgURL3);
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

				new Thread() {
					public void run() {
						Long pk = RecruitDao.saveRecruit(po);
						System.out.println("pk=======================" + pk);
						if (pk > 0) {
							runOnUiThread(new Runnable() {
								public void run() {
									showSuccess();
								}
							});

						} else {
							loadingHandler.sendEmptyMessage(2);
						}

					}
				}.start();

			}
		});

		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					PublishRecruitUI.this.finish();

				}
			});
		}

		// jiubaBtn = (TextView) this.findViewById(R.id.jiubaBtn);
		//
		// ktvBtn = (TextView) this.findViewById(R.id.ktvBtn);
		// yezonghuiBtn = (TextView) this.findViewById(R.id.yezonghuiBtn);
		//
		// jiubaBtn.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// jiubaBtn.setTextColor(getResources().getColor(
		// android.R.color.holo_red_dark));
		//
		// ktvBtn.setTextColor(getResources().getColor(
		// android.R.color.black));
		// yezonghuiBtn.setTextColor(getResources().getColor(
		// android.R.color.black));
		// recruit_type = 1;
		//
		// }
		// });
		// //
		// ktvBtn.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// ktvBtn.setTextColor(getResources().getColor(
		// android.R.color.holo_red_dark));
		//
		// jiubaBtn.setTextColor(getResources().getColor(
		// android.R.color.black));
		// yezonghuiBtn.setTextColor(getResources().getColor(
		// android.R.color.black));
		//
		// recruit_type = 2;
		//
		// }
		// });
		// //
		// yezonghuiBtn.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// ktvBtn.setTextColor(getResources().getColor(
		// android.R.color.black));
		//
		// jiubaBtn.setTextColor(getResources().getColor(
		// android.R.color.black));
		// yezonghuiBtn.setTextColor(getResources().getColor(
		// android.R.color.holo_red_dark));
		//
		// recruit_type = 3;
		//
		// }
		// });

		jiubaBtn = (TextView) this.findViewById(R.id.jiubaBtn);

		ktvBtn = (TextView) this.findViewById(R.id.ktvBtn);
		yezonghuiBtn = (TextView) this.findViewById(R.id.yezonghuiBtn);

		jiubaBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
		ktvBtn.setTextColor(Color.parseColor("#ff404040"));
		yezonghuiBtn.setTextColor(Color.parseColor("#ff404040"));

		jiubaBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
				PublishRecruitUI.this, R.drawable.jb_focus)));

		ktvBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
				PublishRecruitUI.this, R.drawable.ktv)));

		yezonghuiBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
				PublishRecruitUI.this, R.drawable.yzh)));

		jiubaBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				jiubaBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
						PublishRecruitUI.this, R.drawable.jb_focus)));

				ktvBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
						PublishRecruitUI.this, R.drawable.ktv)));

				yezonghuiBtn.setBackgroundDrawable(new BitmapDrawable(
						readBitMap(PublishRecruitUI.this, R.drawable.yzh)));

				jiubaBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
				ktvBtn.setTextColor(Color.parseColor("#ff404040"));
				yezonghuiBtn.setTextColor(Color.parseColor("#ff404040"));

				recruit_type = 1;
				titleEt.setAdapter(mAdapter1);
				

			}
		});
		//
		ktvBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				jiubaBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
						PublishRecruitUI.this, R.drawable.jb)));

				ktvBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
						PublishRecruitUI.this, R.drawable.ktv_focus)));

				yezonghuiBtn.setBackgroundDrawable(new BitmapDrawable(
						readBitMap(PublishRecruitUI.this, R.drawable.yzh)));

				ktvBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
				jiubaBtn.setTextColor(Color.parseColor("#ff404040"));
				yezonghuiBtn.setTextColor(Color.parseColor("#ff404040"));

				recruit_type = 2;
				titleEt.setAdapter(mAdapter2);

			}
		});
		//
		yezonghuiBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				//
				jiubaBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
						PublishRecruitUI.this, R.drawable.jb)));

				ktvBtn.setBackgroundDrawable(new BitmapDrawable(readBitMap(
						PublishRecruitUI.this, R.drawable.ktv)));

				yezonghuiBtn
						.setBackgroundDrawable(new BitmapDrawable(readBitMap(
								PublishRecruitUI.this, R.drawable.yzh_focus)));

				ktvBtn.setTextColor(Color.parseColor("#ff404040"));
				jiubaBtn.setTextColor(Color.parseColor("#ff404040"));
				yezonghuiBtn.setTextColor(Color.parseColor("#FFFFFFFF"));

				recruit_type = 3;

			}
		});

		// ///////////
		photo0 = (ImageView) this.findViewById(R.id.photo0);
		if (photo0 != null) {
			photo0.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 0;
					Intent intent = new Intent();
					intent.setClass(PublishRecruitUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}
		// ////
		photo2 = (ImageView) this.findViewById(R.id.photo2);
		if (photo2 != null) {
			photo2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 2;
					Intent intent = new Intent();
					intent.setClass(PublishRecruitUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}
		//
		photo1 = (ImageView) this.findViewById(R.id.photo1);
		if (photo1 != null) {
			photo1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 1;
					Intent intent = new Intent();
					intent.setClass(PublishRecruitUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}
		//
		photo3 = (ImageView) this.findViewById(R.id.photo3);
		if (photo3 != null) {
			photo3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 3;
					Intent intent = new Intent();
					intent.setClass(PublishRecruitUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}

		// //
		TextView zwPre = (TextView) this.findViewById(R.id.zwPre);
		TextPaint paint = zwPre.getPaint();
		paint.setFakeBoldText(true);
		//
		TextView tqPre = (TextView) this.findViewById(R.id.tqPre);
		paint = tqPre.getPaint();
		paint.setFakeBoldText(true);

	}

	private void showSuccess() {
		mSuccessDialog = new AlertDialog.Builder(this).setTitle("")
				.setMessage("发布成功,是否分享到朋友圈").setIcon(R.drawable.ic_launcher)
				.setPositiveButton("是", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						loadingHandler.sendEmptyMessage(1);
					}
				}).setNegativeButton("否", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						loadingHandler.sendEmptyMessage(1);
						// mSuccessDialog.dismiss();
					}
				}).create();
		mSuccessDialog.show();
	}

}
