package com.yedianchina.ui.i;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.easemob.EMCallBack;
import com.easemob.chat.Constant;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.domain.User;
import com.easemob.util.HanziToPinyin;
import com.example.widget.NumericWheelAdapter;
import com.example.widget.OnWheelChangedListener;
import com.example.widget.WheelView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.dao.MingpingDao;
import com.yedianchina.dao.UserDao;
import com.yedianchina.po.UserPO;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.tools.UploadUtil;
import com.yedianchina.ui.CameraUI;
import com.yedianchina.ui.R;
import com.yedianchina.ui.YeDianChinaApplication;
import com.yedianchina.ui.area.CityUI;

public class IMingpianUI extends Activity {

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
	private String serverImgName;
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
	//String requestURL ="http://42.96.202.33/upload_img.php";

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

	String city_name = "";
	String home_town = "";

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("resultCode=", "" + resultCode);
		Log.e("requestCode=", "" + requestCode);

		if (resultCode == 888) {

			xc();
		}
		if (resultCode == 1024) {
			city_name = data.getExtras().getString("mCurrentProviceName");
			Log.e("当前选择的城市", "mCurrentProviceName=" + city_name);
			locTv.setText(city_name);
			return;
		}

		if (resultCode == 1025) {
			home_town = data.getExtras().getString("mCurrentProviceName");
			Log.e("故乡", "home_town=" + home_town);
			hometownTv.setText(home_town);
			return;
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
								Log.e("上传文件", "imgPath==" + serverImgName);

								imgURL0 = serverImgName;
								imgURL0 = "upload/" + serverImgName;

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

	private Long pk;
	private UserPO userPO;

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

	SelectBirthday birth;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.i_mingpian);
		avatarTv = (ImageView) this.findViewById(R.id.avatarTv);
		accountTv = (TextView) this.findViewById(R.id.accountTv);
		nicknameTv = (EditText) this.findViewById(R.id.nicknameTv);

		sexTv = (TextView) this.findViewById(R.id.sexTv);
		birthdayTv = (TextView) this.findViewById(R.id.birthdayTv);
		birthdayTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Dialog d = onCreateDialog();
				// d.show();
				birth = new SelectBirthday(IMingpianUI.this);
				birth.showAtLocation(IMingpianUI.this.findViewById(R.id.root),
						Gravity.BOTTOM, 0, 0);

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

