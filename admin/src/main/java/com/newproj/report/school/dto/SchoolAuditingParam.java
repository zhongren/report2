package com.newproj.report.school.dto;

public class SchoolAuditingParam {

	private Integer id ;
	private Integer userId ;
	private Integer reportId ;
	private Integer schoolId ;
	private String auditTime ;
	private String auditNote ;
	private String collectionsId ;
	private Integer status ;
	
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

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
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
}
