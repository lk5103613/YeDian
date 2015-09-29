package com.yedianchina.po;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserPO implements Serializable {
	
	String home_town;

	private int chat;
	private String  pwd;
	private int userType;//'1:个人用户    2：商家',
	private String  longi;
	private String  lanti;
	private String height;//身高
	private String weight;//体重
	String astro;//星座
	String nickname;//昵称
    private String  distance;
    String  qm;//个人签名
    int gender;//性别  1:男  2女
    int age;
    private String  birthday;//生日
	String email;
	String last_login;

	String city_name;
	String avatar;

	private Double balance;
	private String mp;
	private Long uid;
	private int code;
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLast_login() {
		return last_login;
	}
	public void setLast_login(String last_login) {
		this.last_login = last_login;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getMp() {
		return mp;
	}
	public void setMp(String mp) {
		this.mp = mp;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getQm() {
		return qm;
	}
	public void setQm(String qm) {
		this.qm = qm;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAstro() {
		return astro;
	}
	public void setAstro(String astro) {
		this.astro = astro;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getLongi() {
		return longi;
	}
	public void setLongi(String longi) {
		this.longi = longi;
	}
	public String getLanti() {
		return lanti;
	}
	public void setLanti(String lanti) {
		this.lanti = lanti;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getChat() {
		return chat;
	}
	public void setChat(int chat) {
		this.chat = chat;
	}
	public String getHome_town() {
		return home_town;
	}
	public void setHome_town(String home_town) {
		this.home_town = home_town;
	}


}
