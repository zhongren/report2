package com.newproj.report.school.dto;

public class SchoolProcessParam {

	private Integer id ;
	private Integer schoolId;
	private Integer reportId;
	private Integer totalNum ;
	private Integer finishNum;
	private Integer submitId ;
	private String submitTime;
	private String auditTime;
	private Integer auditId;
	private String auditNote;
	private Integer status ;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public Integer getReportId() {
		return reportId;
	}
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	public String getSubmitTime() {
		return submitTime;
	}
	public Integer getSubmitId() {
		return submitId;
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
	public void setSubmitId(Integer submitId) {
		this.submitId = submitId;
	}
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public Integer getAuditId() {
		return auditId;
	}
	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}
	public String getAuditNote() {
		return auditNote;
	}
	public void setAuditNote(String auditNote) {
		this.auditNote = auditNote;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
