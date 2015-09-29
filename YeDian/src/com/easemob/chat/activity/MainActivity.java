
package com.easemob.chat.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.ConnectionListener;
import com.easemob.chat.Constant;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.db.InviteMessgeDao;
import com.easemob.chat.db.UserDao;
import com.easemob.chat.domain.InviteMessage;
import com.easemob.chat.domain.InviteMessage.InviteMesageStatus;
import com.easemob.chat.domain.User;
import com.easemob.chat.utils.CommonUtils;
import com.easemob.util.HanziToPinyin;
import com.yedianchina.tools.CONSTANTS;
import com.yedianchina.ui.LoginUI;
import com.yedianchina.ui.MoreUI;
import com.yedianchina.ui.R;
import com.yedianchina.ui.YeDianChinaApplication;
import com.yedianchina.ui.i.I;
import com.yedianchina.ui.i.IMingpianUI;
import com.yedianchina.ui.index.IndexUI;
import com.yedianchina.ui.nearby.NearbyModule;

public class MainActivity extends BaseFrameActivity {

	protected static final String TAG = "MainActivity";
	// 未读消息textview
	private TextView unreadLabel;
	// 未读通讯录textview
	private TextView unreadAddressLable;

	private Button[] mTabs;
	private ContactlistFragment contactListFragment;
	private ChatHistoryFragment chatHistoryFragment;
	private SettingsFragment settingFragment;
	private Fragment[] fragments;
	private int index;
	private RelativeLayout[] tab_containers;
	// 当前fragment的index
	private int currentTabIndex;
	private NewMessageBroadcastReceiver msgReceiver;
	// 账号在别处登录
	private boolean isConflict = false;
	
	
	private void parentControl() {
		// 获取密度
		 

	 
		imageViewIndex = (ImageView) findViewById(R.id.menu_home_img);
		imageViewIndex.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 
				startActivity(new Intent(getApplicationContext(),
						IndexUI.class));
				MainActivity.this.finish();
				
			}
		});
	 

		imageViewType = (ImageView) findViewById(R.id.menu_brand_img);
		imageViewType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(),
						NearbyModule.class));
				MainActivity.this.finish();
			}
		});

		imageViewShooping = (ImageView) findViewById(R.id.menu_shopping_cart_img);
		imageViewShooping.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			
				
			
				Log.e("ImageViewMyLetao", "聊天---");
				SharedPreferences preferences = getSharedPreferences(
						CONSTANTS.YEDIANCHINA_USER_INFO, Activity.MODE_PRIVATE);
				Long uid = preferences.getLong(CONSTANTS.UID, 0);
				if(uid==null||uid==0){
					Toast.makeText(getApplicationContext(),
							"您尚未登录,请先登录", 1).show();
					startActivity(new Intent(getApplicationContext(), LoginUI.class));
					return;
				}
				
				

				String myNickname = preferences.getString(CONSTANTS.NICKNAME, "");
				if (myNickname == null || myNickname.length() == 0
						|| "".equals(myNickname)) {
					Toast.makeText(getApplicationContext(),
							"您的资料不完善，请去“我的中心－》右上角我的名片处完善个人资料 ", 1).show();
					
					startActivity(new Intent(getApplicationContext(), IMingpianUI.class));

				} else {
//					intent=new Intent();
//					
//					intent.setClass(getApplicationContext(), MainActivity.class);
//					MainActivity.this.finish();
				}
				
				

			
				
			}
		});
		

		imageViewMyLetao = (ImageView) findViewById(R.id.menu_shopping_cart_img);
		imageViewMyLetao.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 
				
				startActivity(new Intent(getApplicationContext(),
						I.class));
				MainActivity.this.finish();
				
			}
		});

		imageViewMore = (ImageView) findViewById(R.id.menu_more_img);
		imageViewMore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(),
						MoreUI.class));
				MainActivity.this.finish();
			}
		});

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		parentControl();
		//

		// ////
		initView();
		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDao(this);
		chatHistoryFragment = new ChatHistoryFragment();
		contactListFragment = new ContactlistFragment();
		settingFragment = new SettingsFragment();
		fragments = new Fragment[] { chatHistoryFragment, contactListFragment,
				settingFragment };
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, chatHistoryFragment)
				.add(R.id.fragment_container, contactListFragment)
				.hide(contactListFragment).show(chatHistoryFragment).commit();

		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		// 注册一个ack回执消息的BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(
				new MyContactListener());
		// 注册一个监听连接状态的listener
		EMChatManager.getInstance().addConnectionListener(
				new MyConnectionListener());
		// 注册群聊相关的listener
		EMGroupManager.getInstance().addGroupChangeListener(
				new MyGroupChangeListener());
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		//unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
		mTabs = new Button[3];
	

	}

	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销广播接收者
		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(ackMessageReceiver);
		} catch (Exception e) {
		}

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}

	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 刷新申请与通知消息数
	 */
	public void updateUnreadAddressLable() {
//		runOnUiThread(new Runnable() {
//			public void run() {
//				int count = getUnreadAddressCountTotal();
//				if (count > 0) {
//					unreadAddressLable.setText(String.valueOf(count));
//					unreadAddressLable.setVisibility(View.VISIBLE);
//				} else {
//					unreadAddressLable.setVisibility(View.INVISIBLE);
//				}
//			}
//		});

	}

	/**
	 * 获取未读申请与通知消息
	 * 
	 * @return
	 */
