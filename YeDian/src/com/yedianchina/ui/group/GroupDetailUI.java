package com.yedianchina.ui.group;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yedianchina.dao.GroupDao;
import com.yedianchina.po.GroupPO;
import com.yedianchina.ui.CommonActivity;
import com.yedianchina.ui.R;

public class GroupDetailUI extends CommonActivity {
	Long group_id;
	TextView groupNameTv;
	TextView descTv;
	ImageView avatarTv;
	TextView groupMasterTv;
	TextView createTimeTv;
	TextView merchantNameTv;
	GroupPO po;
	
	 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.group_detail);
		group_id = this.getIntent().getExtras().getLong("pk");
		groupNameTv = (TextView) this.findViewById(R.id.groupNameTv);

		descTv = (TextView) this.findViewById(R.id.descTv);

		avatarTv = (ImageView) this.findViewById(R.id.avatarTv);
		groupMasterTv = (TextView) this.findViewById(R.id.groupMasterTv);
		createTimeTv = (TextView) this.findViewById(R.id.createTimeTv);
		merchantNameTv = (TextView) this.findViewById(R.id.merchantNameTv);

		new Thread() {
			public void run() {
				po=GroupDao.loadGroup(group_id);
				loadHandler.sendEmptyMessage(1);

			}
		}.start();
		
		TextView NavigateTitle=(TextView) this.findViewById(R.id.NavigateTitle);
		if(NavigateTitle!=null){
			NavigateTitle.setText("群组详情");
		}
		 
		TextView qiandaoBtn=(TextView) this.findViewById(R.id.qiandaoBtn);
		if(qiandaoBtn!=null){
			qiandaoBtn.setVisibility(View.INVISIBLE);
		}
		//返回键
		ImageView backBtn = (ImageView) this.findViewById(R.id.backBtn);
		if (backBtn != null) {
			backBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					GroupDetailUI.this.finish();

				}
			});
		}
	 
	}

	private void setDataFn() {
		String groupName=po.getGroup_name();
		groupNameTv.setText(groupName);
		
		String group_desc=po.getGroup_desc();
		descTv.setText(group_desc);
		
		String group_master=po.getGroup_master();
		groupMasterTv.setText(group_master);
		
		String add_time=po.getAdd_time();
		createTimeTv.setText(add_time);
		
//		String merchantName=po.getMerchantName();
//		merchantNameTv.setText(merchantName);
		
		
		String avatar=po.getAvatar();
		avatarTv.setTag(avatar);
		
		
		 ImageLoader.getInstance().displayImage(avatar,avatarTv);
		

	}

	private Handler loadHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				setDataFn();

			}

		}
	};

}
