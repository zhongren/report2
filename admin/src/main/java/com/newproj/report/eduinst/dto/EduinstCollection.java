package com.newproj.report.eduinst.dto;

public class EduinstCollection {
	private Integer id ;
	private Integer instId ;
	private Integer quotaId ;
	private Integer collectionId ;
	private Integer reportId ;
	private String content ;
	private String dataType ;
	private String createTime ;
	private Integer createId ;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuotaId() {
		return quotaId;
	}
	public void setQuotaId(Integer quotaId) {
		this.quotaId = quotaId;
	}
	public Integer getInstId() {
		return instId;
	}
	public void setInstId(Integer instId) {
		this.instId = instId;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Integer getReportId() {
		return reportId;
	}
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getCreateId() {
		return createId;
	}
	public void setCreateId(Integer createId) {
		this.createId = createId;
	}
	public Integer getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(Integer collectionId) {
		this.collectionId = collectionId;
	}
	
	
}
