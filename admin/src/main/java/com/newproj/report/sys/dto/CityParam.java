package com.newproj.report.sys.dto ;

import java.util.List;

import com.newproj.core.beans.Transient;

public class CityParam {
	
	private Integer id ; 	//ID 
	private String name ; 	//名称 
	private Integer level ; 	//层级(1:市,2:区县) 
	private Integer parentId ; 	//上级地区 
	private Integer rank ; 	//排序 
	private String note ; 	//备注 
	private Integer createId ;
	private String createTime ;
	private Integer updateId ;
	private String updateTime ;
	
	@Transient
	private List<CityParam> subCitys ;

	
	public void setId ( Integer id ){ 
		this.id = id ; 
 	}
	public Integer getId ( ) { 
		return this.id ; 
	}
	public void setName ( String name ){ 
		this.name = name ; 
 	}
	public String getName ( ) { 
		return this.name ; 
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
	public Integer getUpdateId() {
		return updateId;
	}
	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
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
	public List<CityParam> getSubCitys() {
		return subCitys;
	}
	public void setSubCitys(List<CityParam> subCitys) {
		this.subCitys = subCitys;
	}

}