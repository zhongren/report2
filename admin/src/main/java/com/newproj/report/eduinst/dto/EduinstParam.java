package com.newproj.report.eduinst.dto ;

import com.newproj.core.beans.Transient;

public class EduinstParam {
	
	private Integer id ; 	//id 
	private String name ; 	//名称 
	private String code ; 	//标识 
	private String type ; 	//教育局类型 
	@Transient
	private Integer parentId ; //归属教育局
	private Integer cityId ;	//市ID
	private Integer countyId ;	//县ID
	private String createTime ; 	//添加日期 
	private Integer createId ; 	//添加人 
	private String updateTime ; 	//更新日期 
	private Integer updateId ; 	//更新人 
	
	@Transient
	private String loginName ;	//登录账号
	@Transient
	private String passwd ;		//登陆密码

	
	public void setId ( Integer id ){ 
		this.id = id ; 
 	}
	public Integer getId ( ) { 
		return this.id ; 
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public void setName ( String name ){ 
		this.name = name ; 
 	}
	public String getName ( ) { 
		return this.name ; 
	}
	public void setCode ( String code ){ 
		this.code = code ; 
 	}
	public String getCode ( ) { 
		return this.code ; 
	}
	public void setType ( String type ){ 
		this.type = type ; 
 	}
	public String getType ( ) { 
		return this.type ; 
	}
	public void setCreateTime ( String createTime ){ 
		this.createTime = createTime ; 
 	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getCountyId() {
		return countyId;
	}
	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
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
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public void setUpdateTime ( String updateTime ){ 
		this.updateTime = updateTime ; 
 	}
	public String getUpdateTime ( ) { 
		return this.updateTime ; 
	}
	public void setUpdateId ( Integer updateId ){ 
		this.updateId = updateId ; 
 	}
	public Integer getUpdateId ( ) { 
		return this.updateId ; 
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

}