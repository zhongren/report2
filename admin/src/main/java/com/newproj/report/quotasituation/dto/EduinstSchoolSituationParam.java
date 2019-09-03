package com.newproj.report.quotasituation.dto;

public class EduinstSchoolSituationParam {
	private Integer countyId ;
	private Integer cityId ;
	private Integer schoolId ;
	private Integer schoolType ;
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
}
