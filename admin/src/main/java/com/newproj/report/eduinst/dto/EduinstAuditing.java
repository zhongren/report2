package com.newproj.report.eduinst.dto;

public class EduinstAuditing {
	private Integer id ;
	private Integer userId ;
	private Integer reportId ;
	private Integer eduinstId ;
	private String eduinstName ;
	private String code ;
	private String type ;
	private String typeName ;
	private String auditTime ;
	private String auditNote ;
	private String collectionsId ;
	private Integer status ;
	private String statusName ;
	
	private Integer processStatus ;
	private String processStatusName ;
	private String submitTime ; //提交审核日期
	private Integer cityId ;
	private String cityName ;
	private Integer countyId ;
	private String countyName ;
	private Integer totalNum ;
	private Integer finishNum ;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getReportId() {
		return reportId;
	}
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	public Integer getEduinstId() {
		return eduinstId;
	}
	public void setEduinstId(Integer eduinstId) {
		this.eduinstId = eduinstId;
	}
	public String getEduinstName() {
		return eduinstName;
	}
	public void setEduinstName(String eduinstName) {
		this.eduinstName = eduinstName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditNote() {
		return auditNote;
	}
	public void setAuditNote(String auditNote) {
		this.auditNote = auditNote;
	}
	public String getCollectionsId() {
		return collectionsId;
	}
	public void setCollectionsId(String collectionsId) {
		this.collectionsId = collectionsId;
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
	public Integer getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}
	public String getProcessStatusName() {
		return processStatusName;
	}
	public void setProcessStatusName(String processStatusName) {
		this.processStatusName = processStatusName;
	}
	public String getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	public Integer getCityId() {
		return cityId;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getFinishNum() {
		return finishNum;
	}
	public void setFinishNum(Integer finishNum) {
		this.finishNum = finishNum;
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
