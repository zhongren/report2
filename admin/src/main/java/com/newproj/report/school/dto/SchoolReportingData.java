package com.newproj.report.school.dto;

public class SchoolReportingData {
	private String  quota1Name; //一级指标
	private String  quota2Name; //二级指标
	private String  title; //采集项
	private String  content = "";  //采集项值
	private String valueType ;
	private String valueOpt ;
	private Integer collectionId ;
	private String 	validation ;
	public String getQuota1Name() {
		return quota1Name;
	}
	public void setQuota1Name(String quota1Name) {
		this.quota1Name = quota1Name;
	}
	public String getQuota2Name() {
		return quota2Name;
	}
	public void setQuota2Name(String quota2Name) {
		this.quota2Name = quota2Name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public String getValueOpt() {
		return valueOpt;
	}
	public void setValueOpt(String valueOpt) {
		this.valueOpt = valueOpt;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getValidation() {
		return validation;
	}
	public void setValidation(String validation) {
		this.validation = validation;
	}
	public Integer getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(Integer collectionId) {
		this.collectionId = collectionId;
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	
}
