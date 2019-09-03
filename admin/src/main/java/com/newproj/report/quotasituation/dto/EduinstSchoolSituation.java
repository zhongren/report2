package com.newproj.report.quotasituation.dto;

public class EduinstSchoolSituation {
	private Integer countyId ;
	private String countyName ;
	private Integer cityId ;
	private String cityName ;
	private Integer schoolId ;
	private String schoolName ;
	private Integer schoolType ;
	private String schoolTypeName ;
	private Integer total ;
	private Integer pass ;
	private Double rate ;
	private Integer reportingId;
	
	public Integer getCountyId() {
		return countyId;
	}
	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public Integer getSchoolType() {
		return schoolType;
	}
	public void setSchoolType(Integer schoolType) {
		this.schoolType = schoolType;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getPass() {
		return pass;
	}
	public void setPass(Integer pass) {
		this.pass = pass;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Integer getReportingId() {
		return reportingId;
	}
	public void setReportingId(Integer reportingId) {
		this.reportingId = reportingId;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getSchoolTypeName() {
		return schoolTypeName;
	}
	public void setSchoolTypeName(String schoolTypeName) {
		this.schoolTypeName = schoolTypeName;
	}
}
