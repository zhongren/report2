package com.newproj.report.export.bean;

public class SchoolCollectionData {
	 private String  firstName; //一级指标
	 private String  secondName; //二级指标
	 private String  collectionName; //采集项
	 private String  value;  //采集项值
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public SchoolCollectionData(String firstName, String secondName, String collectionName, String value) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.collectionName = collectionName;
		this.value = value;
	}
	public SchoolCollectionData() {
	}
	
	 
}
