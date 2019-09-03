package com.newproj.report.quota.dto;

public class SubaccountQuotaParam {
	private Integer userId ;
	private Integer quotaId ;
	private Integer createId ;
	private String createTime ;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getQuotaId() {
		return quotaId;
	}
	public void setQuotaId(Integer quotaId) {
		this.quotaId = quotaId;
	}
	public Integer getCreateId() {
		return createId;
	}
	public void setCreateId(Integer createId) {
		this.createId = createId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
