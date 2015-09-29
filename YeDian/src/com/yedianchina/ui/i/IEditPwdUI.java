package com.yedianchina.ui.i;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yedianchina.dao.LoginUserDao;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.R;

public class IEditPwdUI extends CommonActivity {
	EditText oldPwd;
	EditText newPwd;
	EditText newPwd2;
	
	String oldPwdStr;
	String newPwdStr;
	String newPwd2Str;
	
	int code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.edit_pwd);
		
		oldPwd = (EditText) findViewById(R.id.oldPwd);
		
		newPwd = (EditText) findViewById(R.id.newPwd);
		
		newPwd2 = (EditText) findViewById(R.id.newPwd2);
		
		TextView ok = (TextView) findViewById(R.id.ok);
		if(ok!=null){
			ok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					oldPwdStr=oldPwd.getText().toString();
					if(oldPwdStr.equals("请输入原始密码")||oldPwdStr.length()==0){
						Toast toast = Toast.makeText(getApplicationContext(),
								"请输入原始密码", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return ;
					}
					newPwdStr=newPwd.getText().toString();
					if(newPwdStr.equals("请输入新密码")||oldPwdStr.length()==0){
						Toast toast = Toast.makeText(getApplicationContext(),
								"请输入新密码", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return ;
					}
					newPwd2Str=newPwd2.getText().toString();
					if(!newPwdStr.equals(newPwd2Str)||newPwd2Str.length()==0){
						Toast toast = Toast.makeText(getApplicationContext(),
								"新密码与确认新密码输入不一致，请重新输入", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return ;
					}
					
					
					SharedPreferences preferences = getSharedPreferences(CONSTANTS.YEDIANCHINA_USER_INFO,
							Activity.MODE_PRIVATE);
					final Long  loginUID=preferences.getLong("uid", 0);
					
					
					new Thread() {
						public void run() {
							LoginUserDao dao = new LoginUserDao();
							code=dao.updatePwd(loginUID,oldPwdStr, newPwdStr);
							updateHandler.sendEmptyMessage(code);

						}
					}.start();
					
				}
			});
		}
		
		
	}
	
	
	Handler  updateHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;

			if (what == 1) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"密码修改成功", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

				

			}else if (what == 7) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"原始密码输入有误，请重新输入", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

				

			}  
			
			
			
			else {

				Toast toast = Toast.makeText(getApplicationContext(),
						"密码修改失败，请稍后再试", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

		};
	};

}
