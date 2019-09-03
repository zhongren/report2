package com.newproj.report.school.dto ;

import com.newproj.core.beans.Transient;

public class SchoolParam {
	
	private Integer id ; 	//ID 
	private String code ; 	//标识码 
	private String name ; 	//学校名称 
	private String city ; 	//所属市 
	private String county ; 	//所属区县 
	private String regionType ; 	//城乡分类 
	private String runBy ; 	//办学者类型 
	private Integer type ; 	//办学类型 
	private String createTime ; 	//添加日期 
	private Integer createId ; 	//创建人 
	private String updateTime ; 	//更新日期 
	private Integer updateId ;		//更新人
	private Integer eduinstId ;
	@Transient
	private String password ;
	
	@Transient
	private String loginName ;	//登陆名

	
	public Integer getUpdateId() {
		return updateId;
	}
	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}
	public void setId ( Integer id ){ 
		this.id = id ; 
 	}
	public Integer getId ( ) { 
		return this.id ; 
	}
	public void setCode ( String code ){ 
		this.code = code ; 
 	}
	public String getCode ( ) { 
		return this.code ; 
	}
	public void setName ( String name ){ 
		this.name = name ; 
 	}
	public String getName ( ) { 
		return this.name ; 
	}
	public void setCity ( String city ){ 
		this.city = city ; 
 	}
	public String getCity ( ) { 
		return this.city ; 
	}
	public void setCounty ( String county ){ 
		this.county = county ; 
 	}
	public String getCounty ( ) { 
		return this.county ; 
	}
	public void setRegionType ( String regionType ){ 
		this.regionType = regionType ; 
 	}
	public String getRegionType ( ) { 
		return this.regionType ; 
	}
	public void setRunBy ( String runBy ){ 
		this.runBy = runBy ; 
 	}
	public String getRunBy ( ) { 
		return this.runBy ; 
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public void setCreateTime ( String createTime ){ 
		this.createTime = createTime ; 
 	}
	public String getCreateTime ( ) { 
		return this.createTime ; 
	}
	public void setCreateId ( Integer createId ){ 
		this.createId = createId ; 
 	}
	public Integer getCreateId ( ) { 
		return this.createId ; 
	}
	public void setUpdateTime ( String updateTime ){ 
		this.updateTime = updateTime ; 
 	}
	public String getUpdateTime ( ) { 
		return this.updateTime ; 
	}
	public Integer getEduinstId() {
		return eduinstId;
	}
	public void setEduinstId(Integer eduinstId) {
		this.eduinstId = eduinstId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}