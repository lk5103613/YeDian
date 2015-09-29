package com.yedianchina.ui.nearby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.yedianchina.ui.R;
import com.yedianchina.ui.YeDianChinaApplication;

public class SendMsgUI extends Activity {

	private PopupWindow menuPop,jiaPop;
	TextView send_btn;
	EditText msgEt;
	//private SharePreferenceUtil mSpUtil;
	//private User mFromUser;
	private YeDianChinaApplication mApplication;
	private Gson mGson;
	//private MessageDB mMsgDB;
	//private RecentDB mRecentDB;
	private List<String> keys;
	private static int MsgPagerNum;
	//private MessageAdapter adapter;
	
	private void initData() {
//		mFromUser = (User) getIntent().getSerializableExtra("user");
//		if (mFromUser == null) {// 如果为空，直接关闭
//			finish();
//		}
//		mApplication = YeDianChinaApplication.getInstance();
//		mSpUtil = mApplication.getSpUtil();
//		mGson = mApplication.getGson();
//		mMsgDB = mApplication.getMessageDB();
//		mRecentDB = mApplication.getRecentDB();
//		Set<String> keySet = YeDianChinaApplication.getInstance().getFaceMap()
//				.keySet();
//		keys = new ArrayList<String>();
//		keys.addAll(keySet);
//		MsgPagerNum = 0;
//		adapter = new MessageAdapter(this, initMsgData());
	}
//	private List<MessageItem> initMsgData() {
//		List<MessageItem> list = mMsgDB.getMsg(mFromUser.getUserId(),
//				MsgPagerNum);
//		List<MessageItem> msgList = new ArrayList<MessageItem>();// 消息对象数组
//		if (list.size() > 0) {
//			for (MessageItem entity : list) {
//				if (entity.getName().equals("")) {
//					entity.setName(mFromUser.getNick());
//				}
//				if (entity.getHeadImg() < 0) {
//					entity.setHeadImg(mFromUser.getHeadIcon());
//				}
//				msgList.add(entity);
//			}
//		}
//		return msgList;
//
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.send_msg);
		
		initData();
		
		send_btn=(TextView)this.findViewById(R.id.send_btn);
		msgEt = (EditText) findViewById(R.id.msg_et);
		
		ImageView backBtn=(ImageView)this.findViewById(R.id.backBtn);
		if(backBtn!=null){
			backBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SendMsgUI.this.finish();
					
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
		
		if(send_btn!=null){
			send_btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					String msg = msgEt.getText().toString();
//					MessageItem item = new MessageItem(MessageItem.MESSAGE_TYPE_TEXT,
//							mSpUtil.getNick(), System.currentTimeMillis(), msg,
//							mSpUtil.getHeadIcon(), false, 0);
//					adapter.upDateMsg(item);
//					// if (adapter.getCount() - 10 > 10) {
//					// L.i("begin to remove...");
//					// adapter.removeHeadMsg();
//					// MsgPagerNum--;
//					// }
//					//mMsgListView.setSelection(adapter.getCount() - 1);
//					mMsgDB.saveMsg(mFromUser.getUserId(), item);
//					msgEt.setText("");
//					com.way.bean.Message msgItem = new com.way.bean.Message(
//							System.currentTimeMillis(), msg, "");
//					new SendMsgAsyncTask(mGson.toJson(msgItem), mFromUser.getUserId())
//							.send();
//					RecentItem recentItem = new RecentItem(mFromUser.getUserId(),
//							mFromUser.getHeadIcon(), mFromUser.getNick(), msg, 0,
//							System.currentTimeMillis());
//					mRecentDB.saveRecent(recentItem);
					
				}
			});
		}
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
				Toast.makeText(SendMsgUI.this, "点击图片.", Toast.LENGTH_LONG).show();
				jiaPop.dismiss();
			}
		});
		view.findViewById(R.id.biaoqing).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(SendMsgUI.this, "点击表情.", Toast.LENGTH_LONG).show();
				jiaPop.dismiss();
			}
		});
		view.findViewById(R.id.weizhi).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(SendMsgUI.this, "点击位置.", Toast.LENGTH_LONG).show();
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
				Toast.makeText(SendMsgUI.this, "点击:" + value, Toast.LENGTH_LONG).show();
				
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