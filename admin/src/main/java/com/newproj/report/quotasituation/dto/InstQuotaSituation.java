package com.newproj.report.quotasituation.dto ;
public class InstQuotaSituation {
	
	private Integer id ; 	//ID 
	private String instType; //机构类型
	private Integer instId ; 	//机构ID
	private String schoolType; //学校类型
	private Integer reportingId ; 	//填报年份 
	private Integer situationId ; 	//监测点ID 
	private String situationName ; //监测点名称
	private String calculatedValue ; 	//计算值 
	private String eligibleValue ; 	//是否合格结果 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getInstType() {
		return instType;
	}
	public void setInstType(String instType) {
		this.instType = instType;
	}
	public Integer getInstId() {
		return instId;
	}
	public void setInstId(Integer instId) {
		this.instId = instId;
	}
	public String getSchoolType() {
		return schoolType;
	}
	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}
	public Integer getReportingId() {
		return reportingId;
	}
	public void setReportingId(Integer reportingId) {
		this.reportingId = reportingId;
	}
	public Integer getSituationId() {
		return situationId;
	}
	public void setSituationId(Integer situationId) {
		this.situationId = situationId;
	}
	public String getSituationName() {
		return situationName;
	}
	public void setSituationName(String situationName) {
		this.situationName = situationName;
	}
	public String getCalculatedValue() {
		return calculatedValue;
	}
	public void setCalculatedValue(String calculatedValue) {
		this.calculatedValue = calculatedValue;
	}
	public String getEligibleValue() {
		return eligibleValue;
	}
	public void setEligibleValue(String eligibleValue) {
		this.eligibleValue = eligibleValue;
	}


}