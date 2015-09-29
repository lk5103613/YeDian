package com.yedianchina.po;

import java.io.Serializable;
import java.util.Date;
public class Driver implements Serializable{
	private static final long serialVersionUID = 1L;
	private String nativePlace;//籍贯
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getDrivingLicence() {
		return drivingLicence;
	}
	public void setDrivingLicence(String drivingLicence) {
		this.drivingLicence = drivingLicence;
	}
	public int getDriveCnt() {
		return driveCnt;
	}
	public void setDriveCnt(int driveCnt) {
		this.driveCnt = driveCnt;
	}
	private String drivingLicence;//驾照
	private int driveCnt;//代驾次数'
	String juli;
	private long cityId;
	private long driverId;
	private String truename;
	private String pwd;
	private String mp;
	private String addr;
	private String email;
	private int status;
	private int cautionMoney;
	private Date add_time;
	private String  cityName;
	private String  longi;
	private String  lanti;
	private int star;
	private int workYear;
	private String avatar;
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public long getDriverId() {
		return driverId;
	}
	public void setDriverId(long driverId) {
		this.driverId = driverId;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getMp() {
		return mp;
	}
	public void setMp(String mp) {
		this.mp = mp;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCautionMoney() {
		return cautionMoney;
	}
	public void setCautionMoney(int cautionMoney) {
		this.cautionMoney = cautionMoney;
	}
	public Date getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
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
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public int getWorkYear() {
		return workYear;
	}
	public void setWorkYear(int workYear) {
		this.workYear = workYear;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getJuli() {
		return juli;
	}
	public void setJuli(String juli) {
		this.juli = juli;
	}

}
