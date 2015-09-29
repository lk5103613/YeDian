package com.yedianchina.ui.i;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.dao.MerchantDao;
import com.yedianchina.po.MerchantPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.tools.UploadUtil;
import com.yedianchina.ui.CameraUI;
import com.yedianchina.ui.R;
import com.yedianchina.ui.area.CityUI;

public class IMerchantMingPianUI extends Activity {

	protected Dialog onCreateDialog() {
		// 用来获取日期和时间的
		Calendar calendar = Calendar.getInstance();

		Dialog dialog = null;

		DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker datePicker, int year, int month,
					int dayOfMonth) {

				birthdayTv.setText(year + "年" + (month + 1) + "月" + dayOfMonth
						+ "日");
			}
		};
		dialog = new DatePickerDialog(this, dateListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));

		return dialog;
	}

	// ////////////////////////////////////
	String imgURL0;
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
	// /////////////
	private String strVideoPath = "";
	String requestURL = CONSTANTS.IMG_HOST + "upload_img.php";

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
								Log.e("上传文件", "imgPath==" + imgPath);

								imgURL0 = "upload/" + serverImgName
										+ ".jpg";
								Log.e("imgURL0", "imgURL0=" + imgPath);

								// UploadUtil2.upload(path, requestURL, file);
								// appProduct/saveImg

								// try {
								// //ProductDao dao = new ProductDao();
								// String smallPath="mm_goods_small/" + path +
								// ".jpg";
								// String ios_big_path="ios/" + path + ".jpg";
								// path = "mm_goods/" + path + ".jpg";
								// Log.e("##########################" + token,
								// "path:" + path);
								// //dao.saveImg(token, path,smallPath, uid +
								// "",ios_big_path);
								//
								// } catch (Exception e) {
								// e.printStackTrace();
								// }

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

	private Long pk;
	private MerchantPO merchantPO;

	TextView accountTv;
	EditText nicknameTv;
	TextView sexTv;
	TextView birthdayTv;
	EditText heightTv;

	EditText weightTv;
	TextView locTv;
	TextView hometownTv;
	TextView qmTv;
	TextView uploadAvatarTv;

	TextView manTxt;
	TextView manImg;

	TextView girlTxt;
	TextView girlImg;

	int gender = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.i_merchant_mingpian);
		avatarTv = (ImageView) this.findViewById(R.id.avatarTv);
		accountTv = (TextView) this.findViewById(R.id.accountTv);
		nicknameTv = (EditText) this.findViewById(R.id.nicknameTv);

		sexTv = (TextView) this.findViewById(R.id.sexTv);
		birthdayTv = (TextView) this.findViewById(R.id.birthdayTv);
		birthdayTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Dialog d = onCreateDialog();
				d.show();

			}
		});

		heightTv = (EditText) this.findViewById(R.id.heightTv);

		weightTv = (EditText) this.findViewById(R.id.weightTv);
		locTv = (TextView) this.findViewById(R.id.locTv);
		hometownTv = (TextView) this.findViewById(R.id.hometownTv);
		qmTv = (TextView) this.findViewById(R.id.qmTv);

		gender = 1;

		// 男
		manTxt = (TextView) this.findViewById(R.id.manTxt);
		manImg = (TextView) this.findViewById(R.id.manImg);
		// 女
		girlTxt = (TextView) this.findViewById(R.id.girlTxt);
		girlImg = (TextView) this.findViewById(R.id.girlImg);

		// 点击设置男人
		if (manTxt != null) {
			manTxt.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					manImg.setBackgroundResource(R.drawable.red_dot);
					girlImg.setBackgroundResource(R.drawable.gray_dot);
					gender = 1;

				}
			});
		}
		if (manImg != null) {
			manImg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					manImg.setBackgroundResource(R.drawable.red_dot);
					girlImg.setBackgroundResource(R.drawable.gray_dot);
					gender = 1;
				}
			});
		}

		// 点击设置女人
		if (girlTxt != null) {
			girlTxt.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					manImg.setBackgroundResource(R.drawable.gray_dot);
					girlImg.setBackgroundResource(R.drawable.red_dot);
					gender = 2;

				}
			});
		}
		if (girlImg != null) {
			girlImg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					manImg.setBackgroundResource(R.drawable.gray_dot);
					girlImg.setBackgroundResource(R.drawable.red_dot);
					gender = 2;
				}
			});
		}

		SharedPreferences preferences = getSharedPreferences(CONSTANTS.YEDIANCHINA_USER_INFO,
				Activity.MODE_PRIVATE);

		pk = preferences.getLong("uid", 0);

		new Thread() {
			public void run() {

				updateCurrendData();

				loadingHandler.sendEmptyMessage(1);

			}
		}.start();

		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					IMerchantMingPianUI.this.finish();

				}
			});
		}

	

		uploadAvatarTv = (TextView) this.findViewById(R.id.uploadAvatarTv);

		if (uploadAvatarTv != null) {
			uploadAvatarTv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(IMerchantMingPianUI.this, CameraUI.class);
					startActivityForResult(intent, 101);

				}
			});
		}

		TextView okBtn = (TextView) this.findViewById(R.id.qiandaoBtn);

		okBtn.setText("确认修改");
		if (okBtn != null) {
			okBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {

				}
			});
		}

	}

	public void updateCurrendData() {
		
		merchantPO=MerchantDao.loadMerchantInfo(new Long(pk));

	}

	Handler loadingHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				
				setNearbyMingpianData();

			}
		};
	};

	void setNearbyMingpianData() {

		String logo = merchantPO.getLogo();
		if (logo != null && logo.length() > 5) {

			Log.e("头像", logo);
			avatarTv.setTag(logo);

			ImageLoader.getInstance().displayImage(logo, avatarTv);
		}

		accountTv.setText("" + pk);

		String nickname = merchantPO.getName();
		if (nickname != null && nickname.length() > 0) {
			nicknameTv.setText(nickname);
		}

		

		TextView birthdayTv;
		String linkman = merchantPO.getLinkman();//联系人
		if (linkman != null && linkman.length() > 0) {
			heightTv.setText(linkman);
		}
		//

		String linkMp = merchantPO.getMp();
		if (linkMp != null && linkMp.length() > 0) {
			weightTv.setText("" + linkMp);
		}

		addr = (TextView) this.findViewById(R.id.addr);
		addr.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(IMerchantMingPianUI.this,CityUI.class);
				startActivity(intent);
				
			}
		});
		
		
		
		
		TextView hometownTv;
		EditText qmTv = (EditText) this.findViewById(R.id.qmTv);
		String desc=merchantPO.getDesc();
		qmTv.setText(desc);

	}

	TextView addr;

}

