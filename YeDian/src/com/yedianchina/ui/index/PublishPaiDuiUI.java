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

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.platformtools.Log;

import com.yedianchina.dao.PaiDuiDao;
import com.yedianchina.po.PaiDuiPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.tools.UploadUtil;
import com.yedianchina.ui.CameraUI;
import com.yedianchina.ui.R;

public class PublishPaiDuiUI extends Activity {
	String imgURL0;
	String imgURL1;
	String imgURL2;
	String imgURL3;
	//
	String imgURL4;
	String imgURL5;
	String imgURL6;
	String imgURL7;
	
	
	
	
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
	ImageView avatarTv;
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
	String serverImgName;

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
				//
				if (k == 4) {
					photo4.setImageBitmap(photo);
				}
				if (k == 5) {
					photo5.setImageBitmap(photo);
				}
				if (k == 6) {
					photo6.setImageBitmap(photo);
				}
				if (k == 7) {
					photo7.setImageBitmap(photo);
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
								
								
//								imgPath = UploadUtil.uploadFile(file,
//										requestURL);
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
								////
								if (k == 4) {
									imgURL4 = "upload/" + serverImgName
											+ ".jpg";
								}
								if (k == 5) {
									imgURL5 = "upload/" + serverImgName
											+ ".jpg";
								}
								if (k == 6) {
									imgURL6 = "upload/" + serverImgName
											+ ".jpg";
								}
								if (k == 7) {
									imgURL7 = "upload/" + serverImgName
											+ ".jpg";
								}
								
								
								
								

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

		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

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

	ImageView photo0;
	ImageView photo1;
	ImageView photo2;
	ImageView photo3;
	//
	ImageView photo4;
	ImageView photo5;
	ImageView photo6;
	ImageView photo7;
	
	
	
	
	
	EditText themeTv;
	EditText timeEt;
	EditText addrEt;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.publish_paidui);

		themeTv = (EditText) this.findViewById(R.id.themeEt);
		timeEt = (EditText) this.findViewById(R.id.timeEt);
		addrEt = (EditText) this.findViewById(R.id.addrEt);

		// 返回按钮
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					PublishPaiDuiUI.this.finish();

				}
			});

		}

		TextView qiandaoBtn = (TextView) this.findViewById(R.id.qiandaoBtn);
		if (qiandaoBtn != null) {
			qiandaoBtn.setText("提交");
			qiandaoBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					
					final PaiDuiPO po = new PaiDuiPO();
					String thmeme = themeTv.getText().toString();
					if (thmeme != null) {
						po.setName(thmeme);
					}
					String time = timeEt.getText().toString();
					if (time != null) {
						po.setTime(time);
					}

					String addr = addrEt.getText().toString();
					if (addr != null) {
						po.setAddr(addr);
					}
					
					if(StringUtils.isNotEmpty(imgURL0)&&imgURL0.length()>=5){
						po.setPhoto0(imgURL0);
					}
					if(StringUtils.isNotEmpty(imgURL1)&&imgURL1.length()>=5){
						po.setPhoto1(imgURL1);
					}
					
					if(StringUtils.isNotEmpty(imgURL2)&&imgURL2.length()>=5){
						po.setPhoto2(imgURL2);
					}
					if(StringUtils.isNotEmpty(imgURL3)&&imgURL3.length()>=5){
						po.setPhoto3(imgURL3);
					}
					

					new Thread() {
						public void run() {
							PaiDuiDao.savePaidui(po);
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
					intent.setClass(PublishPaiDuiUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}
		// ////
		photo1 = (ImageView) this.findViewById(R.id.photo1);
		if (photo1 != null) {
			photo1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 1;
					Intent intent = new Intent();
					intent.setClass(PublishPaiDuiUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}
		//
		photo2 = (ImageView) this.findViewById(R.id.photo2);
		if (photo2 != null) {
			photo2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 2;
					Intent intent = new Intent();
					intent.setClass(PublishPaiDuiUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}

		photo3 = (ImageView) this.findViewById(R.id.photo3);
		if (photo3 != null) {
			photo3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 3;
					Intent intent = new Intent();
					intent.setClass(PublishPaiDuiUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}
		//
		photo4 = (ImageView) this.findViewById(R.id.photo4);
		if (photo4 != null) {
			photo4.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 4;
					Intent intent = new Intent();
					intent.setClass(PublishPaiDuiUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}
		//
		photo5 = (ImageView) this.findViewById(R.id.photo5);
		if (photo5 != null) {
			photo5.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 5;
					Intent intent = new Intent();
					intent.setClass(PublishPaiDuiUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}

		photo6 = (ImageView) this.findViewById(R.id.photo6);
		if (photo6 != null) {
			photo6.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 6;
					Intent intent = new Intent();
					intent.setClass(PublishPaiDuiUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}
		
		photo7 = (ImageView) this.findViewById(R.id.photo7);
		if (photo7 != null) {
			photo7.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					k = 7;
					Intent intent = new Intent();
					intent.setClass(PublishPaiDuiUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}
		
		
		
		
		
		
		
		//////////////////////////

	}

	Handler saveHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				Intent intent = new Intent(action);
				intent.putExtra("updatePaiduiListFn", "1");
				sendBroadcast(intent);
				PublishPaiDuiUI.this.finish();

			}
		}
	};
	public static final String action = "updatePaiduiListFn.broadcast.action";

}

