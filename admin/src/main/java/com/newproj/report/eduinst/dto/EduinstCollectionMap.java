package com.newproj.report.eduinst.dto;

public class EduinstCollectionMap {

	private Integer instId ;
	private Integer quotaId ;
	private Integer collectionId ;
	private Integer reportId ;
	private String instType;

	public Integer getInstId() {
		return instId;
	}

	public void setInstId(Integer instId) {
		this.instId = instId;
	}

	public Integer getQuotaId() {
		return quotaId;
	}

	public void setQuotaId(Integer quotaId) {
		this.quotaId = quotaId;
	}

	public Integer getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Integer collectionId) {
		this.collectionId = collectionId;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public String getInstType() {
		return instType;
	}

	public void setInstType(String instType) {
		this.instType = instType;
	}
}