		SharedPreferences preferences = getSharedPreferences(
				CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);

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
					IMingpianUI.this.finish();

				}
			});
		}

		uploadAvatarTv = (TextView) this.findViewById(R.id.uploadAvatarTv);

		if (uploadAvatarTv != null) {
			uploadAvatarTv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(IMingpianUI.this, CameraUI.class);
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

					SharedPreferences preferences = getSharedPreferences(
							CONSTANTS.YEDIANCHINA_USER_INFO,
							Activity.MODE_PRIVATE);
					final Long loginUID = preferences.getLong("uid", 0);

					String nickname = nicknameTv.getText().toString();
					if (nickname != null && nickname.length() > 0) {
						userPO.setNickname(nickname);
					} else {
						Toast toast = Toast.makeText(IMingpianUI.this, "请输入昵称",
								Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return;
					}

					String qm = qmTv.getText().toString();
					if (qm != null && qm.length() > 0) {
						userPO.setQm(qm);
					} else {
						userPO.setQm("");
					}
					if (city_name != null && city_name.length() > 0) {
						userPO.setCity_name(city_name);
					} else {
						userPO.setCity_name("");
					}

					if (birthday != null && birthday.length() > 5) {
						userPO.setBirthday(birthday);
					}
					
					imgURL0 = "upload/" + serverImgName;
					if(serverImgName!=null&&serverImgName.length()>5){
						userPO.setAvatar(imgURL0);
					}
					
					

					new Thread() {
						public void run() {
							try {

								MingpingDao.updateMingPian(loginUID, userPO);

								reg2ChatServerHandler.sendEmptyMessage(1);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();

				}
			});
		}

	}

	int id;

	protected void onDestroy() {

		super.onDestroy();
	};

	private boolean progressShow;
	Handler reg2ChatServerHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {

				String nickname = nicknameTv.getText().toString();// 保持昵称本地

				SharedPreferences preferences = getSharedPreferences(
						CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString(CONSTANTS.NICKNAME, nickname);

				editor.commit(); // 一定要记得提交

				progressShow = true;
				final ProgressDialog pd = new ProgressDialog(IMingpianUI.this);
				pd.setCancelable(true);
				pd.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						progressShow = false;
					}
				});
				pd.setMessage("正在登陆...");
				pd.show();
				// 调用sdk登陆方法登陆聊天服务器

				Log.e("登录",
						"正在登陆 。。。。。。" + userPO.getMp() + "   pwd="
								+ userPO.getPwd());
				EMChatManager.getInstance().login(userPO.getMp(),
						userPO.getPwd(), new EMCallBack() {

							@Override
							public void onSuccess() {
								if (!progressShow) {
									return;
								}
								// 登陆成功，保存用户名密码
								YeDianChinaApplication.getInstance()
										.setUserName(userPO.getMp());
								YeDianChinaApplication.getInstance()
										.setPassword(userPO.getPwd());
								runOnUiThread(new Runnable() {
									public void run() {
										pd.setMessage("正在获取好友和群聊列表...");
									}
								});
								try {
									// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
									List<String> usernames = EMChatManager
											.getInstance()
											.getContactUserNames();
									Map<String, User> userlist = new HashMap<String, User>();
									for (String username : usernames) {
										User user = new User();
										user.setUsername(username);
										setUserHearder(username, user);
										userlist.put(username, user);
									}
									// 添加user"申请与通知"
									User newFriends = new User();
									newFriends
											.setUsername(Constant.NEW_FRIENDS_USERNAME);
									newFriends.setNick("申请与通知");
									newFriends.setHeader("");
									userlist.put(Constant.NEW_FRIENDS_USERNAME,
											newFriends);
									// 添加"群聊"
									User groupUser = new User();
									groupUser
											.setUsername(Constant.GROUP_USERNAME);
									groupUser.setNick("群聊");
									groupUser.setHeader("");
									userlist.put(Constant.GROUP_USERNAME,
											groupUser);

									// 存入内存
									YeDianChinaApplication.getInstance()
											.setContactList(userlist);
									// 存入db
									com.easemob.chat.db.UserDao dao = new com.easemob.chat.db.UserDao(
											IMingpianUI.this);
									List<User> users = new ArrayList<User>(
											userlist.values());
									dao.saveContactList(users);

									// 获取群聊列表,sdk会把群组存入到EMGroupManager和db中
									EMGroupManager.getInstance()
											.getGroupsFromServer();
								} catch (Exception e) {
								}

								if (!IMingpianUI.this.isFinishing())
									pd.dismiss();
								// 进入主页面
								// startActivity(new Intent(LoginUI.this,
								// MainActivity.class));
								finish();
							}

							@Override
							public void onProgress(int progress, String status) {

							}

							@Override
							public void onError(int code, final String message) {
								if (!progressShow) {
									return;
								}
								runOnUiThread(new Runnable() {
									public void run() {
										pd.dismiss();
										Log.e("message", message);

										if (message
												.indexOf("not support the capital letters") != -1) {
											Toast.makeText(
													getApplicationContext(),
													"用户名不支持大写字母", 0).show();
										} else {
											Toast.makeText(
													getApplicationContext(),
													"登录失败: " + message, 0)
													.show();
										}

									}
								});
							}
						});

			}
		};
	};

	public void updateCurrendData() {
		userPO = UserDao.loadUserInfo(new Long(pk));

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

		String logo = userPO.getAvatar();
		if (logo != null && logo.length() > 5) {

			Log.e("头像", logo);
			avatarTv.setTag(logo);

			ImageLoader.getInstance().displayImage(logo, avatarTv);
		}

		accountTv.setText("" + pk);

		String nickname = userPO.getNickname();
		if (nickname != null && nickname.length() > 0) {
			nicknameTv.setText(nickname);
		}

		int gender = userPO.getGender();
		if (gender == 1) {
			// sexTv.setText("男");
			manImg.setBackgroundResource(R.drawable.red_dot);
			girlImg.setBackgroundResource(R.drawable.gray_dot);
		}
		if (gender == 2) {
			// sexTv.setText("女");
			manImg.setBackgroundResource(R.drawable.gray_dot);
			girlImg.setBackgroundResource(R.drawable.red_dot);
		}

		String height = userPO.getHeight();
		if (height != null && height.length() > 0) {
			heightTv.setText("");
		}
		//

		String weight = userPO.getWeight();
		if (weight != null && weight.length() > 0) {
			weightTv.setText("" + weight);
		}

		city_name = userPO.getCity_name();
		if (city_name != null && city_name.length() > 0) {
			locTv.setText(city_name);
		}
		locTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(IMingpianUI.this, CityUI.class);
				startActivityForResult(intent, 1024);

			}
		});

		hometownTv = (TextView) this.findViewById(R.id.hometownTv);
		String home_town = userPO.getHome_town();
		if (home_town != null && home_town.length() > 0) {
			hometownTv.setText(home_town);
		}
		hometownTv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(IMingpianUI.this, CityUI.class);
				startActivityForResult(intent, 1025);// 故乡

			}
		});

		EditText qmTv = (EditText) this.findViewById(R.id.qmTv);
		String qm = userPO.getQm();
		if (qm != null && qm.length() > 0) {
			qmTv.setText(qm);
		}

		String birthday = userPO.getBirthday();
		if (birthday != null && birthday.length() > 5) {
			birthdayTv.setText(birthday);
		}

	}

	TextView addr;

	protected void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	public class SelectBirthday extends PopupWindow implements OnClickListener {

		private Activity mContext;
		private View mMenuView;
		private ViewFlipper viewfipper;
		private Button btn_submit, btn_cancel;
		private String age;
		private DateNumericAdapter monthAdapter, dayAdapter, yearAdapter;
		private WheelView year, month, day;
		private int mCurYear = 80, mCurMonth = 5, mCurDay = 14;
		private String[] dateType;

		public SelectBirthday(Activity context) {
			super(context);
			mContext = context;
			this.age = "2012-9-25";
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(R.layout.birthday, null);
			viewfipper = new ViewFlipper(context);
			viewfipper.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			year = (WheelView) mMenuView.findViewById(R.id.year);
			month = (WheelView) mMenuView.findViewById(R.id.month);
			day = (WheelView) mMenuView.findViewById(R.id.day);
			btn_submit = (Button) mMenuView.findViewById(R.id.submit);
			btn_cancel = (Button) mMenuView.findViewById(R.id.cancel);
			btn_submit.setOnClickListener(this);
			btn_cancel.setOnClickListener(this);
			Calendar calendar = Calendar.getInstance();
			OnWheelChangedListener listener = new OnWheelChangedListener() {
				public void onChanged(WheelView wheel, int oldValue,
						int newValue) {
					updateDays(year, month, day);

				}
			};
			int curYear = calendar.get(Calendar.YEAR);
			if (age != null && age.contains("-")) {
				String str[] = age.split("-");
				mCurYear = 100 - (curYear - Integer.parseInt(str[0]));
				mCurMonth = Integer.parseInt(str[1]) - 1;
				mCurDay = Integer.parseInt(str[2]) - 1;
				;
			}
			dateType = mContext.getResources().getStringArray(R.array.date);
			monthAdapter = new DateNumericAdapter(context, 1, 12, 5);
			monthAdapter.setTextType(dateType[1]);
			month.setViewAdapter(monthAdapter);
			month.setCurrentItem(mCurMonth);
			month.addChangingListener(listener);
			// year

			yearAdapter = new DateNumericAdapter(context, curYear - 100,
					curYear + 100, 100 - 20);
			yearAdapter.setTextType(dateType[0]);
			year.setViewAdapter(yearAdapter);
			year.setCurrentItem(mCurYear);
			year.addChangingListener(listener);
			// day

			updateDays(year, month, day);
			day.setCurrentItem(mCurDay);
			updateDays(year, month, day);
			day.addChangingListener(listener);

			viewfipper.addView(mMenuView);
			viewfipper.setFlipInterval(6000000);
			this.setContentView(viewfipper);
			this.setWidth(LayoutParams.FILL_PARENT);
			this.setHeight(LayoutParams.WRAP_CONTENT);
			this.setFocusable(true);
			ColorDrawable dw = new ColorDrawable(0x00000000);
			this.setBackgroundDrawable(dw);
			this.update();

		}

		@Override
		public void showAtLocation(View parent, int gravity, int x, int y) {
			super.showAtLocation(parent, gravity, x, y);
			viewfipper.startFlipping();
		}

		private void updateDays(WheelView year, WheelView month, WheelView day) {

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR,
					calendar.get(Calendar.YEAR) + year.getCurrentItem());
			calendar.set(Calendar.MONTH, month.getCurrentItem());

			int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			dayAdapter = new DateNumericAdapter(mContext, 1, maxDays,
					calendar.get(Calendar.DAY_OF_MONTH) - 1);
			dayAdapter.setTextType(dateType[2]);
			day.setViewAdapter(dayAdapter);
			int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
			day.setCurrentItem(curDay - 1, true);
			int years = calendar.get(Calendar.YEAR) - 100;
			age = years + "-" + (month.getCurrentItem() + 1) + "-"
					+ (day.getCurrentItem() + 1);
		}

		/**
		 * Adapter for numeric wheels. Highlights the current value.
		 */
		private class DateNumericAdapter extends NumericWheelAdapter {
			// Index of current item
			int currentItem;
			// Index of item to be highlighted
			int currentValue;

			/**
			 * Constructor
			 */
			public DateNumericAdapter(Context context, int minValue,
					int maxValue, int current) {
				super(context, minValue, maxValue);
				this.currentValue = current;
				setTextSize(24);
			}

			protected void configureTextView(TextView view) {
				super.configureTextView(view);
				view.setTypeface(Typeface.SANS_SERIF);
			}

			public CharSequence getItemText(int index) {
				currentItem = index;
				return super.getItemText(index);
			}

		}

		public void onClick(View v) {
			System.out.println("age=" + age);
			birthday = age;
			birthdayTv.setText(age);

			this.dismiss();
		}

	}

	String birthday = "";

}
