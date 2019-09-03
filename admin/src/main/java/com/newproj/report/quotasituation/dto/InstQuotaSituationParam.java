package com.newproj.report.quotasituation.dto ;
public class InstQuotaSituationParam {
	
	private Integer id ; 	//ID 
	private String instType; //机构类型
	private Integer instId ; 	//机构ID
	private String schoolType; //学校类型
	private Integer reportingId ; 	//填报年份 
	private Integer situationId ; 	//监测点ID 
	private String situationName ; //监测点名称
	private String calculatedValue ; 	//计算值 
	private String calculatedExtra ;
	private String eligibleValue ; 	//是否合格结果 
	private String eligibleExtra ;
	private Integer state ; //状态

	public String getSituationName() {
		return situationName;
	}
	public void setSituationName(String situationName) {
		this.situationName = situationName;
	}
	public void setId ( Integer id ){ 
		this.id = id ; 
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
	public Integer getId ( ) { 
		return this.id ; 
	}
	public void setReportingId ( Integer reportingId ){ 
		this.reportingId = reportingId ; 
 	}
	public Integer getReportingId ( ) { 
		return this.reportingId ; 
	}
	public void setSituationId ( Integer situationId ){ 
		this.situationId = situationId ; 
 	}
	public Integer getSituationId ( ) { 
		return this.situationId ; 
	}
	public void setCalculatedValue ( String calculatedValue ){ 
		this.calculatedValue = calculatedValue ; 
 	}
	public String getCalculatedValue ( ) { 
		return this.calculatedValue ; 
	}
	public void setEligibleValue ( String eligibleValue ){ 
		this.eligibleValue = eligibleValue ; 
 	}
	public String getEligibleValue ( ) { 
		return this.eligibleValue ; 
	}
	public String getCalculatedExtra() {
		return calculatedExtra;
	}
	public void setCalculatedExtra(String calculatedExtra) {
		this.calculatedExtra = calculatedExtra;
	}
	public String getEligibleExtra() {
		return eligibleExtra;
	}
	public void setEligibleExtra(String eligibleExtra) {
		this.eligibleExtra = eligibleExtra;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}

}