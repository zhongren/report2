package com.newproj.report.bulletin.dto;

public class Bulletin {
	private Integer id ;
	private Integer cityId ;
	private Integer countyId ;
	private Integer eduinstId;
	private Integer schoolId ;
	private String categoryCode ;
	private String type ;
	private String title ;
	private String content ;
	private String createTime ;
	private Integer createId ;
	private String createName ;
	private String publishTime ;
	private Integer publishId ;
	private String publishName ;
	private Integer readNum ;
	private Integer status ;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getEduinstId() {
		return eduinstId;
	}
	public void setEduinstId(Integer eduinstId) {
		this.eduinstId = eduinstId;
	}
	public Integer getCountyId() {
		return countyId;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getPublishName() {
		return publishName;
	}
	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}
	public Integer getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public Integer getPublishId() {
		return publishId;
	}
	public void setPublishId(Integer publishId) {
		this.publishId = publishId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getReadNum() {
		return readNum;
	}
	public void setReadNum(Integer readNum) {
		this.readNum = readNum;
	}
}
