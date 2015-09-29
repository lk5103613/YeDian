package com.yedianchina.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * APP更新
 * 
 * 
 * @version 创建时间：2013-12-11 下午4:45:14
 * 
 */
public class BaseSplashUI extends Activity {
	// 检测更新按钮
	private Button cButtonCheckUpdate;
	// 更新进度显示
	private ProgressBar cProgressBarUpdate;
	// 更新显示框
	private Dialog cDialogUpdateProgress;
	// 存放APK下载路径
	private String cSaveApkPath;
	// 是否取消更新
	private boolean isCancelUpdate;
	// 下载进度
	private int cProgress;
	// handler消息处理判断,进度处理
	private final int DOWNLOADING = 1;
	// handler消息处理判断，下载完成处理
	private final int DOWNLOADFINISH = 2;
	// handler消息处理判断，下载失败处理
	private final int DOWNLOADFAIL = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		findview();
//		register();
	}

	/**
	 * 找控件
	 * 
	 * @time 注释时间：2013-12-11 下午4:45:07
	 */
//	private void findview() {
//		cButtonCheckUpdate = (Button) findViewById(R.id.btn_check);
//	}

	/**
	 * 设置监听
	 * 
	 * @time 注释时间：2013-12-11 下午4:45:01
	 */
	private void register() {
		cButtonCheckUpdate
				.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {
						showDialogChoice();
					}

				});

	}

	/**
	 * 更新提示
	 * 
	 * @time 注释时间：2013-12-11 下午4:44:52
	 */
	private void showDialogChoice() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("更新提示:");
		builder.setMessage("检测到新版本，是否立即更新?");
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 显示下载进度
				showUpdateProgress();
			}
		});
		builder.setNegativeButton("稍后更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		// 是否更新选择框
		Dialog cDialogUpdate = builder.create();
		cDialogUpdate.show();
	}

	/**
	 * 显示下载进度
	 * 
	 * @time 注释时间：2013-12-11 下午4:44:44
	 */
	public void showUpdateProgress() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("正在更新...");
		View v = LayoutInflater.from(this).inflate(R.layout.update_progress,
				null);
		cProgressBarUpdate = (ProgressBar) v.findViewById(R.id.pb_update);
		builder.setView(v);
		builder.setNegativeButton("取消更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				cDialogUpdateProgress.cancel();

			}
		});
		cDialogUpdateProgress = builder.create();
		cDialogUpdateProgress.show();
		downloadAPK();
	}

	/**
	 * 下载APK文件
	 * 
	 * @time 注释时间：2013-12-11 下午5:15:31
	 */
	private void downloadAPK() {
		new downloadApkThread().start();
	}

	
	private class downloadApkThread extends Thread {
		HttpURLConnection conn;

		@Override
		public void run() {
			try {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获取存储卡路径
					String path = Environment.getExternalStorageDirectory()
							+ "/";
					// APK保存路径
					cSaveApkPath = path + "AppUpdateDownload";
					// APK下载的地址
					URL url = new URL(DownloadUrl.downloadURL);
					conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 取得数据总大小
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();
					File file = new File(cSaveApkPath);
					if (!file.exists()) {
						// 建立文件夹
						file.mkdir();
					}
					// 建立文件
					File apkFile = new File(cSaveApkPath, "UpdateApp.apk");
					// 设置需要写入的文件
					FileOutputStream fos = new FileOutputStream(apkFile);
					// 当前下载的长度
					int count = 0;
					// 缓冲区
					byte buf[] = new byte[1024];
					do {
						// 每次读取的长度
						int numRead = is.read(buf);
						// 总共读取的长度
						count += numRead;
						// 下载进度
						cProgress = (int) (((float) count / length) * 100);
						// 设置显示进度
						cHandler.sendEmptyMessage(DOWNLOADING);
						if (numRead <= 0) {
							// 下载完成，开始安装APK
							cHandler.sendEmptyMessage(DOWNLOADFINISH);
							break;
						}
						// 写入文件中
						fos.write(buf, 0, numRead);
					} while (!isCancelUpdate);
					// 关闭流
					fos.close();
					is.close();
					// 如果点击了取消下载按钮，则删除已经下载的文件
					if (isCancelUpdate) {
						apkFile.delete();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				cHandler.sendEmptyMessage(DOWNLOADFAIL);
			} finally {
				// 关闭升级框
				cDialogUpdateProgress.cancel();
				// 断开连接
				conn.disconnect();
			}
		}
	}

	/**
	 * 处理下载过程中的信息
	 */
	private Handler cHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case DOWNLOADING:
				cProgressBarUpdate.setProgress(cProgress);
				break;
			case DOWNLOADFINISH:
				installAPK();
				break;
			case DOWNLOADFAIL:
				Toast.makeText(BaseSplashUI.this, "下载失败", Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 安装下载的APK文件
	 * 
	 * @time 注释时间：2013-12-11 下午6:11:40
	 */
	private void installAPK() {
		// 找文件
		File apkFile = new File(cSaveApkPath, "UpdateApp.apk");
		// 如果文件不存在，则直接返回
		if (!apkFile.exists()) {
			return;
		}
		// 设置跳转到安装界面
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "关于").setIcon(

		android.R.drawable.ic_menu_info_details);

		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "帮助").setIcon(

		android.R.drawable.ic_menu_help);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			new AlertDialog.Builder(this)
					.setMessage(
							"上海道宽代驾")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									// 按钮事件
								}
							}).setIcon(android.R.drawable.ic_menu_info_details)
					.setTitle("作者").show();
			break;

		case Menu.FIRST + 2:

//			new AlertDialog.Builder(this)
//					.setMessage("使用过程中如有问题或建议\n请发邮件至caiyoufei@looip.cn")
//					.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//								public void onClick(
//										DialogInterface dialoginterface, int i) {
//									// 按钮事件
//								}
//							}).setTitle("帮助")
//					.setIcon(android.R.drawable.ic_menu_help).show();
			break;
		}
		return false;
	}
}
