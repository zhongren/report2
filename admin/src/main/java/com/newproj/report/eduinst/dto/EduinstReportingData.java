package com.newproj.report.eduinst.dto;

public class EduinstReportingData {
	 private String  quota1Name; //一级指标
	 private String  quota2Name; //二级指标
	 private String  title; //采集项
	 private String  content;  //采集项值
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
	public void setContent(String content) {
		this.content = content;
	}
}
