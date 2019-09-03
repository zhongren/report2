package com.newproj.report.school.dto;

import java.math.BigDecimal;

public class SchoolType {
	private Integer id ;
	private String name ;
	private Integer type ;
	private BigDecimal primaryRate ;
	private BigDecimal middleRate;
	private BigDecimal highRate ;
	private String note ;
	private String createTime ;
	private String updateTime ;
	private Integer createId;
	private String createName ;
	private Integer updateId ;
	private String updateName ;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public void setMiddleRate(BigDecimal middleRate) {
		this.middleRate = middleRate;
	}
	public BigDecimal getHighRate() {
		return highRate;
	}
	public void setHighRate(BigDecimal highRate) {
		this.highRate = highRate;
	}
	public String getNote() {
		return note;
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
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public Integer getUpdateId() {
		return updateId;
	}
	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

}
