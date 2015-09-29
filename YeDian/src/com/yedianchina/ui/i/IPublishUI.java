package com.yedianchina.ui.i;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.platformtools.Log;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.tools.UploadUtil;
import com.yedianchina.ui.CameraUI;
import com.yedianchina.ui.R;

public class IPublishUI extends Activity {
	String imgURL0;
	String imgURL1;
	String imgURL2;
	String imgURL3;
	int k=0;
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
String requestURL = CONSTANTS.IMG_HOST + "UpLoadReportServlet";
	
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
					CompressFormat format= Bitmap.CompressFormat.JPEG;
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
				photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0
																		// -
																		// 100)ѹ���ļ�
																		// final
																		// LinearLayout
																		// layout=(LinearLayout)this.findViewById(R.id.ll8);
				// ImageView tmp=new ImageView(this);
				// tmp.setImageBitmap(photo);
				// layout.addView(tmp);
				 
				avatarTv.setImageBitmap(photo);

				 

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
								
								//imgPath = UploadUtil.uploadFile(file,
								//requestURL);
								Log.e("上传文件", "imgPath=="+imgPath);

								 
									imgURL0 = imgPath;
									Log.e("imgURL0", "imgURL0="+imgPath);
							 
								
								

							

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
	
	TextView photo0;
	TextView photo1;
	TextView photo2;
	TextView photo3;
	EditText contentEt;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.i_publish);
		
		
		//返回按钮
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					IPublishUI.this.finish();

				}
			});

		}
	
	 
		TextView qiandaoBtn = (TextView) this.findViewById(R.id.qiandaoBtn);
		if (qiandaoBtn != null) {
			qiandaoBtn.setVisibility(View.INVISIBLE);

		}
		
		photo0 = (TextView) this.findViewById(R.id.imgBtn);
		if(photo0!=null){
			photo0.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(IPublishUI.this, CameraUI.class);
					startActivityForResult(intent, 101);
					
				}
			});
		}
		contentEt=(EditText)this.findViewById(R.id.contentEt);
		
	}

}

