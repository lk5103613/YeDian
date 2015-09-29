package com.yedianchina.po;



public class City {
	public City(String name, String pinyi) {
		super();
		this.name = name;
		this.pinyi = pinyi;
	}

	public City() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String name;
	public String pinyi;
	public Long  cityId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinyi() {
		return pinyi;
	}
	public void setPinyi(String pinyi) {
		this.pinyi = pinyi;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}


}
