package com.yedianchina.ui.activity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.sdk.platformtools.Log;
import com.yedianchina.dao.ActivityDao;
import com.yedianchina.po.ActivityPO;
import com.yedianchina.tools.BitmapUtils;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.tools.ImageTools;
import com.yedianchina.tools.UploadUtil;
import com.yedianchina.ui.CameraUI;
import com.yedianchina.ui.R;

public class PublishActivityUI extends Activity {
	private static final int SCALE = 5;// 照片缩小比例

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
	
	
	
	
	//////////////////////////////////////////////////////////////////////
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

						avatarTv.setImageBitmap(photo);

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

									imgURL0 =  serverImgName
											+ ".jpg";
									Log.e("imgURL0", "imgURL0=" + imgURL0);
								}
								if (k == 1) {
									imgURL1 =  serverImgName
											+ ".jpg";
									Log.e("imgURL1", "imgURL1=" + imgURL1);
								}
								if (k == 2) {
									imgURL2 =  serverImgName
											+ ".jpg";
									Log.e("imgURL2", "imgURL2=" + imgURL2);
								}
								if (k == 3) {
									imgURL3 =  serverImgName
											+ ".jpg";
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

						//avatarTv.setImageBitmap(smallBitmap);
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

									imgURL0 =  serverImgName
											+ ".jpg";
									Log.e("imgURL0", "imgURL0=" + imgURL0);
								}
								if (k == 1) {
									imgURL1 =  serverImgName
											+ ".jpg";
									Log.e("imgURL1", "imgURL1=" + imgURL1);
								}
								if (k == 2) {
									imgURL2 =  serverImgName
											+ ".jpg";
									Log.e("imgURL2", "imgURL2=" + imgURL2);
								}
								if (k == 3) {
									imgURL3 =  serverImgName
											+ ".jpg";
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

		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1.5);

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

	ImageView photo0;
	ImageView photo1;
	ImageView photo2;
	ImageView photo3;
	EditText themeTv;
	EditText timeEt;
	EditText addrEt;
	
	EditText descTv;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.publish_activity);

		themeTv = (EditText) this.findViewById(R.id.themeEt);
		timeEt = (EditText) this.findViewById(R.id.timeEt);
		addrEt = (EditText) this.findViewById(R.id.addrEt);
		
		descTv = (EditText) this.findViewById(R.id.descTv);
		
		

		// 返回按钮
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					PublishActivityUI.this.finish();

				}
			});

		}
		//NavigateTitle
		TextView NavigateTitle = (TextView) this.findViewById(R.id.NavigateTitle);
		if (NavigateTitle != null) {
			NavigateTitle.setText("发布活动");
		}
		
		
		

		TextView qiandaoBtn = (TextView) this.findViewById(R.id.qiandaoBtn);
		if (qiandaoBtn != null) {
			qiandaoBtn.setVisibility(View.INVISIBLE);
		}
		
		TextView saveBtn = (TextView) this.findViewById(R.id.saveBtn);
		if (saveBtn != null) {
			
			saveBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					final ActivityPO po = new ActivityPO();
					String thmeme = themeTv.getText().toString();
					if (thmeme != null) {
						po.setName(thmeme);
					}
					String activityTime = timeEt.getText().toString();
					if (activityTime != null) {
						po.setActivityTime(activityTime);
					}

					String addr = addrEt.getText().toString();
					if (addr != null) {
						po.setAddress(addr);
					}
					if (StringUtils.isNotEmpty(imgURL0)
							&& imgURL0.length() >= 5) {
						po.setPhoto0(imgURL0);
					}
					if (StringUtils.isNotEmpty(imgURL1)
							&& imgURL1.length() >= 5) {
						po.setPhoto1(imgURL1);
					}

					if (StringUtils.isNotEmpty(imgURL2)
							&& imgURL2.length() >= 5) {
						po.setPhoto2(imgURL2);
					}
					if (StringUtils.isNotEmpty(imgURL3)
							&& imgURL3.length() >= 5) {
						po.setPhoto3(imgURL3);
					}
					
					String  content=descTv.getText().toString();
					if(content!=null&&content.length()>0){
						po.setContent(content);
					}

					new Thread() {
						public void run() {
							ActivityDao.saveActivity(po);
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
					intent.setClass(PublishActivityUI.this, CameraUI.class);
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
					intent.setClass(PublishActivityUI.this, CameraUI.class);
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
					intent.setClass(PublishActivityUI.this, CameraUI.class);
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
					intent.setClass(PublishActivityUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}
		
		///////////////
		
		TextView descPreTv=(TextView)this.findViewById(R.id.descPreTv);
		TextPaint paint = descPreTv.getPaint(); 
		paint.setFakeBoldText(true);
		//addrPreEt
		TextView addrPreEt=(TextView)this.findViewById(R.id.addrPreEt);
		paint = addrPreEt.getPaint(); 
		paint.setFakeBoldText(true);
		//
		TextView timePreEt=(TextView)this.findViewById(R.id.timePreEt);
		paint = timePreEt.getPaint(); 
		paint.setFakeBoldText(true);
		//
		TextView themePreEt=(TextView)this.findViewById(R.id.themePreEt);
		paint = themePreEt.getPaint(); 
		paint.setFakeBoldText(true);

	}

	Handler saveHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				Intent intent = new Intent(action);
				intent.putExtra("updateNearByActivity", "1");
				sendBroadcast(intent);

				PublishActivityUI.this.finish();

			}
		}
	};
	public static final String action = "activity.broadcast.action";

}
