package com.newproj.report.school.dto;

public class SchoolDataReporting {

	private Integer status ;
	private String statusName ;
	private Integer amount ;
	private Float rate ;
	
	private Integer cityId ;
	private String cityName ;
	private Integer countyId ;
	private String countyName ;
	
	public SchoolDataReporting(){
		
	}
	
	public SchoolDataReporting(Integer status, String statusName, Integer amount, Float rate) {
		super();
		this.status = status;
		this.statusName = statusName;
		this.amount = amount;
		this.rate = rate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Float getRate() {
		return rate;
	}
	public void setRate(Float rate) {
		this.rate = rate;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getCountyId() {
		return countyId;
	}

	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
}
