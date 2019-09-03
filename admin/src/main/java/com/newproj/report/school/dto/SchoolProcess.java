package com.newproj.report.school.dto;

public class SchoolProcess {

	private Integer id ;
	private Integer schoolId;
	private Integer reportId;
	private Integer totalNum ;
	private Integer finishNum;
	private String submitTime;
	private String auditTime;
	private String auditId;
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
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditId() {
		return auditId;
	}
	public void setAuditId(String auditId) {
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