//	public int getUnreadAddressCountTotal() {
//		int unreadAddressCountTotal = 0;
//		if (YeDianChinaApplication.getInstance().getContactList()
//				.get(Constant.NEW_FRIENDS_USERNAME) != null)
//			unreadAddressCountTotal = YeDianChinaApplication.getInstance()
//					.getContactList().get(Constant.NEW_FRIENDS_USERNAME)
//					.getUnreadMsgCount();
//		return unreadAddressCountTotal;
//	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			// EMMessage message =
			// EMChatManager.getInstance().getMessage(msgId);

			// 刷新bottom bar消息未读数
			updateUnreadLabel();
			if (currentTabIndex == 0) {
				// 当前页面如果为聊天历史页面，刷新此页面
				if (chatHistoryFragment != null) {
					chatHistoryFragment.refresh();
				}
			}
			// 注销广播，否则在ChatActivity中会收到这个广播
			abortBroadcast();
		}
	}

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					msg.isAcked = true;
				}
			}
			abortBroadcast();
		}
	};

	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;

	/***
	 * 联系人变化listener
	 * 
	 */
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = YeDianChinaApplication.getInstance()
					.getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = new User();
				user.setUsername(username);
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
							.get(headerName.substring(0, 1)).get(0).target
							.substring(0, 1).toUpperCase());
					char header = user.getHeader().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						user.setHeader("#");
					}
				}
				// 暂时有个bug，添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// 刷新ui
			if (currentTabIndex == 1)
				contactListFragment.refresh();

		}

		@Override
		public void onContactDeleted(List<String> usernameList) {
			// 被删除
			Map<String, User> localUsers = YeDianChinaApplication.getInstance()
					.getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			// 刷新ui
			if (currentTabIndex == 1)
				contactListFragment.refresh();
			updateUnreadLabel();

		}

		@Override
		public void onContactInvited(String username, String reason) {
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null
						&& inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d(TAG, username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

			try {
				EMChatManager.getInstance().acceptInvitation(username);// 如果聊天对象不是我的联系人
																		// 则需要发送添加好友请求过去
																		// 07-18
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(TAG, username + "同意了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,demo未实现

		}

	}

	/**
	 * 保存提示新消息
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

		// 刷新bottom bar消息未读数
		updateUnreadAddressLable();
		// 刷新好友页面ui
		if (currentTabIndex == 1)
			contactListFragment.refresh();
	}

	/**
	 * 保存邀请等msg
	 * 
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = YeDianChinaApplication.getInstance().getContactList()
				.get(Constant.NEW_FRIENDS_USERNAME);
		user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	/**
	 * 连接监听listener
	 * 
	 */
	private class MyConnectionListener implements ConnectionListener {

		@Override
		public void onConnected() {
			chatHistoryFragment.errorItem.setVisibility(View.GONE);
		}

		@Override
		public void onDisConnected(String errorString) {
			if (errorString != null && errorString.contains("conflict")) {
				// 显示帐号在其他设备登陆dialog
				showConflictDialog();
			} else {
				chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
				chatHistoryFragment.errorText.setText("连接不到聊天服务器");
			}
		}

		@Override
		public void onReConnected() {
			chatHistoryFragment.errorItem.setVisibility(View.GONE);
		}

		@Override
		public void onReConnecting() {
		}

		@Override
		public void onConnecting(String progress) {
		}

	}

	/**
	 * MyGroupChangeListener
	 */
	private class MyGroupChangeListener implements GroupChangeListener {

		@Override
		public void onInvitationReceived(String groupId, String groupName,
				String inviter, String reason) {
			// 被邀请
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(inviter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(inviter + "邀请你加入了群聊"));
			// 保存邀请消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					if (currentTabIndex == 0)
						chatHistoryFragment.refresh();
					if (CommonUtils.getTopActivity(MainActivity.this).equals(
							GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter,
				String reason) {

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee,
				String reason) {

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {
			// 提示用户被T了，demo省略此步骤
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					try {
						updateUnreadLabel();
						if (currentTabIndex == 0)
							chatHistoryFragment.refresh();
						if (CommonUtils.getTopActivity(MainActivity.this)
								.equals(GroupsActivity.class.getName())) {
							GroupsActivity.instance.onResume();
						}
					} catch (Exception e) {
						Log.e("###", "refresh exception " + e.getMessage());
					}

				}
			});
		}

		@Override
		public void onGroupDestroy(String groupId, String groupName) {
			// 群被解散
			// 提示用户群被解散,demo省略
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					if (currentTabIndex == 0)
						chatHistoryFragment.refresh();
					if (CommonUtils.getTopActivity(MainActivity.this).equals(
							GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		@Override
		public void onApplicationReceived(String groupId, String groupName,
				String applyer, String reason) {
			// 用户申请加入群聊
			InviteMessage msg = new InviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
			msg.setStatus(InviteMesageStatus.BEAPPLYED);
			notifyNewIviteMessage(msg);
		}

		@Override
		public void onApplicationAccept(String groupId, String groupName,
				String accepter) {
			// 加群申请被同意
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(accepter + "同意了你的群聊申请"));
			// 保存同意消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					if (currentTabIndex == 0)
						chatHistoryFragment.refresh();
					if (CommonUtils.getTopActivity(MainActivity.this).equals(
							GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});
		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName,
				String decliner, String reason) {
			// 加群申请被拒绝，demo未实现
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isConflict) {
			updateUnreadLabel();
			updateUnreadAddressLable();
			EMChatManager.getInstance().activityResumed();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private android.app.AlertDialog.Builder conflictBuilder;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {

		YeDianChinaApplication.getInstance().logout();

		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(
							MainActivity.this);
				conflictBuilder.setTitle("下线通知");
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								conflictBuilder = null;
								finish();
								startActivity(new Intent(MainActivity.this,
										LoginActivity.class));
							}
						});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				Log.e("###",
						"---------color conflictBuilder error" + e.getMessage());
			}

		}

	}

}
