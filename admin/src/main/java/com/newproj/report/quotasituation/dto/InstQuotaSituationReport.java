package com.newproj.report.quotasituation.dto;

public class InstQuotaSituationReport {

	private Integer situationId ;
	private String situationName ; //机构名称
	private String standardNote ;  //目标值描述
	private String calculatedValue ;//指标值
	private String eligibleValue ; //达成情况
	
	private String schoolName ; //学校名称
	private String content ;	//采集项值
	
	public String getSituationName() {
		return situationName;
	}
	public void setSituationName(String situationName) {
		this.situationName = situationName;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getContent() {
		return content;
	}
	public Integer getSituationId() {
		return situationId;
	}
	public void setSituationId(Integer situationId) {
		this.situationId = situationId;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStandardNote() {
		return standardNote;
	}
	public void setStandardNote(String standardNote) {
		this.standardNote = standardNote;
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
