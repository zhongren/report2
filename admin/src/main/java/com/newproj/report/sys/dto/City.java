package com.newproj.report.sys.dto ;

import java.util.List;

public class City {
	
	private Integer id ; 	//ID 
	private String name ; 	//名称 
	private Integer level ; 	//层级(1:市,2:区县) 
	private String levelName ;
	private Integer parentId ; 	//上级地区 
	private Integer rank ; 	//排序 
	private String note ; 	//备注 
	private Integer createId ;
	private String createName ;
	private String createTime ;
	private Integer updateId ;
	private String updateName ;
	private String updateTime ;
	
	private String parentName ;
	
	private List<City> subCitys ;

	
	public void setId ( Integer id ){ 
		this.id = id ; 
 	}
	public Integer getId ( ) { 
		return this.id ; 
	}
	public void setName ( String name ){ 
		this.name = name ; 
 	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public String getName ( ) { 
		return this.name ; 
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public void setLevel ( Integer level ){ 
		this.level = level ; 
 	}
	public Integer getLevel ( ) { 
		return this.level ; 
	}
	public void setParentId ( Integer parentId ){ 
		this.parentId = parentId ; 
 	}
	public Integer getParentId ( ) { 
		return this.parentId ; 
	}
	public void setRank ( Integer rank ){ 
		this.rank = rank ; 
 	}
	public Integer getRank ( ) { 
		return this.rank ; 
	}
	public void setNote ( String note ){ 
		this.note = note ; 
 	}
	public String getNote ( ) { 
		return this.note ; 
	}
	public List<City> getSubCitys() {
		return subCitys;
	}
	public void setSubCitys(List<City> subCitys) {
		this.subCitys = subCitys;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

}