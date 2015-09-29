package com.yedianchina.po;

public class Order {;
	private long id;
	private String startAddr;//驾车起始地址
	private String endAddr;
	public String getEndAddr() {
		return endAddr;
	}
	public void setEndAddr(String endAddr) {
		this.endAddr = endAddr;
	}
	private String subscribe;
	private String mp;
	private int driverNum;
	public String getStartAddr() {
		return startAddr;
	}
	public void setStartAddr(String startAddr) {
		this.startAddr = startAddr;
	}
	public String getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}
	public String getMp() {
		return mp;
	}
	public void setMp(String mp) {
		this.mp = mp;
	}
	public int getDriverNum() {
		return driverNum;
	}
	public void setDriverNum(int driverNum) {
		this.driverNum = driverNum;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	

}
