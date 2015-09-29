package com.yedianchina.po;

public class GroupPO {
	String add_time;
	String group_master;
	Long group_id;
	String group_name;
	String group_desc;
	int need_validate;//'0' COMMENT '1：需要验证  0：不需要',
	String avatar;
	String merchantName;
	Long merchantId;
	String last_login;
	private Long member_cnt;
	
	private String distance;
	
	public Long getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Long group_id) {
		this.group_id = group_id;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getGroup_desc() {
		return group_desc;
	}
	public void setGroup_desc(String group_desc) {
		this.group_desc = group_desc;
	}
	public int getNeed_validate() {
		return need_validate;
	}
	public void setNeed_validate(int need_validate) {
		this.need_validate = need_validate;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getLast_login() {
		return last_login;
	}
	public void setLast_login(String last_login) {
		this.last_login = last_login;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public Long getMember_cnt() {
		return member_cnt;
	}
	public void setMember_cnt(Long member_cnt) {
		this.member_cnt = member_cnt;
	}
	public String getGroup_master() {
		return group_master;
	}
	public void setGroup_master(String group_master) {
		this.group_master = group_master;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

}
