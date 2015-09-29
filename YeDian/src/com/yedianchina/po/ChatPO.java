package com.yedianchina.po;

public class ChatPO {
	private String avatar;
	private String nickname;
	private String addTime;
	private String msg;
	private Long targetUID;//聊天的目标对象uid
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Long getTargetUID() {
		return targetUID;
	}
	public void setTargetUID(Long targetUID) {
		this.targetUID = targetUID;
	}

}
