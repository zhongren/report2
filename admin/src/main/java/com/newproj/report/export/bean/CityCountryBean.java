package com.newproj.report.export.bean;

import java.util.List;

public class CityCountryBean {
	
	private int cityId;
		
	private String cityName;
	
	private List<CityClassNumBean> coutrys;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public List<CityClassNumBean> getCoutrys() {
		return coutrys;
	}

	public void setCoutrys(List<CityClassNumBean> coutrys) {
		this.coutrys = coutrys;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	
	
	
}
