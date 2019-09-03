package com.newproj.report.school.dto;

import java.math.BigDecimal;

public class SchoolTypeParam {
	private Integer id ;
	private String name ;
	private BigDecimal primaryRate ;
	private BigDecimal middleRate;
	private BigDecimal highRate ;
	private String note ;
	private String createTime ;
	private String updateTime ;
	private Integer createId;
	private Integer updateId ;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getPrimaryRate() {
		return primaryRate;
	}
	public void setPrimaryRate(BigDecimal primaryRate) {
		this.primaryRate = primaryRate;
	}
	public BigDecimal getMiddleRate() {
		return middleRate;
	}
	public void setMiddleRate(BigDecimal middleRate) {
		this.middleRate = middleRate;
	}
	public String getNote() {
		return note;
	}
	public BigDecimal getHighRate() {
		return highRate;
	}
	public void setHighRate(BigDecimal highRate) {
		this.highRate = highRate;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getCreateId() {
		return createId;
	}
	public void setCreateId(Integer createId) {
		this.createId = createId;
	}
	public Integer getUpdateId() {
		return updateId;
	}
	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}

}
