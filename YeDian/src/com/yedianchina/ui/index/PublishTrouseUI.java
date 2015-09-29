package com.yedianchina.ui.index;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.platformtools.Log;
import com.yedianchina.dao.PublishErshouShebeiDao;
import com.yedianchina.po.ErshoushebeiPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.tools.T;
import com.yedianchina.tools.UploadUtil;
import com.yedianchina.ui.CameraUI;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.R;

public class PublishTrouseUI extends CommonActivity {

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

	// ///////////
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
	private File mRecAudioFile;
	private String bigPic;

	private String strVideoPath = "";
	String requestURL = CONSTANTS.IMG_HOST + "yw_uploadify.php";

	public void xc() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, 889);

	}
	String  serverImgName;

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

		// ��ȡ�������ͼƬ
		if (requestCode == PHOTOZOOM) {
			startPhotoZoom(data.getData());
		}
		// ������
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
				zoomImage(photo, 100, 100);

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

								UploadUtil
										.post(file, requestURL, serverImgName);
								
								Log.e("上传文件", "imgPath==" + imgPath);
								if (k == 0) {
									imgURL0 = "upload/" + serverImgName
											+ ".jpg";
								}
								if (k == 1) {
									imgURL1 = "upload/" + serverImgName
											+ ".jpg";
								}
								if (k == 2) {
									imgURL2 = "upload/" + serverImgName
											+ ".jpg";
								}
								if (k == 3) {
									imgURL3 = "upload/" + serverImgName
											+ ".jpg";
								}

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
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 64);
		intent.putExtra("outputY", 64);
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

	EditText nameEt;
	EditText descEt;
	EditText priceEt;
	EditText linkmanEt;

	EditText linkMpEt;
	EditText emailEt;
	EditText addrEt;

	ImageView photo0;
	ImageView photo1;
	ImageView photo2;
	ImageView photo3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.publish_ershoushebei);
		nameEt = (EditText) this.findViewById(R.id.nameEt);
		descEt = (EditText) this.findViewById(R.id.descEt);
		priceEt = (EditText) this.findViewById(R.id.priceEt);

		linkmanEt = (EditText) this.findViewById(R.id.linkmanEt);
		linkMpEt = (EditText) this.findViewById(R.id.linkMpEt);
		emailEt = (EditText) this.findViewById(R.id.emailEt);
		addrEt = (EditText) this.findViewById(R.id.addrEt);

		TextView NavigateTitle = (TextView) this
				.findViewById(R.id.NavigateTitle);
		if (NavigateTitle != null) {
			NavigateTitle.setText("二手设备");
		}

		TextView okBtn = (TextView) this.findViewById(R.id.qiandaoBtn);
		okBtn.setText("提交");
		if (okBtn != null) {
			okBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					new Thread() {
						public void run() {
							ErshoushebeiPO po = new ErshoushebeiPO();
							String name = nameEt.getText().toString();
							po.setName(name);

							String desc = descEt.getText().toString();
							po.setDesc(desc);

							String price = priceEt.getText().toString();
							po.setPrice(price);

							String linkman = linkmanEt.getText().toString();
							po.setLinkman(linkman);
							String linkMp = linkMpEt.getText().toString();
							po.setLinkmp(linkMp);
							String email = emailEt.getText().toString();
							po.setEmail(email);
							String addr = addrEt.getText().toString();
							po.setAddr(addr);
							po.setFlag("2");//2:服装

							if (imgURL0 != null && imgURL0.length() > 5) {
								po.setImgURL0(imgURL0);
							} else {
								po.setImgURL0("");
							}
							if (imgURL1 != null && imgURL1.length() > 5) {
								po.setImgURL1(imgURL1);
							} else {
								po.setImgURL1("");
							}
							if (imgURL2 != null && imgURL2.length() > 5) {
								po.setImgURL2(imgURL2);
							} else {
								po.setImgURL2("");
							}
							if (imgURL3 != null && imgURL3.length() > 5) {
								po.setImgURL3(imgURL3);
							} else {
								po.setImgURL3("");
							}

							PublishErshouShebeiDao.saveErshoushebei(po);

							saveHandler.sendEmptyMessage(1);

						}
					}.start();

				}
			});
		}
		photo0 = (ImageView) this.findViewById(R.id.photo0);
		if (photo0 != null) {
			photo0.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 0;
					Intent intent = new Intent();
					intent.setClass(PublishTrouseUI.this, CameraUI.class);
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
					intent.setClass(PublishTrouseUI.this, CameraUI.class);
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
					intent.setClass(PublishTrouseUI.this, CameraUI.class);
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
					intent.setClass(PublishTrouseUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}
		
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					finish();

				}
			});

		}
		

	}

	private Handler saveHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				T.showShort(PublishTrouseUI.this, "发布成功");
				new Thread() {
					public void run() {
						try {
							Thread.sleep(2);
							closeHandler.sendEmptyMessage(1);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			}

		}

	};

	private Handler closeHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				Intent intent = new Intent(action);
				intent.putExtra("updateTrouse", "1");
				sendBroadcast(intent);

				PublishTrouseUI.this.finish();
			}
		}
	};
	public static final String action = "trouselist.broadcast.action";
	

}
