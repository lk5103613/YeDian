package com.yedianchina.ui.i;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.yedianchina.ui.R;

public class ISendMsgUI extends Activity {

	private PopupWindow menuPop,jiaPop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.send_msg);
		
		ImageView backBtn=(ImageView)this.findViewById(R.id.backBtn);
		if(backBtn!=null){
			backBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ISendMsgUI.this.finish();
					
				}
			});
		}
		//
	
		

		initMenuPop();
		initJiaPop();

		findViewById(R.id.menu).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuPop.showAsDropDown(v, -126, 18);
			}
		});
		findViewById(R.id.jia).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				jiaPop.showAsDropDown(v, 0, 16);
			}
		});
	}
	
	private void initJiaPop() {
		// 引入窗口配置文件
		View view = LayoutInflater.from(this).inflate(R.layout.send_msg_popup_2, null);
		// 创建PopupWindow对象
		jiaPop = new PopupWindow(view, LayoutParams.MATCH_PARENT, 160, false);
		jiaPop.setBackgroundDrawable(new BitmapDrawable());
		jiaPop.setOutsideTouchable(true);
		jiaPop.setFocusable(true);
		
		view.findViewById(R.id.tupian).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ISendMsgUI.this, "点击图片.", Toast.LENGTH_LONG).show();
				jiaPop.dismiss();
			}
		});
		view.findViewById(R.id.biaoqing).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ISendMsgUI.this, "点击表情.", Toast.LENGTH_LONG).show();
				jiaPop.dismiss();
			}
		});
		view.findViewById(R.id.weizhi).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(ISendMsgUI.this, "点击位置.", Toast.LENGTH_LONG).show();
				jiaPop.dismiss();
			}
		});
	}

	private void initMenuPop() {
		// 引入窗口配置文件
		View view = LayoutInflater.from(this).inflate(R.layout.send_msg_popup, null);
		// 创建PopupWindow对象
		menuPop = new PopupWindow(view, 320, LayoutParams.WRAP_CONTENT, false);
		menuPop.setBackgroundDrawable(new BitmapDrawable());
		menuPop.setOutsideTouchable(true);
		menuPop.setFocusable(true);

		ListView listView = (ListView) view.findViewById(R.id.listView);
		// 配置适配器
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),// 数据源
				R.layout.send_msg_list_item,// 显示布局
				new String[] { "textView" }, // 数据源的属性字段
				new int[] { R.id.textView }); // 布局里的控件id
		// 添加并且显示
		listView.setAdapter(adapter);
		// 添加点击
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String value = "";
				switch(arg2){
				case 0:
					value = "查看资料";
					break;
				case 1:
					value = "清空聊天记录";
					break;
				case 2:
					value = "加黑名单";
					break;
				case 3:
					value = "举报";
					break;
				}
				Toast.makeText(ISendMsgUI.this, "点击:" + value, Toast.LENGTH_LONG).show();
				
				menuPop.dismiss();
			}
		});
	}

	private List<Map<String, String>> getData() {
		List<Map<String, String>> mylist = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("textView", "查看资料");
		mylist.add(map);
		map = new HashMap<String, String>();
		map.put("textView", "清空聊天记录");
		mylist.add(map);
		map = new HashMap<String, String>();
		map.put("textView", "加黑名单");
		mylist.add(map);
		map = new HashMap<String, String>();
		map.put("textView", "举报");
		mylist.add(map);
		mylist.add(map);
		return mylist;
	}

}
